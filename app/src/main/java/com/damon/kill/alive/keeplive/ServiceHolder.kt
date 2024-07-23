package com.damon.kill.alive.keeplive

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.damon.kill.alive.IMyAidlInterface
import com.damon.kill.alive.utils.Logger

class ServiceHolder private constructor() {
    // 用于缓存ServiceConnection的连接状态
    private val connCache = mutableMapOf<ServiceConnection, Boolean>()

    // Holder类用于实现单例模式
    private object Holder {
        val  INSTANCE = ServiceHolder()
    }

    // 获取ServiceHolder的单例实例
    companion object {
        fun getInstance(): ServiceHolder {
            return Holder.INSTANCE
        }
    }

    // ServiceConnection的实现类
    inner class ServiceConnectionImpl(private val listener: OnServiceConnectionListener?) : ServiceConnection {
        private var isConnected = false // 服务连接状态
        private var monitorService: IMyAidlInterface? = null // 服务接口实例

        // 服务断开连接时调用
        override fun onServiceDisconnected(name: ComponentName?) {
            listener?.onServiceConnection(this, false) // 通知监听器服务断开
            isConnected = false
            connCache[this] = false // 更新缓存状态
        }

        // 服务连接时调用
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Logger.d(Logger.TAG, "ComponentName: $name, IBinder: $service")
            monitorService = IMyAidlInterface.Stub.asInterface(service) // 获取服务接口实例
            Logger.d(Logger.TAG, "IBinder asInterface: $monitorService")
            if (listener != null && monitorService != null) {
                listener.onServiceConnection(this, true) // 通知监听器服务连接成功
            }
            isConnected = true
            connCache[this] = true // 更新缓存状态
        }

        // 检查服务是否连接
        fun isConnected(): Boolean {
            return isConnected
        }

        // 获取监控服务实例
        fun getMonitorService(): IMyAidlInterface? {
            return monitorService
        }
    }

    // 绑定服务的方法
    fun bindService(context: Context, clazz: Class<out Service>, listener: OnServiceConnectionListener?): Boolean {
        val bindIntent = Intent(context, clazz) // 创建绑定意图
        bindIntent.action = "${context.packageName}.monitor.bindService" // 设置意图的动作
        Logger.i(Logger.TAG, "call bindService(): $bindIntent")
        // 绑定服务并返回绑定结果
        return context.bindService(bindIntent, ServiceConnectionImpl(listener), Context.BIND_AUTO_CREATE)
    }

    // 解绑服务的方法
    fun unbindService(context: Context, connection: ServiceConnection?) {
        if (connection is ServiceConnectionImpl && connection.isConnected()) {
            try {
                Logger.i(Logger.TAG, "call unbindService(): $connection")
                context.unbindService(connection) // 解绑服务
                connCache.remove(connection) // 从缓存中移除连接
            } catch (th: Throwable) {
                th.printStackTrace() // 打印异常堆栈
            }
        }
    }

    // 服务连接的监听器接口
    interface OnServiceConnectionListener {
        fun onServiceConnection(connection: ServiceConnection, isConnected: Boolean) // 服务连接回调
    }
}
