package com.easytest.sunyingying.util;

/**
 * Created by sunyingying on 2015/8/27.
 */
public class Util {

    private static Util util = null;
    private int PID = -1;
    private String processCpuRatio = "0.00%";
    private String totalCpuRatio = "0.00%";
    private String packageName;

    public static Util getUtil(){
        if(util == null){
            util = new Util();
        }
        return util;
    }


    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public String getProcessCpuRatio() {
        return processCpuRatio;
    }

    public void setProcessCpuRatio(String processCpuRatio) {
        this.processCpuRatio = processCpuRatio;
    }

    public String getTotalCpuRatio() {
        return totalCpuRatio;
    }

    public void setTotalCpuRatio(String totalCpuRatio) {
        this.totalCpuRatio = totalCpuRatio;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
