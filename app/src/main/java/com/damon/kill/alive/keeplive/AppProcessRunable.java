package com.damon.kill.alive.keeplive;


import com.damon.kill.alive.keeplive.bean.DamonBean;
import com.damon.kill.alive.keeplive.bean.DamonParcel;
import com.damon.kill.alive.keeplive.config.HookMain;
import com.damon.kill.alive.sculder.ShellExecutor;
import com.damon.kill.alive.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppProcessRunable implements Runnable {
    private DamonBean env;
    private String[] strArr;
    private String str;

    public AppProcessRunable(DamonBean env, String[] strArr, String str) {
        this.env = env;
        this.strArr = strArr;
        this.str = str;
    }

    @Override
    public void run() {
        DamonParcel entity = new DamonParcel();
        entity.str = str;
        entity.strArr = strArr;
        entity.intent = env.intent;
        entity.intent2 = env.intent2;
        entity.intent3 = env.intent3;

        List<String> list = new ArrayList();
        list.add("export CLASSPATH=$CLASSPATH:" + env.publicSourceDir);
        if (env.nativeLibraryDir.contains("arm64")) {
            list.add("export _LD_LIBRARY_PATH=/system/lib64/:/vendor/lib64/:" + env.nativeLibraryDir);
            list.add("export LD_LIBRARY_PATH=/system/lib64/:/vendor/lib64/:" + env.nativeLibraryDir);
            list.add(String.format("%s / %s %s --application --nice-name=%s &",
                    new Object[]{new File("/system/bin/app_process").exists() ?
                            "app_process" : "app_process", HookMain.class.getName(),
                            entity.toString(), str}));
        } else {
            list.add("export _LD_LIBRARY_PATH=/system/lib/:/vendor/lib/:" + env.nativeLibraryDir);
            list.add("export LD_LIBRARY_PATH=/system/lib/:/vendor/lib/:" + env.nativeLibraryDir);
            list.add(String.format("%s / %s %s --application --nice-name=%s &",
                    new Object[]{new File("/system/bin/app_process32").exists() ?
                            "app_process32" : "app_process", HookMain.class.getName(),
                            entity.toString(), str}));
        }
        Logger.i(Logger.TAG, "cmds: " + list);
        File file = new File("/");
        String[] strArr = new String[list.size()];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = list.get(i);
        }
        ShellExecutor.execute(file, null, strArr);
    }
}

