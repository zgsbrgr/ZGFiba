package com.zgsbrgr.demo.fiba.network.fake

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FakeZGFibaNetworkDataSourceTest {

    private lateinit var subject: FakeZGFibaNetworkDataSource

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        subject = FakeZGFibaNetworkDataSource(
            ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true }
        )
    }

    @Test
    fun testDeserializationOfMatches() = runTest(testDispatcher) {
        Assert.assertEquals(
            FakeDataSource.sampleSection,
            subject.getResults().first()
        )
    }


    @Test
    fun testDeserializationOfRosters() = runTest(testDispatcher) {
        Assert.assertEquals(
            FakeDataSource.sampleRoster,
            subject.getTeamRoster(null).first()
        )
    }
}
