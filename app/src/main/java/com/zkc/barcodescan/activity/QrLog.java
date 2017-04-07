package com.zkc.barcodescan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zkc.barcodescan.R;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import eclipse.QrRecordLog;
import eclipse.Rdrecords;
import web.CommonRespOne;
import web.RetrofitUtil;

public class QrLog extends Activity {

    private ListView mRecordListView;
    private MyAdapter adapter;


    public void showToast(String content){
        Toast toast=Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 80);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_log);
        getOverflowMenu();
        initList();
        initView();
    }

    private void initView() {
        /**
         * 点击搜索按钮的事件
         */
        findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        CommonRespOne<QrRecordLog> qrl = (CommonRespOne<QrRecordLog>) msg.obj;
                        Bundle data = msg.getData();
                        String errMsg = data.getString("msg");
                        // UI界面的更新等相关操作
                        if(errMsg==null || "".equals(errMsg)){
                            //有数据返回
                            adapter.loadAll(qrl.getData());
                        }else{
                            //有错误返回信息
                            showToast(errMsg);
                        }
                    }
                };
//                EditText editText = (EditText) findViewById(R.id.keyword_log);
//                String s = editText.getText().toString().replaceAll("\n", "");
//                editText.setText(s);
                String qrcode = ((EditText) findViewById(R.id.keyword_log)).getText().toString();
                qrcode = qrcode.replaceAll("\n","");
                RetrofitUtil.getIns().queryLog(qrcode,handler);
            }
        });

        EditText et = (EditText) findViewById(R.id.keyword_log);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString();
                if(ss.indexOf("\n")>-1){
                    EditText ett = (EditText) findViewById(R.id.keyword_log);
                    ss = ss.replaceAll("\n","");
                    if(ss.length()>20){
                        ss = ss.substring(0,20);
                    }
                    ett.setText(ss);
                }
            }
        });

    }

    private void initList() {
        mRecordListView = (ListView) findViewById(R.id.listview);
        adapter = new MyAdapter(new ArrayList<QrRecordLog>());
        mRecordListView.setAdapter(adapter);

//        adapter.notifyDataSetChanged();
    }

    private class MyAdapter extends BaseAdapter {
        private List<QrRecordLog> data;

        private MyAdapter(List<QrRecordLog> list) {
            data = list;
        }

        public void loadAll(List<QrRecordLog> list){
            if(list==null){
                list = new ArrayList<>();
            }
            data = list;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(QrLog.this).inflate(R.layout.item_qr_log, null);
                holder.text1 = (TextView) convertView.findViewById(R.id.text1);
                holder.text2 = (TextView) convertView.findViewById(R.id.text2);
                holder.text3 = (TextView) convertView.findViewById(R.id.text3);
                holder.text4 = (TextView) convertView.findViewById(R.id.text4);
                holder.text5 = (TextView) convertView.findViewById(R.id.text5);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            QrRecordLog rd = data.get(position);
            holder.text1.setText(rd.getOptTimeStr());
            holder.text2.setText(rd.getStr4());
            holder.text3.setText(rd.getString6());
            holder.text4.setText(rd.getCusNameLog());
            holder.text5.setText(rd.getZhaiYao());

            return convertView;
        }

        private class Holder {
            TextView text1;
            TextView text2;
            TextView text3;
            TextView text4;
            TextView text5;

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, 1, 1, "二维码轨迹查询");
        menu.add(0, 2, 2, "扫码操作");
        return super.onCreateOptionsMenu(menu);

    }
    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == 1) {

        } else if (item.getItemId() == 2) {
            this.finish();
//            Intent intent = new Intent();
//            intent.setClass(QrLog.this, RdRecordActivity.class);
//            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
