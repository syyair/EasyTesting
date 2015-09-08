package com.easytest.sunyingying.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.easytest.sunyingying.easytesting.R;
import com.easytest.sunyingying.util.Util;

/**
 * Created by sunyingying on 2015/8/24.
 */
public class FloatingService extends Service {

    private View view;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private DisplayMetrics metrics;
    private TextView tv_memory,tv_traffic,tv_cpu,tv_battery,tv_batteryPercent;
    private BroadcastReceiver myBatteryReceiver;
    private IntentFilter batteryFilter;
    private int pid;
    private GetPID getPid = new GetPID();
    private GetCpu getCpu = new GetCpu();
    private GetMemory getMemory ;
    private Util util = Util.getUtil();
    private Handler handler = new Handler();
    private String packagename;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //packagename参数在MainActivity类中选择应用的时候被赋值到util类中的setPackageName方法中
        packagename = util.getPackageName();
        //获取当前进程的pid
        //Return the context of the single, global Application object of the current process.
        pid = getPid.getPid(getApplicationContext(), packagename);
        view = LayoutInflater.from(this).inflate(R.layout.floating,null);
        windowManager = (WindowManager)this.getSystemService(WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
            /*
			 * LayoutParams.TYPE_SYSTEM_ERROR：保证该悬浮窗所有View的最上层
			 * LayoutParams.FLAG_NOT_FOCUSABLE:该浮动窗不会获得焦点，但可以获得拖动
			 * PixelFormat.TRANSPARENT：悬浮窗透明
			 */
        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        initView();
        getBatteryStatus();
        getCpu();
        getMemory();

        //添加view显示出来
        windowManager.addView(view, layoutParams);
        //刷新界面
        handler.post(refreshRunnable);

    }

    private void initView(){
        tv_memory = (TextView)view.findViewById(R.id.tv_memory);
        tv_traffic = (TextView)view.findViewById(R.id.tv_traffic);
        tv_cpu = (TextView)view.findViewById(R.id.tv_cpu);
        tv_battery = (TextView)view.findViewById(R.id.tv_battery);
        tv_batteryPercent = (TextView)view.findViewById(R.id.tv_batterypercent);
    }

    //获取电压
    private void getBatteryStatus(){
        myBatteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //获取电池电压 voltage
                //获取当前电量 level
                //获取最大电量 sacale
                int voltage = intent.getIntExtra("voltage",-1);
                int sumVoltage = intent.getIntExtra("scale",-1);
                int level = intent.getIntExtra("level",-1);
                int percent = level * 100 / sumVoltage;
                //判断是否在充电,返回值是0没有在充电，其他值可能用不同的途径在充电
                int status = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

                if(status == 0){
                    tv_battery.setText("voltage : " + voltage + " mV");
                    tv_batteryPercent.setText("BatteryP : " + percent + "%");
                }else{
                    tv_battery.setText("正在充电");
                    tv_batteryPercent.setText("");
                }
            }
        };
        //充电状态或者电池的电量发生变化
        batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //注册一个广播，用于获取电量信息，包括一个BroadcastReceiver和一个Filter
        //电池的充电状态、电荷级别改变，不能通过组建声明接收这个广播，只有通过Context.registerReceiver()注册
        registerReceiver(myBatteryReceiver,batteryFilter);
    }

    //获取cpu数据
    private void getCpu(){
        getCpu.getCpuRatioInfo(pid);
        String processRatio = util.getProcessCpuRatio();
        tv_cpu.setText("CPU: " + processRatio);
    }

    //获取进程占用的内存
    private void getMemory(){
        //创建对象的时候直接传入context为什么就是空？
        getMemory = new GetMemory(getApplicationContext());
        int[] pidArr = new int[1];
        pidArr[0] = pid;
        getMemory.getPss(pidArr);
        String memory = util.transSize(util.getPss());
        tv_memory.setText("Memory: " + memory);
    }
    private void refreshUI(){
        getCpu();
        getMemory();
    }
    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            refreshUI();
//            Log.e("easytest", "runnable is running");
            handler.postDelayed(refreshRunnable,2000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBatteryReceiver);
    }
}
