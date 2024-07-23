package com.damon.kill.alive.main

import android.app.Application
import android.content.Context
import com.damon.kill.alive.keeplive.DamonHolder

class AliveApp: Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        DamonHolder.attach(base!!, this)
    }
}