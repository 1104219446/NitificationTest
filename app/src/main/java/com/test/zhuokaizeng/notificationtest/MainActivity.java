package com.test.zhuokaizeng.notificationtest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnAdd;
    private Button mBtnRemove;
    private Button mBtnClear;
    private Button mBtnSp;
    public final static int MESSAGE_ADD=1;
    public final static int MESSAGE_CLEAR=2;
    public final static int MESSAGE_DELETE_BY_ID=3;
    public final static int MESSAGE_ADD_SP=4;
    public final static int MESSAGE_CLOSE=5;
    public final static String CHANNEL_ID="100";
    public static MyHandler myHandler;
    public static int mNotificationId=0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setListener();
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        myHandler=new MyHandler(this);
    }

    private void setListener() {
        mBtnClear.setOnClickListener(this);
        mBtnRemove.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
        mBtnSp.setOnClickListener(this);
    }

    private void findView() {
        mBtnAdd=findViewById(R.id.btn_add);
        mBtnRemove=findViewById(R.id.btn_remove);
        mBtnClear=findViewById(R.id.btn_clear);
        mBtnSp=findViewById(R.id.btn_add_sp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                myHandler.sendEmptyMessage(MESSAGE_ADD);
                break;
            case R.id.btn_remove:
                myHandler.sendEmptyMessage(MESSAGE_DELETE_BY_ID);
                break;
            case R.id.btn_clear:
                myHandler.sendEmptyMessage(MESSAGE_CLEAR);
                break;
            case R.id.btn_add_sp:
                myHandler.sendEmptyMessage(MESSAGE_ADD_SP);
                break;
            default:
                break;
        }
    }

    public static class MyHandler extends Handler{

        private final WeakReference<Activity>mActivity;
        private NotificationManager mNotificationManager;
        private Intent mIntent;

        @RequiresApi(api = Build.VERSION_CODES.O)
        private MyHandler(Activity activity){
            mActivity=new WeakReference<>(activity);
            init(activity);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void init(Activity activity) {
            mNotificationManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"my_channel",NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            mNotificationManager.createNotificationChannel(channel);//创建Channel
            mIntent=new Intent();
            mIntent.setClass(activity,SecondActivity.class);
        }

        @TargetApi(Build.VERSION_CODES.O)
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            Activity activity=mActivity.get();
            if(activity==null){
                throw new NullPointerException("activity 为空");
            }else if(msg.what==MESSAGE_ADD){
                Notification.Builder builder=new Notification.Builder(activity);
                PendingIntent intent=PendingIntent.getActivity(activity,0,mIntent,0);

                builder.setContentTitle("测试  "+mNotificationId) //必须提供
                        .setContentText("测试内容") //必须提供
                        .setTicker("测试通知到达")
                        .setWhen(System.currentTimeMillis())
                        .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
                        .setSmallIcon(R.mipmap.ic_launcher_round) //必须提供
                        .setOngoing(true)           //  设置常驻用户无法清除
                        .setContentIntent(intent)   //用于点击通知跳转界面
                        .setChannelId(CHANNEL_ID); //Android O以上版本必须提供

                mNotificationManager.notify(mNotificationId++, builder.build());
            }else if(msg.what==MESSAGE_CLEAR){
                mNotificationManager.cancelAll();
            }else if(msg.what==MESSAGE_DELETE_BY_ID){
                if(mNotificationId>0){
                    mNotificationManager.cancel(mNotificationId-1);
                    mNotificationId--;
                }else{
                    Toast.makeText(activity, "已经没有通知需要清除了", Toast.LENGTH_SHORT).show();
                }
            }else if(msg.what==MESSAGE_ADD_SP){

                Notification.Builder builder=new Notification.Builder(activity);
                Intent in=new Intent("close");
                in.setPackage(activity.getPackageName());
                PendingIntent intent=PendingIntent.getBroadcast(activity,0
                        ,in,PendingIntent.FLAG_UPDATE_CURRENT);

                MyNotificationView myNotificationView=new MyNotificationView(activity.getPackageName()
                        ,R.layout.notification_item
                        ,activity);
                myNotificationView.setOnClickPendingIntent(R.id.btn_item_close,intent);

                builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(false)
                        .setAutoCancel(true)
                        .setCustomContentView(myNotificationView)
                        .setChannelId(CHANNEL_ID)
                        .setWhen(SystemClock.currentThreadTimeMillis())
                        .setContentIntent(intent);
                mNotificationManager.notify(mNotificationId++, builder.build());

            }else if(msg.what==MESSAGE_CLOSE){
                Toast.makeText(activity,"关闭成功",Toast.LENGTH_SHORT).show();
            }

            super.handleMessage(msg);
        }
    }
}
