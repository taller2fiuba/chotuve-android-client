package com.taller2.chotuve

import com.taller2.chotuve.modelo.AppServerService
import com.taller2.chotuve.modelo.data.Video
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

    @Test
    fun appServerCrearVideo() {
        // habría que ver la manera de mockear esto
        val response = AppServerService.create().crearVideo(
            Video(
                "test android",
                "https://android/test"
            )
        ).execute()
        val code = response.code()

        assertEquals(201,code )
    }

    @Test
    fun appServerNoCrearVideo() {
        // habría que ver la manera de mockear esto
        val response = AppServerService.create().crearVideo(
            Video(
                "",
                "https://android/test"
            )
        ).execute()
        val errorJson = response.errorBody()!!.string()
        // no se como parsear
        println(errorJson)
        assertEquals(response.code(), 400)
    }
}
