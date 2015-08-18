package com.easytest.sunyingying.easytesting;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

    ListView listView;
    ArrayList<HashMap<String,Object>> arrayList;
    public ItemsAdapter adapter;

    private String packagename;
    private String activitylaunch;

    private GetAppLaunchTime getAppLaunchTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.list_item);

        adapter = new ItemsAdapter(this);
        arrayList = listP();
        listView.setAdapter(adapter);

        getAppLaunchTime = new GetAppLaunchTime();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                packagename = arrayList.get(position).get("packagename").toString();
                activitylaunch = arrayList.get(position).get("mainactivity").toString();
                startApp();
            }
        });

    }

    public void startApp(){
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        if("com.easytest.sunyingying.easytesting".equals(packagename)){
            //如果是自己就不kill
            return;
        }else{
            am.killBackgroundProcesses(packagename);

            Intent launchIntent = new Intent();
            launchIntent.setComponent(new ComponentName(packagename,activitylaunch));
            long thisTime = getAppLaunchTime.startActivityWithTime(launchIntent);
            Toast.makeText(MainActivity.this,"启动耗时： " + thisTime +"毫秒" ,Toast.LENGTH_LONG).show();
        }
    }


    public ArrayList<HashMap<String,Object>> listP(){
        PackageManager packageManager = this.getPackageManager();
        ArrayList<HashMap<String,Object>> items = new ArrayList<HashMap<String,Object>>();
        //如果是启动activity就符合action:ACTION_MAIN和category:CATEGORY_LAUNCHER这样的条件
        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        //queryIntentActivities : Retrieve all activities that can be performed for the given intent.
        //ResolveInfo : Information that is returned from resolving an intent against an IntentFilter.
        // This partially corresponds to information collected from the AndroidManifest.xml's <intent> tags.
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        Collections.sort(resolveInfoList, new ResolveInfo.DisplayNameComparator(packageManager));
        for(ResolveInfo resolveInfo : resolveInfoList){
            //(resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0代表是用户安装的apk
            //(resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0表示是系统程序，但用户更新过，也算是用户安装的程序
            if((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0
                    && (resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0){
                HashMap<String,Object> itemMap = new HashMap<String,Object>();
                itemMap.put("uid",resolveInfo.activityInfo.applicationInfo.uid);
                itemMap.put("appimage",resolveInfo.activityInfo.applicationInfo.loadIcon(packageManager));
                itemMap.put("appname",resolveInfo.activityInfo.applicationInfo.loadLabel(packageManager));
                itemMap.put("packagename",resolveInfo.activityInfo.packageName);
                itemMap.put("mainactivity", resolveInfo.activityInfo.name);
                items.add(itemMap);
            }
        }
        return items;

    }

    /**
     * 实现复杂的adapter，因为要显示apk的图标，普通的adapter不能实现
     */
    class ItemsAdapter extends BaseAdapter {

        private LayoutInflater inflater = null;

        public ItemsAdapter(Context context){
            //Obtains the LayoutInflater from the given context.
            inflater = LayoutInflater.from(context);
        }

        public class Holder{
            ImageView appImage;
            TextView appname,packagename;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public HashMap<String,Object> getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            Holder holder;
            if(convertView == null){
                holder = new Holder();
                view = inflater.inflate(R.layout.itemunit,null);
                holder.appImage = (ImageView)view.findViewById(R.id.image);
                holder.appname = (TextView)view.findViewById(R.id.appname);
                holder.packagename = (TextView)view.findViewById(R.id.packagename);
                view.setTag(holder);

            }else{
                view = convertView;
                //在View和Object之间进行关联.
                holder = (Holder)view.getTag();

            }
            holder.appImage.setImageDrawable((Drawable)arrayList.get(position).get("appimage"));
            holder.appname.setText(arrayList.get(position).get("appname").toString());
            holder.packagename.setText(arrayList.get(position).get("packagename").toString());

            return view;
        }
    }

}
