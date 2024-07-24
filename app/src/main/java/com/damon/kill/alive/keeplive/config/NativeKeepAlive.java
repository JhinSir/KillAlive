package com.damon.kill.alive.keeplive.config;

public class NativeKeepAlive {

    public static native void lockFile(String lockFile);

    public static native void nativeSetSid();

    public static native void waitFileLock(String lockFile);

    public static native void doDaemon(String indicatorSelfPath,
                                       String indicatorDaemonPath,
                                       String observerSelfPath,
                                       String observerDaemonPath,
                                       String packageName,
                                       String serviceName,
                                       int sdkVersion);



    public void onDaemonDead() {
        IKeepAliveProcess.Fetcher.fetchStrategy().onDaemonDead();
    }

    static {
        try {
            System.loadLibrary("keep_alive");
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
