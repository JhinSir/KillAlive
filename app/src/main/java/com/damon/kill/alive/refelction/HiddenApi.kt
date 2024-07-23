package com.damon.kill.alive.refelction

import android.util.Log
import java.lang.reflect.Method

object HiddenApi {
    // 保存setHiddenApiExemptions方法的引用
    private var sSetHiddenApiExemptions: Method? = null
    // 保存VMRuntime对象的引用
    private var sVMRuntime: Any? = null

    init {
        try {
            // 通过反射获取Class.forName和Class.getDeclaredMethod方法
            val forNameMethod = Class::class.java.getDeclaredMethod("forName", String::class.java)
//            val getDeclaredMethodMethod = Class::class.java.getDeclaredMethod(
//                "getDeclaredMethod", String::class.java, Array<Class<*>>::class.java
//            )

            // 正确的方式是使用java.lang.reflect.Array
            val arrayClass = java.lang.reflect.Array.newInstance(Class::class.java, 0).javaClass
            val getDeclaredMethodMethod = Class::class.java.getDeclaredMethod(
                "getDeclaredMethod", String::class.java, arrayClass
            )

            // 获取dalvik.system.VMRuntime类
            val vmRuntimeClass = forNameMethod.invoke(null, "dalvik.system.VMRuntime") as Class<*>
            // 获取VMRuntime的setHiddenApiExemptions方法
            sSetHiddenApiExemptions = getDeclaredMethodMethod.invoke(
                vmRuntimeClass, "setHiddenApiExemptions", arrayOf(Array<String>::class.java)
            ) as Method
            // 获取VMRuntime的getRuntime方法
            val getVMRuntimeMethod = getDeclaredMethodMethod.invoke(vmRuntimeClass, "getRuntime", null) as Method
            // 获取VMRuntime实例
            sVMRuntime = getVMRuntimeMethod.invoke(null)
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }

    // 设置隐藏API的豁免
    fun setExemptions(vararg methods: String): Boolean {
        if (sSetHiddenApiExemptions == null || sVMRuntime == null) {
            return false
        }

        return try {
            // 调用setHiddenApiExemptions方法,传入需要豁免的API列表
            sSetHiddenApiExemptions!!.invoke(sVMRuntime, arrayOf(methods))
            true
        } catch (th: Throwable) {
            th.printStackTrace()
            false
        }
    }

    // 豁免所有隐藏API
    fun exemptAll(): Boolean {
        Log.i("HiddenApi", "Start execute exemptAll method ...")
        return setExemptions("L")
    }
}
