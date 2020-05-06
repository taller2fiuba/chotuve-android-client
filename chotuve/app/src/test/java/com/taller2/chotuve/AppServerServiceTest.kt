package com.taller2.chotuve

import org.junit.Assert.assertEquals
import org.junit.Test

class AppServerServiceTest {

    @Test
    fun appServerCorrect() {
        // habría que ver la manera de mockear esto
        val response = AppServerService.create().ping().execute()
        val code = response.code()

        assertEquals(code, 200)
    }
}