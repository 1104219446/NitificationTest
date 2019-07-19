package com.test.zhuokaizeng.notificationtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/7/18
 */
public class MusicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if (action != null && action.equals("close")) {
            if(MainActivity.myHandler!=null){
                MainActivity.myHandler.sendEmptyMessage(MainActivity.MESSAGE_CLOSE);
            }
        }
    }
}
