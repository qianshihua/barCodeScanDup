package com.zkc.barcodescan.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zkc.barcodescan.R;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QrLog extends Activity {

    private ListView mRecordListView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_log);
        getOverflowMenu();
        initList();
        initView();
    }

    private void initView() {
//        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mRecordListView.getVisibility() == View.VISIBLE) {
//                    mRecordListView.setVisibility(View.GONE);
//                } else {
//                    mRecordListView.setVisibility(View.VISIBLE);
//                }
//            }
//        });



    }

    private void initList() {
        mRecordListView = (ListView) findViewById(R.id.listview);
        adapter = new MyAdapter(getData());
        mRecordListView.setAdapter(adapter);

//        adapter.notifyDataSetChanged();
    }

    private class MyAdapter extends BaseAdapter {
        private List<LogItem> data;

        private MyAdapter(List<LogItem> list) {
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

            LogItem rd = data.get(position);
            holder.text1.setText(rd.getName());
            holder.text2.setText(rd.getAmount());
            holder.text3.setText(rd.getPrice());
            holder.text4.setText(rd.getS4());
            holder.text5.setText(rd.getS5());

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

    private class LogItem {
        private String name;
        private String amount;
        private String price;
        private String s4;

        public LogItem(String name, String amount, String price, String s4, String s5) {
            this.name = name;
            this.amount = amount;
            this.price = price;
            this.s4 = s4;
            this.s5 = s5;
        }

        public String getS4() {
            return s4;
        }

        public void setS4(String s4) {
            this.s4 = s4;
        }

        public String getS5() {
            return s5;
        }

        public void setS5(String s5) {
            this.s5 = s5;
        }

        public LogItem() {
        }

        private String s5;

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

    private List<LogItem> getData() {
        List<LogItem> list = new ArrayList<>();
        list.add(new LogItem("2015-11-30 16:27:41", "", "","","根据单品创建二维码"));
        list.add(new LogItem("2015-11-30 16:27:41", "成品库", "CPRK2015120029","","入库"));
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

        } else if (item.getItemId() == 2) {
            Intent intent = new Intent();
            intent.setClass(QrLog.this, RdRecordActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
