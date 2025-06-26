package ru.kolesnik.potok.core.network.cookie

import android.os.Build
import android.util.Log
import java.net.CookieStore
import java.net.HttpCookie
import java.net.URI
import java.net.URISyntaxException
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

open class InMemoryCookieStore(private val name: String) : CookieStore {

    private val uriIndex = ConcurrentHashMap<URI, MutableList<HttpCookie>>()
    private val lock = ReentrantLock(false)
    private var lastCleanup = System.currentTimeMillis()
    private var lastUri: URI? = null
    private var lastCookies: List<HttpCookie> = emptyList()

    companion object {
        private const val CLEANUP_INTERVAL = 60 * 1000L // 1 минута
        private const val TAG = "InMemoryCookieStore"
    }

    private fun checkCleanup() {
        if (System.currentTimeMillis() - lastCleanup > CLEANUP_INTERVAL) {
            lock.lock()
            try {
                // Удаляем просроченные куки
                for (list in uriIndex.values) {
                    val it = list.iterator()
                    while (it.hasNext()) {
                        if (it.next().hasExpired()) {
                            it.remove()
                        }
                    }
                }
                lastCleanup = System.currentTimeMillis()
            } finally {
                lock.unlock()
            }
        }
    }

    override fun removeAll(): Boolean {
        lock.lock()
        try {
            val result = uriIndex.isNotEmpty()
            uriIndex.clear()
            Log.d(TAG, "[$name] Removed all cookies")
            return result
        } finally {
            lock.unlock()
        }
    }

    override fun add(uri: URI?, cookie: HttpCookie?) {
        if (cookie == null) {
            Log.i(TAG, "[$name] tried to add null cookie. Doing nothing.")
            return
        }

        if (uri == null) {
            Log.i(TAG, "[$name] tried to add null URI. Doing nothing.")
            return
        }

        lock.lock()
        try {
            checkCleanup()
            addIndex(getEffectiveURI(uri), cookie)
            // Сбрасываем кэш при добавлении новых кук
            lastUri = null
            Log.d(TAG, "[$name] Added cookie: ${cookie.name}=${cookie.value} for ${uri.host}")
        } finally {
            lock.unlock()
        }
    }

    override fun getCookies(): List<HttpCookie> {
        checkCleanup()
        lock.lock()
        try {
            val rt = arrayListOf<HttpCookie>()
            for (list in uriIndex.values) {
                val it = list.iterator()
                while (it.hasNext()) {
                    val cookie = it.next()
                    if (cookie.hasExpired()) {
                        it.remove()
                    } else if (!rt.contains(cookie)) {
                        rt.add(cookie)
                    }
                }
            }
            Log.d(TAG, "[$name] Retrieved ${rt.size} cookies")
            return Collections.unmodifiableList(rt)
        } finally {
            lock.unlock()
        }
    }

    override fun getURIs(): List<URI> {
        lock.lock()
        try {
            val result = ArrayList(uriIndex.keys)
            result.remove(null)
            return Collections.unmodifiableList(result)
        } finally {
            lock.unlock()
        }
    }

    override fun remove(uri: URI?, cookie: HttpCookie?): Boolean {
        if (cookie == null) {
            Log.i(TAG, "[$name] tried to remove null cookie. Doing nothing.")
            return true
        }

        if (uri == null) {
            Log.i(TAG, "[$name] tried to remove null URI. Doing nothing.")
            return true
        }

        lock.lock()
        try {
            val lintedURI = getEffectiveURI(uri)
            if (uriIndex[lintedURI] == null) {
                return false
            } else {
                val cookies = uriIndex[lintedURI]
                val removed = cookies?.remove(cookie) ?: false
                if (removed) {
                    lastUri = null  // Сбрасываем кэш при изменении
                    Log.d(TAG, "[$name] Removed cookie: ${cookie.name} for ${uri.host}")
                }
                return removed
            }
        } finally {
            lock.unlock()
        }
    }

    override fun get(uri: URI?): List<HttpCookie> {
        if (uri == null) {
            Log.i(TAG, "[$name] getting cookies for null URI results in empty list")
            return emptyList()
        }

        checkCleanup()

        // Используем кэш для идентичных URI
        val effectiveUri = getEffectiveURI(uri)
        if (effectiveUri == lastUri) {
            return lastCookies
        }

        lock.lock()
        try {
            val cookies = arrayListOf<HttpCookie>()
            uri.host?.let { cookies.addAll(getInternal1(it)) }
            val internal2 = getInternal2(effectiveUri).filter { !cookies.contains(it) }
            cookies.addAll(internal2)

            // Обновляем кэш
            lastUri = effectiveUri
            lastCookies = cookies

            Log.d(TAG, "[$name] Retrieved ${cookies.size} cookies for ${uri.host}")
            return cookies
        } finally {
            lock.unlock()
        }
    }

