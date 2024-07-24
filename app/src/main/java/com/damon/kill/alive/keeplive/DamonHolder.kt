package com.damon.kill.alive.keeplive

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.damon.kill.alive.keeplive.config.KeepAlive
import com.damon.kill.alive.keeplive.config.KeepAliveConfigs
import com.damon.kill.alive.keeplive.instrumation.DamonInstrumentation
import com.damon.kill.alive.receiver.DamonReceiver
import com.damon.kill.alive.refelction.HiddenApi
import com.damon.kill.alive.service.DamonServices
import com.damon.kill.alive.service.NotifactionServices
import com.damon.kill.alive.utils.Logger


object DamonHolder {
    init {
        // 在Android P及以上版本中,豁免所有隐藏API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            HiddenApi.exemptAll()
        }
    }

    private val connCache = mutableMapOf<Activity, ServiceConnection>()

    fun attach(base: Context, app: Application) {
        // 注册Activity生命周期回调
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                Logger.d(Logger.TAG, "====> [${activity.localClassName}] created")
                // 绑定DamonServices

                ServiceHolder.getInstance().bindService(activity, DamonServices::class.java,object :ServiceHolder.OnServiceConnectionListener{
                    override fun onServiceConnection(
                        connection: ServiceConnection,
                        isConnected: Boolean
                    ) {
                        if (isConnected) {
                            connCache[activity] = connection
                        }
                    }
                })
            }

            override fun onActivityStarted(activity: Activity) {
                Logger.v(Logger.TAG, "====> [${activity.localClassName}] started")
            }

            override fun onActivityResumed(activity: Activity) {
                Logger.v(Logger.TAG, "====> [${activity.localClassName}] resumed")
            }

            override fun onActivityPaused(activity: Activity) {
                Logger.v(Logger.TAG, "====> [${activity.localClassName}] paused")
            }

            override fun onActivityStopped(activity: Activity) {
                Logger.v(Logger.TAG, "====> [${activity.localClassName}] stopped")
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                Logger.v(Logger.TAG, "====> [${activity.localClassName}] save instance state")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Logger.v(Logger.TAG, "====> [${activity.localClassName}] destroyed")
                connCache[activity]?.let {
                    ServiceHolder.getInstance().unbindService(activity, it)
                }
            }
        })

        // 启动守护进程
        JavaDamon.getInstance().fire(
            base,
            Intent(base, DamonServices::class.java),
            Intent(base, DamonReceiver::class.java),
            Intent(base, DamonInstrumentation::class.java)
        )

        // 初始化KeepAlive
        val configs = KeepAliveConfigs(
            KeepAliveConfigs.Config(
                "${base.packageName}:resident",
                NotifactionServices::class.java.canonicalName
            )
        )
        // configs.ignoreBatteryOptimization()
        // configs.rebootThreshold(10 * 1000, 3)
//        configs.setOnBootReceivedListener { context, intent ->
//            Logger.d(Logger.TAG, "############################# onReceive(): intent=$intent")
//            ContextCompat.startForegroundService(context, Intent(context, DamonServices::class.java))
//        }
        KeepAlive.init(base, configs)
    }
}
