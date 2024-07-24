package com.damon.kill.alive.keeplive.config;

import java.lang.reflect.Field;

public class IBinderManager {
    // 通过invoke方法获取START_SERVICE_TRANSACTION的值
    private int startService = invoke("TRANSACTION_startService", "START_SERVICE_TRANSACTION");
    // 通过invoke方法获取BROADCAST_INTENT_TRANSACTION的值
    private int broadcastIntent = invoke("TRANSACTION_broadcastIntent", "BROADCAST_INTENT_TRANSACTION");
    // 通过invoke方法获取START_INSTRUMENTATION_TRANSACTION的值
    private int startInstrumentation = invoke("TRANSACTION_startInstrumentation", "START_INSTRUMENTATION_TRANSACTION");

    /**
     * 动态获取IActivityManager接口中的事务常量值
     *
     * @param str  事务的字段名
     * @param str2 备用的事务字段名
     * @return 事务常量的整数值，如果未找到则返回-1
     */
    public int invoke(String str, String str2) {
        int result = -1; // 默认结果为-1，表示未找到
        try {
            // 尝试通过反射获取IActivityManager$Stub类
            Class<?> cls = Class.forName("android.app.IActivityManager$Stub");
            // 获取指定字段
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true); // 设置字段可访问
            result = declaredField.getInt(cls); // 获取字段的整数值
            declaredField.setAccessible(false); // 还原字段的可访问性
        } catch (Exception e) {
            try {
                // 如果第一个类未找到，尝试获取IActivityManager类
                Class<?> cls2 = Class.forName("android.app.IActivityManager");
                Field declaredField2 = cls2.getDeclaredField(str2); // 获取备用字段
                declaredField2.setAccessible(true); // 设置字段可访问
                result = declaredField2.getInt(cls2); // 获取备用字段的整数值
                declaredField2.setAccessible(false); // 还原字段的可访问性
            } catch (Exception e1) {
                // 处理第二个异常（可选）
            }
        }
        return result; // 返回获取的结果
    }

    // 返回startService的常量值
    public int startService() {
        return startService;
    }

    // 返回broadcastIntent的常量值
    public int broadcastIntent() {
        return broadcastIntent;
    }

    // 返回startInstrumentation的常量值
    public int startInstrumentation() {
        return startInstrumentation;
    }

    // 打印异常信息
    public void thrown(Throwable th) {
        th.printStackTrace(); // 输出异常堆栈信息
    }
}

