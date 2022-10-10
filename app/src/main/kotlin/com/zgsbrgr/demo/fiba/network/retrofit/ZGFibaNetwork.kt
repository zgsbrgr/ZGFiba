package com.zgsbrgr.demo.fiba.network.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.zgsbrgr.demo.fiba.data.RosterDto
import com.zgsbrgr.demo.fiba.data.SectionDto
import com.zgsbrgr.demo.fiba.network.ZGFibaNetworkDataSource
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
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

@Singleton
class RetrofitZGFibaNetwork @Inject constructor(
    networkJson: Json
) : ZGFibaNetworkDataSource {

    private val networkApi = Retrofit.Builder()
        .baseUrl("https://dev.zgsbrgr.com/fiba/")
        .client(
            OkHttpClient.Builder()
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
