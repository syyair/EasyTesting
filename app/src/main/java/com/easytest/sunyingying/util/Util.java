package com.easytest.sunyingying.util;

import java.text.NumberFormat;

/**
 * Created by sunyingying on 2015/8/27.
 */
public class Util {

    private static Util util = null;
    private int PID = -1;
    private String processCpuRatio = "0.00%";
    private String totalCpuRatio = "0.00%";
    private String packageName;
    private long pss;
    private int UID = -1;
    private long lastFluent;

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

    public long getPss() {
        return pss;
    }

    public void setPss(long pss) {
        this.pss = pss;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String transSize(long originalSize){
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        if(originalSize / 1024 == 0){
            return originalSize + "KB";
        }else if (originalSize / 1048576 == 0){
            return numberFormat.format((float) originalSize / 1024) + "MB";
        }else{
            return numberFormat.format((float) originalSize / 1048576) + "GB";
        }
    }

    public String transSizeFluent(long originalSize){
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        if(originalSize / 1024 == 0){
            return originalSize + "bytes/s";
        }
        else if (originalSize / 1048576 == 0){
            return numberFormat.format((float) originalSize / 1024) + "kb/s";
        }
        else{
            return numberFormat.format((float) originalSize / 1048576) + "mb/s";
        }
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public long getLastFluent() {
        return lastFluent;
    }

    public void setLastFluent(long lastFluent) {
        this.lastFluent = lastFluent;
    }
}
