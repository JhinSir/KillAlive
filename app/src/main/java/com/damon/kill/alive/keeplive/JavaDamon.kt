package com.damon.kill.alive.keeplive

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.damon.kill.alive.keeplive.bean.DamonBean
import com.damon.kill.alive.keeplive.config.NativeKeepAlive
import com.damon.kill.alive.sculder.FutureScheduler
import com.damon.kill.alive.sculder.SingleThreadFutureScheduler
import com.damon.kill.alive.service.DamonServices

import com.damon.kill.alive.utils.Logger
import com.damon.kill.alive.utils.ToolsUtils

class JavaDamon private constructor() {
    // 定义一个分隔符常量
    private val COLON_SEPARATOR = ":"
    // 用于调度任务的单线程调度器
    private var futureScheduler: FutureScheduler? = null

    init {
        // 双重检查锁定的懒加载
        if (futureScheduler == null) {
            synchronized(JavaDamon::class.java) {
                if (futureScheduler == null) {
                    futureScheduler = SingleThreadFutureScheduler(
                        "javadamon-holder",
                        true
                    )
                }
            }
        }
    }

    // Holder类用于持有JavaDaemon的单例实例
    private object Holder {
        // 创建JavaDaemon的单例实例
        @JvmStatic
        val INSTANCE: JavaDamon = JavaDamon()
    }

    // 获取JavaDaemon的单例实例
    companion object {
        @JvmStatic
        fun getInstance(): JavaDamon {
            return Holder.INSTANCE
        }
    }

    // 启动守护进程
    fun fire(context: Context, intent: Intent, intent2: Intent, intent3: Intent) {
        val env = DamonBean()
        val applicationInfo = context.applicationInfo
        // 设置Damon环境的公共源目录和本地库目录
        env.publicSourceDir = applicationInfo.publicSourceDir
        env.nativeLibraryDir = applicationInfo.nativeLibraryDir
        env.intent = intent
        env.intent2 = intent2
        env.intent3 = intent3
        env.processName = ToolsUtils.getProcessName()

        // 定义要启动的子进程
        val strArr = arrayOf("guard", "monitor1", "monitor2")
        fire(context, env, strArr)
    }

    // 执行具体的进程启动逻辑
    private fun fire(context: Context, env: DamonBean, strArr: Array<String>) {
        Logger.i(Logger.TAG, "############################################## !!! fire(): " +
                "env=$env, strArr=${strArr.joinToString()}")
        val processName =  ToolsUtils.getProcessName()
        Logger.v(Logger.TAG, "processName: $processName")

        // 检查当前进程名称
        if (processName!!.startsWith(context.packageName) && processName!!.contains(COLON_SEPARATOR)) {
            val substring = processName?.substring(processName.lastIndexOf(COLON_SEPARATOR) + 1)
            val list = mutableListOf<String>()
            var isMatchFound = false

            // 遍历目标进程名数组
            for (str in strArr) {
                if (str == substring) {
                    isMatchFound = true
                } else {
                    list.add(str)
                }
            }

            // 如果当前进程是目标进程之一，则进行文件锁定
            if (isMatchFound) {
                Logger.v(Logger.TAG, "app lock file start: $substring")
                NativeKeepAlive.lockFile("${context.filesDir}/$substring${"_d"}")
                Logger.v(Logger.TAG, "app lock file finish")

                // 创建锁定文件的数组
                val strArr2 = list.map { "${context.filesDir}/$it${"_d"}" }.toTypedArray()
                futureScheduler?.scheduleFuture(AppProcessRunable(env, strArr2, "damon"), 0)
            }
        } else if (processName == context.packageName) {
            // 当前进程是主进程，启动前台服务
            ContextCompat.startForegroundService(context, Intent(context, DamonServices::class.java))
        }
    }
}
