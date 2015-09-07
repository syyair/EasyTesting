package com.easytest.sunyingying.service;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.Service;
import android.content.Context;
import android.os.Debug;

import com.easytest.sunyingying.util.Util;

/**
 * Created by sunyingying on 2015/9/1.
 */
public class GetMemory {
    public ActivityManager activityManager;
    public ActivityManager.MemoryInfo memoryInfo;
    public Util util = Util.getUtil();

    public GetMemory(Context context){
        activityManager = (ActivityManager)context.getSystemService(Service.ACTIVITY_SERVICE);
        memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
    }
    public void getPss(int[] pid){
        Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(pid);
        int pss = memoryInfo[0].getTotalPss();
        util.setPss(pss);
    }

}
