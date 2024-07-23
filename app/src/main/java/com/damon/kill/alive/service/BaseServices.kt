package com.damon.kill.alive.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import androidx.annotation.Nullable
import com.damon.kill.alive.IMyAidlInterface
import com.damon.kill.alive.utils.Logger
import com.damon.kill.alive.utils.NotificationUtil

abstract class BaseServices : Service() {

    // 创建IMonitorService的Binder
    private val binder = object : IMyAidlInterface.Stub() {
        @Throws(RemoteException::class)
        override fun processMessage(bundle: Bundle) {
            // 调用自身的processMessage方法
            this@BaseServices.processMessage(bundle)
        }
    }

    // 处理接收到的消息
    private fun processMessage(bundle: Bundle) {
        // 具体的消息处理逻辑可以在这里实现
    }

    override fun onCreate() {
        super.onCreate()
        // 创建并显示通知
        val noti = NotificationUtil.createNotification(
            this,
            0,
            null,
            null,
            null
        )
        NotificationUtil.showNotification(this, noti)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.d(Logger.TAG, "############### intent: $intent, startId: $startId")
        return super.onStartCommand(intent, flags, startId)
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
}
