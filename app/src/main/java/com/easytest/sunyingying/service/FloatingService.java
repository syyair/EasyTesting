package com.easytest.sunyingying.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.easytest.sunyingying.easytesting.R;

/**
 * Created by sunyingying on 2015/8/24.
 */
public class FloatingService extends Service {

    private View view;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private DisplayMetrics metrics;
    private TextView tv_memory,tv_traffic,tv_cpu,tv_battery;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
        //添加view显示出来
        windowManager.addView(view, layoutParams);

    }

    private void initView(){
        tv_memory = (TextView)view.findViewById(R.id.tv_memory);
        tv_traffic = (TextView)view.findViewById(R.id.tv_traffic);
        tv_cpu = (TextView)view.findViewById(R.id.tv_cpu);
        tv_battery = (TextView)view.findViewById(R.id.tv_battery);

    }
}
