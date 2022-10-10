package com.zgsbrgr.demo.fiba.network

import com.zgsbrgr.demo.fiba.data.RosterDto
import com.zgsbrgr.demo.fiba.data.SectionDto

interface ZGFibaNetworkDataSource {

    suspend fun getResults(): List<SectionDto>

    suspend fun getTeamRoster(teamId: String? = null): List<RosterDto>
}
