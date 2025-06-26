package ru.kolesnik.potok.core.network.datasource.impl

import android.util.Log
import ru.kolesnik.potok.core.network.api.AuthApi
import ru.kolesnik.potok.core.network.datasource.AuthDataSource
import javax.inject.Inject

class RetrofitAuthDataSource @Inject constructor(
    private val api: AuthApi
) : AuthDataSource {
    override suspend fun auth(): Boolean {
        return try {
            val response = api.auth()
            if (response.isSuccessful) {
                Log.d("Auth", "Authentication successful")
                true
            } else {
                Log.e("Auth", "Authentication failed: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("Auth", "Authentication error", e)
            false
        }
    }
}