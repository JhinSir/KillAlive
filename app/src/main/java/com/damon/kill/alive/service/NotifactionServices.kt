package com.damon.kill.alive.service

import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.Nullable
import com.damon.kill.alive.keeplive.ServiceHolder
import com.damon.kill.alive.utils.Logger
import com.damon.kill.alive.utils.NotificationUtil

class NotifactionServices : KeepAliveService() {
    private val handler = Handler(Looper.getMainLooper())
    private var notificationTask: (() -> Unit)? = null
    private var i=0

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        // 该服务不支持绑定,返回null
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // 执行父类的onCreate逻辑

        // 定义显示通知的函数
        notificationTask = {
            val noti = NotificationUtil.createNotification(
                this@NotifactionServices,
                0, // 这里可以设置通知图标
                "通知标题", // 这里可以设置通知标题
                "这是一个定时通知", // 这里可以设置通知内容
                null // 这里可以设置点击通知后打开的Activity
            )
            NotificationUtil.showNotification(this@NotifactionServices, noti)
        }



    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.d(
            Logger.TAG,
            "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ " +
                    "intent: $intent, startId: $startId"
        )

        // 启动定时通知任务
        startNotificationTask()

        // 绑定DamonServices
        ServiceHolder.getInstance().bindService(this, DamonServices::class.java, null)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 在服务销毁时停止定时任务
        stopNotificationTask()
        Log.d("xuewenwen","服务销毁----------")
    }

    private fun startNotificationTask() {
        notificationTask?.let { task ->
            handler.postDelayed(
                {
                    task.invoke()
                    startNotificationTask()
                    Log.d("xiucai","+++++++++++++++++${i++}")
                },
                5000
            )
        }
    }

    private fun stopNotificationTask() {
        Log.d("xuewenwen","服务销毁-  stopNotificationTask---------")
        handler.removeCallbacksAndMessages(null)
        notificationTask = null
    }
}

