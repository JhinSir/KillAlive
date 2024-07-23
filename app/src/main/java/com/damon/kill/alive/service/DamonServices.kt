package com.damon.kill.alive.service

import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.damon.kill.alive.utils.Logger

class DamonServices : BaseServices() {
    // 在绑定服务时,返回父类的Binder实现
    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    // 在服务创建时调用
    override fun onCreate() {
        try {
            // 启动前台服务NotifyResidentService
            ContextCompat.startForegroundService(
                this,
                Intent().setClassName(packageName, NotifactionServices::class.java.name)
            )
        } catch (th: Throwable) {
            Logger.e(Logger.TAG, "failed to start foreground service: ${th.message}")
        }

        // 启动MonitorService1
        startService(
            Intent().setClassName(packageName, MonitorService1::class.java.name)
        )

        // 启动MonitorService2
        startService(
            Intent().setClassName(packageName, MonitorService2::class.java.name)
        )

        // 调用父类的onCreate方法
        super.onCreate()
    }
}
