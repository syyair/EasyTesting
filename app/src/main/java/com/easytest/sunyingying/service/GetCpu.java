package com.easytest.sunyingying.service;

import com.easytest.sunyingying.util.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

/**
 * Created by sunyingying on 2015/8/27.
 */
public class GetCpu {

    private DecimalFormat format;
    private long processCpu1 = 0;
    private long idleCpu1 = 0;
    private long totalCpu1 = 0;
    private long processCpu2 = 0;
    private long idleCpu2 = 0;
    private long totalCpu2 = 0;
    private String processCpuRatio;
    private String totalCpuRatio;
    private int pid = -1;
    private Util util = Util.getUtil();

    public GetCpu(){
        format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
    }

    public void getCpuRatioInfo(int pid){

        this.pid = pid;
        readCpuStat();

        if(dataValidity()){
            processCpuRatio = format.format(100*((double)(processCpu1 - processCpu2) / (double) (totalCpu1 - totalCpu2)));
            totalCpuRatio = format.format(Math
                    .abs(100 * ((double) ((totalCpu1 - idleCpu1) - (totalCpu2 - idleCpu2)) / (double) (totalCpu1 - totalCpu2))));
            util.setProcessCpuRatio(processCpuRatio + "%");
            util.setTotalCpuRatio(totalCpuRatio + "%");
            totalCpu2 = totalCpu1;
            processCpu2 = processCpu1;
            idleCpu2 = idleCpu1;
        }

    }


    public void readCpuStat() {
        String processPid = Integer.toString(pid);
        ///proc/prcessPid/stat文件包含了某个进程的CPU的所有活动信息
        String processCpuStatPath = "/proc/" + processPid + "/stat";
        try {
            // monitor total and idle cpu stat of certain process
            RandomAccessFile cpuInfo = new RandomAccessFile("/proc/stat", "r");
            // "\\s+" 代表空格、回车 或者多个空格
            String[] token = cpuInfo.readLine().split("\\s+");
            idleCpu1 = Long.parseLong(token[4]);
            totalCpu1 = Long.parseLong(token[1]) + Long.parseLong(token[2])
                    + Long.parseLong(token[3]) + Long.parseLong(token[4])
                    + Long.parseLong(token[6]) + Long.parseLong(token[5])
                    + Long.parseLong(token[7]);
            cpuInfo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // monitor cpu stat of certain process
            RandomAccessFile processCpuInfo = new RandomAccessFile(processCpuStatPath, "r");
            String[] tokenP = processCpuInfo.readLine().split("\\s+");
            processCpu1 = Long.parseLong(tokenP[13]) + Long.parseLong(tokenP[14])
                    + Long.parseLong(tokenP[15]) + Long.parseLong(tokenP[16]);
            processCpuInfo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean dataValidity(){
        boolean flag = true;
        if(processCpu2 == 0 || processCpu1 < processCpu2 || totalCpu1 < totalCpu2){
            flag = false;
            totalCpu2 = totalCpu1;
            processCpu2 = processCpu1;
            idleCpu2 = idleCpu1;
            util.setProcessCpuRatio("0.00%");
        }else if ((totalCpu1 - totalCpu2) < (processCpu1 - processCpu2)){
            flag = false;
            totalCpu2 = totalCpu1;
            processCpu2 = processCpu1;
            idleCpu2 = idleCpu1;
            util.setProcessCpuRatio("100.00%");
        }
        return flag;
    }
}
