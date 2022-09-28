package com.zgsbrgr.demo.fiba.data

import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface Api {

    @Serializable
    data class NetworkResponse<T>(
        val sections: T
    )

    @GET("results.json")
    suspend fun fetchResults(): NetworkResponse<List<SectionDto>>
}
