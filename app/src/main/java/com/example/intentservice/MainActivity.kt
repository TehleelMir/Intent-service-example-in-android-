package com.example.intentservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat

/*
    The intent service class is just a subclass of normal service, with once Exception and that is, it use the
    background thread to run the service and not the main thread like service does.

    And when it finishes all the work it automatically stops itself.
    And since this is service, that mean it can't run in the background when the app is closed from android 8 and higher for that we have to use
    job intent service but for the job as we know its not accurate. But we can also run intent service as a foreground service that way it can run
    in the background as well.
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        val editText = findViewById<EditText>(R.id.editText)
        findViewById<Button>(R.id.start)
            .setOnClickListener {
                val str = editText.text.toString()

                val intent = Intent(this, IntentServiceClass::class.java)
                intent.putExtra("key", "hello world")

                ContextCompat.startForegroundService(this, intent)
            }
    }
}