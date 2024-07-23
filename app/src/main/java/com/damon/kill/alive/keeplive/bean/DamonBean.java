package com.damon.kill.alive.keeplive.bean;

import android.content.Intent;

public class DamonBean {
    public String processName;
    public String publicSourceDir;

    public String nativeLibraryDir;
    public Intent intent;
    public Intent intent2;
    public Intent intent3;

    @Override
    public String toString() {
        return "DamonBean{" +
                "processName='" + processName + '\'' +
                ", publicSourceDir='" + publicSourceDir + '\'' +
                ", nativeLibraryDir='" + nativeLibraryDir + '\'' +
                ", intent=" + intent +
                ", intent2=" + intent2 +
                ", intent3=" + intent3 +
                '}';
    }
}
