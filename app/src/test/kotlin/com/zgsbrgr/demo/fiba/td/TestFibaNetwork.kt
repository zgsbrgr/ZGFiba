package com.zgsbrgr.demo.fiba.td

import com.zgsbrgr.demo.fiba.data.RosterDto
import com.zgsbrgr.demo.fiba.data.SectionDto
import com.zgsbrgr.demo.fiba.network.retrofit.NetworkResponse
import com.zgsbrgr.demo.fiba.network.retrofit.RetrofitZGFibaNetworkApi

class TestFibaNetwork : RetrofitZGFibaNetworkApi {

    override suspend fun fetchResults(): NetworkResponse<List<SectionDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchTeamRoster(teamId: String?): NetworkResponse<List<RosterDto>> {
        TODO("Not yet implemented")
    }
}
