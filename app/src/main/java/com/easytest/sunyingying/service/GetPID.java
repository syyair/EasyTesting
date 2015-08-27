package com.easytest.sunyingying.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;

import com.easytest.sunyingying.util.Util;

import java.util.List;

/**
 * Created by sunyingying on 2015/8/27.
 */
public class GetPID {

    public ActivityManager activityManager;
    private Util util = Util.getUtil();

    public int getPid(Context context,String process){
        activityManager = (ActivityManager)context.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList){
            int pid = appProcessInfo.pid;
            String processName = appProcessInfo.processName;
            if(processName.equals(process)){
                util.setPID(pid);
                return pid;
            }
        }
        util.setPID(-1);
        return -1;
    }
}
