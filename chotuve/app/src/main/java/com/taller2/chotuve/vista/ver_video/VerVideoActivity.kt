package com.taller2.chotuve.vista.ver_video

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.taller2.chotuve.R
import com.taller2.chotuve.vista.principal.URI_KEY

class VerVideoActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ver_video)

        val uri = intent.getStringExtra(URI_KEY)
        val textView = findViewById<TextView>(R.id.textViewUri).apply {
            text = uri
        }
    }
}