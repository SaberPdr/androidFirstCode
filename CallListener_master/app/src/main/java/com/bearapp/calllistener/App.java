package com.bearapp.calllistener;

import android.app.Application;
import android.content.Intent;

/**
 * Created by Henry.Ren on 7/6/16.
 */
public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Intent intent = new Intent(this, CallListenerService.class);
        startService(intent);
    }

    public static App getInstance() {
        return instance;
    }
}
