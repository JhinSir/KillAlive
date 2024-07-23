package com.damon.kill.alive.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.damon.kill.alive.service.DamonServices
import com.damon.kill.alive.utils.Logger

open class DamonReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Logger.v(Logger.TAG, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! receiver: $intent")

        // 启动前台服务DamonServices
        ContextCompat.startForegroundService(context, Intent(context, DamonServices::class.java))
    }
}
