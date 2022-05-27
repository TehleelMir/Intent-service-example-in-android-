package com.example.intentservice

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.intentservice.MyApplication.Companion.CHANNEL_ID

/*
    IntentService won't work from android 11 or api 30, as it has been
    deprecated instead use work manger or job intent service
 */

class IntentServiceClass: IntentService("IntentServiceClass") {
    private lateinit var wakeLock: PowerManager.WakeLock

    init {
        setIntentRedelivery(true) // return true so that if service got killed it will start again and false for the otherwise
    }

    override fun onCreate() {
        super.onCreate()

        /*
            The below code will keep the cup running even if user turn of the screen.
         */
        val powerManger = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManger.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "IntentService: WakeLock")
        wakeLock.acquire(1000 * 60 * 10) // keep the cup running if the phone display is turn off, for 10 minutes at lest.

        /*
            Because we only want to make this service foreground service if the
            build version is >= O
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Some title here")
                .setContentText("Running")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

            /*
                this will change the service into foreground service but it will still
                run on a background thread and will also stop itself once its work is done.
             */
            startForeground(1, notification)
        }
    }

    /*
        The below method will get executed on a background thread.
        But no matter how many times we call the service again it won't create thread of each
        intent, instead it will executed all the services calls Sequentially on a 1 background thread.
     */
    override fun onHandleIntent(intent: Intent?) {
        Log.i("here22", "In onHandleIntent")
        val str = intent?.extras?.getString("key") ?: ""

        for(i in 1..10) {
            Log.i("here22", "$str => Running in background tread.... $i")
            Thread.sleep(2000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        wakeLock.release()
    }
}