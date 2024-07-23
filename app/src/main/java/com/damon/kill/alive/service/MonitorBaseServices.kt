package com.damon.kill.alive.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.damon.kill.alive.utils.Logger

open class MonitorBaseServices : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        // 该服务不支持绑定,返回null
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Logger.v(Logger.TAG, "onCreate")
    }
}
