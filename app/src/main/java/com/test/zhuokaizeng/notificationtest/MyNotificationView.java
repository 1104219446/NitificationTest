package com.test.zhuokaizeng.notificationtest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Describe：
 * Author:zhuokai.zeng
 * CreateTime:2019/7/18
 */
public class MyNotificationView extends RemoteViews implements View.OnClickListener {
    private RelativeLayout mLayout;
    private Activity mActivity;
    private TextView mTvClose;
    private LayoutInflater mLayoutInflater;

    public MyNotificationView(String packageName, int layoutId, Activity activity) {
        super(packageName, layoutId);
        mActivity=activity;
        mLayoutInflater=activity.getLayoutInflater();
        mLayout= (RelativeLayout) mLayoutInflater.inflate(layoutId,null);
        mTvClose=mLayout.findViewById(R.id.btn_item_close);
        mTvClose.setOnClickListener(this);
    }

    //无法监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_item_close:
                Toast.makeText(mActivity,"关闭成功",Toast.LENGTH_SHORT).show();
                Log.d("abcd", "onClick: ");
                break;
        }
    }
}
