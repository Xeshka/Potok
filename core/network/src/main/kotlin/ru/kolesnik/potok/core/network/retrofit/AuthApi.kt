package ru.kolesnik.potok.core.network.retrofit

import androidx.tracing.trace
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ru.kolesnik.potok.core.network.BuildConfig
import ru.kolesnik.potok.core.network.AddressbookSource
import ru.kolesnik.potok.core.network.AuthApi
import ru.kolesnik.potok.core.network.model.customSerializersModule
import ru.kolesnik.potok.core.network.model.employee.EmployeeResponse
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

private const val RANDM_BASE_URL = BuildConfig.BACKEND_URL
private val employeeCache = ConcurrentHashMap<String, EmployeeResponse>()

@Singleton
internal class RetrofitNetworkFull @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
) : AddressbookSource {

    private val networkApi = trace("RetrofitRandmNetwork") {
        Retrofit.Builder()
            .baseUrl(RANDM_BASE_URL)
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(
                GlobalJson.instance.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(AuthApi::class.java)
    }

    override suspend fun getEmployee(
        employeeNumbers: List<String>,
        avatar: Boolean
    ): List<EmployeeResponse> {
        return employeeNumbers.distinct().mapNotNull { employeeNumber ->
            employeeCache[employeeNumber] ?: run {
                networkApi.getEmployee(employeeNumber, avatar = true).firstOrNull()
                    .also {
                        if (it != null) employeeCache[employeeNumber] = it
                    }
            }
        }
    }

}

object GlobalJson {
    val instance = Json {
        serializersModule = customSerializersModule
        ignoreUnknownKeys = true      // Игнорировать неизвестные ключи
        explicitNulls = false         // Не требовать null для отсутствующих полей
        coerceInputValues = true      // Автоматически преобразовывать неверные значения
        prettyPrint = true            // Для красивого вывода при сериализации
        isLenient = true              // Ленивый парсинг
        encodeDefaults = true         // Сериализовать значения по умолчанию
    }
}