    internal fun getEffectiveURI(uri: URI): URI {
        return try {
            URI(uri.scheme ?: "http", uri.host, null, null, null)
        } catch (ignored: URISyntaxException) {
            uri
        }
    }

    private fun addIndex(index: URI, cookie: HttpCookie) {
        val cookies = uriIndex[index]
        if (cookies != null) {
            cookies.remove(cookie)
            cookies.add(cookie)
        } else {
            val newCookies = ArrayList<HttpCookie>()
            newCookies.add(cookie)
            uriIndex[index] = newCookies
        }
    }

    private fun netscapeDomainMatches(domain: String?, host: String?): Boolean {
        if (domain == null || host == null) {
            return false
        }

        // if there's no embedded dot in domain and domain is not .local
        val isLocalDomain = ".local".equals(domain, ignoreCase = true)
        var embeddedDotInDomain = domain.indexOf('.')
        if (embeddedDotInDomain == 0) {
            embeddedDotInDomain = domain.indexOf('.', 1)
        }
        if (!isLocalDomain && (embeddedDotInDomain == -1 || embeddedDotInDomain == domain.length - 1)) {
            return false
        }

        // if the host name contains no dot and the domain name is .local
        val firstDotInHost = host.indexOf('.')
        if (firstDotInHost == -1 && isLocalDomain) {
            return true
        }

        val domainLength = domain.length
        val lengthDiff = host.length - domainLength
        if (lengthDiff == 0) {
            // if the host name and the domain name are just string-compare euqal
            return host.equals(domain, ignoreCase = true)
        } else if (lengthDiff > 0) {
            // need to check H & D component
            val D = host.substring(lengthDiff)

            // Android-changed: b/26456024 targetSdkVersion based compatibility for domain matching
            // Android M and earlier: Cookies with domain "foo.com" would not match "bar.foo.com".
            // The RFC dictates that the user agent must treat those domains as if they had a
            // leading period and must therefore match "bar.foo.com".
            return if (Build.VERSION.SDK_INT <= 23 && !domain.startsWith(".")) {
                false
            } else D.equals(domain, ignoreCase = true)
        } else if (lengthDiff == -1) {
            // if domain is actually .host
            return domain[0] == '.' && host.equals(domain.substring(1), ignoreCase = true)
        }

        return false
    }

    private fun getInternal1(host: String): List<HttpCookie> {
        // BEGIN Android-changed: b/25897688 InMemoryCookieStore ignores scheme (http/https)
        // Use a separate list to handle cookies that need to be removed so
        // that there is no conflict with iterators.
        val toRemove = ArrayList<HttpCookie>()
        val cookies = ArrayList<HttpCookie>()

        for ((_, lst) in uriIndex) {
            for (c in lst) {
                val domain = c.domain
                if (c.version == 0 && netscapeDomainMatches(
                        domain,
                        host
                    ) || c.version == 1 && HttpCookie.domainMatches(domain, host)
                ) {

                    // the cookie still in main cookie store
                    if (!c.hasExpired()) {
                        // don't add twice
                        if (!cookies.contains(c)) {
                            cookies.add(c)
                        }
                    } else {
                        toRemove.add(c)
                    }
                }
            }
            // Clear up the cookies that need to be removed
            for (c in toRemove) {
                lst.remove(c)
            }
            toRemove.clear()
        }

        return cookies
    }

    private fun getInternal2(comparator: URI): List<HttpCookie> {
        // BEGIN Android-changed: b/25897688 InMemoryCookieStore ignores scheme (http/https)
        // Removed cookieJar
        val cookies = ArrayList<HttpCookie>()

        for (index in uriIndex.keys) {
            if (index === comparator || comparator.compareTo(index) == 0) {
                val indexedCookies = uriIndex[index]
                // check the list of cookies associated with this domain
                if (indexedCookies != null) {
                    val it = indexedCookies.iterator()
                    while (it.hasNext()) {
                        val ck = it.next()
                        // the cookie still in main cookie store
                        if (!ck.hasExpired()) {
                            // don't add twice
                            if (!cookies.contains(ck))
                                cookies.add(ck)
                        } else {
                            it.remove()
                        }
                    }
                } // end of indexedCookies != null
            } // end of comparator.compareTo(index) == 0
        } // end of cookieIndex iteration

        return cookies
    }
}