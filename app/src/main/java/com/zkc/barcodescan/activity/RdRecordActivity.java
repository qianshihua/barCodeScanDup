package com.zkc.barcodescan.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zkc.barcodescan.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RdRecordActivity extends Activity {

    private ListView mRecordListView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rd_record);

        initList();
        initView();
    }

    private void initView() {
        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecordListView.getVisibility() == View.VISIBLE) {
                    mRecordListView.setVisibility(View.GONE);
                } else {
                    mRecordListView.setVisibility(View.VISIBLE);
                }
            }
        });


        ((TextView)findViewById(R.id.err_tv)).setText("fjwoejfowjfwiofewjofjowfjwojfiowjfowjfowjfowi\nfjwoejfowf\nfwjeof\n");
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
        for (int i = 0; i < 10; i++) {
            RdRecord rd = new RdRecord();
            rd.setAmount("" + i);
            rd.setName("sfj" + i);
            rd.setPrice("" + i);

            list.add(rd);
        }

        return list;
    }
}
