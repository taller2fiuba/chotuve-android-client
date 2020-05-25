package com.taller2.chotuve

import android.app.Application
import android.content.Context

class Chotuve : Application() {
    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}