package com.zkc.barcodescan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
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

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import web.RetrofitUtil;

public class RdRecordActivity extends Activity {

    private ListView mRecordListView;
    private MyAdapter adapter;
    private ScanBroadcastReceiver scanBroadcastReceiver;

    public void showToast(String content){
        Toast toast=Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 80);
        toast.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rd_record);
        getOverflowMenu();
        initList();
        initView();
        scanBroadcastReceiver = new ScanBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.zkc.scancode");
        this.registerReceiver(scanBroadcastReceiver, intentFilter);
    }

    private void initView() {
        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Bundle data = msg.getData();
                        String errMsg = data.getString("msg");
                        // UI界面的更新等相关操作
                        if(errMsg!=null && errMsg.length()>0){
                            showToast(errMsg);
                            return;
                        }else{
//                            Intent intent = new Intent();
//                            intent.setClass(LoginActivity.this, RdRecordActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivityForResult(intent, 3);
                        }
                    }
                };
                String ccode = ((EditText) findViewById(R.id.keyword_et)).getText().toString();
                RetrofitUtil.getIns().queryRecordeByCcode(ccode,handler);
                if(true) return;
                if (mRecordListView.getVisibility() == View.VISIBLE) {
                    mRecordListView.setVisibility(View.GONE);
                } else {
                    mRecordListView.setVisibility(View.VISIBLE);
                }
            }
        });


        ((TextView) findViewById(R.id.biaoTou)).setText("销售出库单|普通销售|公司待修成品仓|XSCK2016010261|陈黎丹|2016-01-06");
        ((TextView) findViewById(R.id.err_tv)).setText("3000581601511000013,3000581601511000015,3000581601511000017,3000581601511000023,3000581601511000056,3000581601511000078,3000581601511000079,3000581601511000080,3000581601511000081,3000581601511000082,33000581601511000012,33000581601511000013,33000581601511000013,3000581601511000013,3000581601511000013,3000581601511000013,3000581601511000013,");
    }

    private void initList() {
        mRecordListView = (ListView) findViewById(R.id.listview);
        adapter = new MyAdapter(getData());
        mRecordListView.setAdapter(adapter);

//        adapter.notifyDataSetChanged();
    }

    private class MyAdapter extends BaseAdapter {
        private List<RdRecord> data;

        private MyAdapter(List<RdRecord> list) {
            data = list;
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
                convertView = LayoutInflater.from(RdRecordActivity.this).inflate(R.layout.item_rd_record, null);
                holder.text1 = (TextView) convertView.findViewById(R.id.text1);
                holder.text2 = (TextView) convertView.findViewById(R.id.text2);
                holder.text3 = (TextView) convertView.findViewById(R.id.text3);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            RdRecord rd = data.get(position);
            holder.text1.setText(rd.getName());
            holder.text2.setText(rd.getAmount());
            holder.text3.setText(rd.getPrice());

            return convertView;
        }

        private class Holder {
            TextView text1;
            TextView text2;
            TextView text3;

        }
    }

    private class RdRecord {
        private String name;
        private String amount;
        private String price;

        public RdRecord(String name, String amount, String price) {
            this.name = name;
            this.amount = amount;
            this.price = price;
        }

        public RdRecord() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    private List<RdRecord> getData() {
        List<RdRecord> list = new ArrayList<>();
        list.add(new RdRecord("DS-J100颈椎按摩披肩", "8", "3"));
        list.add(new RdRecord("A7L按摩椅（香槟金版）", "2", "0"));
        list.add(new RdRecord("A7L按摩椅（纪念版）", "3", "1"));
        list.add(new RdRecord("A7L按摩椅（纪念版）", "3", "1"));
        list.add(new RdRecord("A7L按摩椅（纪念版）", "3", "0"));
        list.add(new RdRecord("A7L按摩椅（纪念版）", "3", "0"));
        list.add(new RdRecord("A7L按摩椅（纪念版）", "3", "0"));
        list.add(new RdRecord("A7L按摩椅（纪念版）", "3", "0"));
        list.add(new RdRecord("A7L按摩椅（纪念版）", "3", "0"));
        list.add(new RdRecord("A7L按摩椅（纪念版）", "3", "0"));
        list.add(new RdRecord("A7L按摩椅（纪念版）", "3", "2"));
//        for (int i = 0; i < 10; i++) {
//            RdRecord rd = new RdRecord();
//            rd.setAmount("" + i);
//            rd.setName("sfj" + i);
//            rd.setPrice("" + i);
//
//            list.add(rd);
//        }

        return list;
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
            Intent intent = new Intent();
            intent.setClass(RdRecordActivity.this, QrLog.class);
            startActivity(intent);
        } else if (item.getItemId() == 2) {

        }
        return super.onOptionsItemSelected(item);
    }

    class ScanBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String text = intent.getExtras().getString("code");
            TextView tv = (TextView) findViewById(R.id.err_tv);
            tv.setText(text+","+tv.getText());
            EditText editText = (EditText) findViewById(R.id.keyword_et);
            String s = editText.getText().toString();
            s = s.replaceAll(text, "").replaceAll("\n", "");
            editText.setText(s);


        }
    }
}
