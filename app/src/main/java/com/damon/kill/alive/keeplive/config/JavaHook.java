package com.damon.kill.alive.keeplive.config;

import android.app.Instrumentation;

import com.damon.kill.alive.keeplive.instrumation.DamonInstrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JavaHook {
    private static final String TAG = "Hooker";

    public static void hookInstrumentation() throws Exception {
        Class<?> activityThread = Class.forName("android.app.ActivityThread");
        Method sCurrentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
        sCurrentActivityThread.setAccessible(true);
        //获取ActivityThread 对象
        Object activityThreadObject = sCurrentActivityThread.invoke(activityThread);

        //获取 Instrumentation 对象
        Field mInstrumentation = activityThread.getDeclaredField("mInstrumentation");
        mInstrumentation.setAccessible(true);
        Instrumentation instrumentation = (Instrumentation) mInstrumentation.get(activityThreadObject);
        DamonInstrumentation customInstrumentation = new DamonInstrumentation();
        //将我们的 customInstrumentation 设置进去
        mInstrumentation.set(activityThreadObject, customInstrumentation);
    }
}
