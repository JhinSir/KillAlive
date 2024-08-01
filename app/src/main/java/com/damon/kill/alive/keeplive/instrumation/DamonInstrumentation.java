package com.damon.kill.alive.keeplive.instrumation;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;

import com.damon.kill.alive.service.DamonServices;
import com.damon.kill.alive.utils.Logger;


public class DamonInstrumentation extends Instrumentation {
    @Override
    public void callApplicationOnCreate(Application application) {
        super.callApplicationOnCreate(application);
        Logger.v(Logger.TAG, "callApplicationOnCreate--------------------");
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Logger.v(Logger.TAG, "onCreate------------------------");
        ContextCompat.startForegroundService(getTargetContext(),
                new Intent(getTargetContext(), DamonServices.class));
    }
}
