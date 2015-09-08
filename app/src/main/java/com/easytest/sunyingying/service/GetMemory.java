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
    public ActivityManager activityManager;;
    public ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
    public Util util = Util.getUtil();

    public GetMemory(Context context){
        activityManager = (ActivityManager)context.getSystemService(Service.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
    }
    public void getPss(int[] pid){
        Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(pid);
        // Return total PSS memory usage in kB
        // VSS - Virtual Set Size 虚拟耗用内存（包含共享库占用的内存）
        // RSS - Resident Set Size 实际使用物理内存（包含共享库占用的内存）
        // PSS - Proportional Set Size 实际使用的物理内存（比例分配共享库占用的内存）
        // USS - Unique Set Size 进程独自占用的物理内存（不包含共享库占用的内存）
        int pss = memoryInfo[0].getTotalPss();
        util.setPss(pss);
    }

}
