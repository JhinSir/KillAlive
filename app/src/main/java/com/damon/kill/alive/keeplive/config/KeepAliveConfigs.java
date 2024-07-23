package com.damon.kill.alive.keeplive.config;

import android.content.Context;
import android.content.Intent;

import com.damon.kill.alive.service.KeepAliveService;

public class KeepAliveConfigs {

    public final Config PERSISTENT_CONFIG;
    public final Config DAEMON_ASSISTANT_CONFIG;
    public boolean ignoreOptimization = false;
    public int rebootIntervalMs = 5000; // 10s
    public int rebootMaxTimes = 3;
    public boolean limitReboot = false;

    public static OnBootReceivedListener bootReceivedListener;

    public KeepAliveConfigs(Config persistentConfig, Config daemonAssistantConfig) {
        this.PERSISTENT_CONFIG = persistentConfig;
        this.DAEMON_ASSISTANT_CONFIG = daemonAssistantConfig;
    }

    public KeepAliveConfigs(Config persistentConfig) {
        this.PERSISTENT_CONFIG = persistentConfig;
        this.DAEMON_ASSISTANT_CONFIG = new Config("android.process.daemon",
                KeepAliveService.class.getCanonicalName());
    }

    public KeepAliveConfigs ignoreBatteryOptimization() {
        ignoreOptimization = true;
        return this;
    }

    public KeepAliveConfigs rebootThreshold(int rebootIntervalMs, int rebootMaxTimes) {
        limitReboot = true;
        this.rebootIntervalMs = rebootIntervalMs;
        this.rebootMaxTimes = rebootMaxTimes;
        return this;
    }

    public KeepAliveConfigs setOnBootReceivedListener(OnBootReceivedListener listener) {
        bootReceivedListener = listener;
        return this;
    }

    public static class Config {

        public final String processName;
        public final String serviceName;

        public Config(String processName, String serviceName) {
            this.processName = processName;
            this.serviceName = serviceName;
        }
    }

    public interface OnBootReceivedListener {
        void onReceive(Context context, Intent intent);
    }
}
