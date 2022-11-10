@file:Suppress("MagicNumber")
package com.zgsbrgr.demo.fiba.network.retrofit

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zgsbrgr.demo.fiba.data.RosterDto
import com.zgsbrgr.demo.fiba.data.SectionDto
import com.zgsbrgr.demo.fiba.network.ZGFibaNetworkDataSource
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

interface RetrofitZGFibaNetworkApi {

    @GET(value = "results.json")
    suspend fun fetchResults(): NetworkResponse<List<SectionDto>>

    @GET(value = "teams/{id}.json")
    suspend fun fetchTeamRoster(@Path("id") teamId: String?): NetworkResponse<List<RosterDto>>
}

@Serializable
data class NetworkResponse<T>(
    val data: T
)

sealed class Resource<T> {
    @Serializable
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val error: Throwable) : Resource<T>()
    class Loading<T> : Resource<T>()
}

private const val CACHE_SIZE = 5 * 1024 * 1024L // 5 MB
const val CACHE_CONTROL_HEADER = "Cache-Control"
const val CACHE_CONTROL_NO_CACHE = "no-cache"

private fun httpCache(application: Application): Cache {
    return Cache(application.applicationContext.cacheDir, CACHE_SIZE)
}

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)

        val shouldUseCache = request.header(CACHE_CONTROL_HEADER) != CACHE_CONTROL_NO_CACHE
        if (!shouldUseCache) return originalResponse

        val cacheControl = CacheControl.Builder()
            .maxAge(10, TimeUnit.MINUTES)
            .build()

        return originalResponse.newBuilder()
            .header(CACHE_CONTROL_HEADER, cacheControl.toString())
            .build()
    }
}

@Singleton
class RetrofitZGFibaNetwork @Inject constructor(
    application: Application,
    networkJson: Json
) : ZGFibaNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl("https://dev.zgsbrgr.com/fiba/")
        .client(
            OkHttpClient.Builder()
                .cache(httpCache(application))
                .addNetworkInterceptor(CacheInterceptor())
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
                .build()
        )
        .addConverterFactory(
            @OptIn(ExperimentalSerializationApi::class)
            networkJson.asConverterFactory("application/json".toMediaType())
        )
        .build()
        .create(RetrofitZGFibaNetworkApi::class.java)

    override suspend fun getResults(): List<SectionDto> =
        networkApi.fetchResults().data

    override suspend fun getTeamRoster(teamId: String?): List<RosterDto> =
        networkApi.fetchTeamRoster(teamId = teamId).data
}
