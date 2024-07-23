package com.damon.kill.alive.service

import android.content.Intent
import android.os.IBinder
import com.damon.kill.alive.keeplive.ServiceHolder

class MonitorService1 : MonitorBaseServices() {
    override fun onBind(intent: Intent?): IBinder? {
        // 该服务不支持绑定,返回null
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // 绑定DamonService
        ServiceHolder.getInstance().bindService(this, DamonServices::class.java, null)
    }
}
