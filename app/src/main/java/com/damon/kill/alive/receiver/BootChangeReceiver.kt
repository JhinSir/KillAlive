package com.damon.kill.alive.receiver

import android.content.Context
import android.content.Intent
import com.damon.kill.alive.keeplive.config.KeepAliveConfigs

class BootChangeReceiver:DamonReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (KeepAliveConfigs.bootReceivedListener != null) {
            KeepAliveConfigs.bootReceivedListener.onReceive(context, intent)
        }
    }
}