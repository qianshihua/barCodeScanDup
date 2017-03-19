package com.zkc.barcodescan.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by qiansh on 2017/3/19.
 */
public class LoadingActivity extends Activity {
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //设置一个progressdialog的弹窗
        dialog = ProgressDialog.show(this, "加載中...", "正在搜尋。。。。，請稍後！");
        Thread thread = new Thread(new Runnable() {
            public void run() {
                //do...

                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }
        });
        thread.start();

    }

    //处理跳转到主Activity
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Intent mIntent = new Intent();
            mIntent.setClass(LoadingActivity.this, ServerCfgActivity.class);
            startActivity(mIntent);
            LoadingActivity.this.finish();
            Log.d(">>>>>Mhandler", "LoadActivity关闭");
            if (msg.what == 0) {
                dialog.dismiss();
            }
        }
    };
}
