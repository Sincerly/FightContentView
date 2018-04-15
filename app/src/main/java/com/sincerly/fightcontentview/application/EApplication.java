package com.sincerly.fightcontentview.application;

import android.app.Application;

import com.sincerly.fightcontentview.config.AppConfig;

import java.io.File;

/**
 * Created by Administrator on 2018/4/15 0015.
 */

public class EApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initDir();
    }

    private void initDir() {
        File tmpDir = new File(AppConfig.PATH);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
    }
}
