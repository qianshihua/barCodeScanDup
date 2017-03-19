package com.zkc.barcodescan.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
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
import android.widget.ToggleButton;

import com.zkc.barcodescan.R;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import eclipse.Rdrecord;
import eclipse.Rdrecords;
import eclipse.Transvouch;
import web.CommonRespOne;
import web.CommonRespThree;
import web.RetrofitUtil;

public class RdRecordActivity extends Activity {

    private ListView mRecordListView;
    private MyAdapter adapter;
    private ScanBroadcastReceiver scanBroadcastReceiver;

    /**
     * 调拨单的ctvcode或者出入库但的rdid
     */
    private String rdIdOrCtvcode;




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
        scanBroadcastReceiver = new ScanBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.zkc.scancode");
        this.registerReceiver(scanBroadcastReceiver, intentFilter);
    }

    private void initView() {
//        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Handler handler = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        Bundle data = msg.getData();
//                        String errMsg = data.getString("msg");
//                        // UI界面的更新等相关操作
//                        if(errMsg!=null && errMsg.length()>0){
//                            showToast(errMsg);
//                            return;
//                        }else{
////                            Intent intent = new Intent();
////                            intent.setClass(LoginActivity.this, RdRecordActivity.class);
////                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                            startActivityForResult(intent, 3);
//                        }
//                    }
//                };
//                String ccode = ((EditText) findViewById(R.id.keyword_et)).getText().toString();
//                RetrofitUtil.getIns().queryRecordeByCcode(ccode,handler);
//                if(true) return;
//                if (mRecordListView.getVisibility() == View.VISIBLE) {
//                    mRecordListView.setVisibility(View.GONE);
//                } else {
//                    mRecordListView.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        /**
         * 点击查询按钮的事件
         */
        findViewById(R.id.search_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        CommonRespThree<Rdrecord, Rdrecords,Transvouch> respThree = (CommonRespThree<Rdrecord, Rdrecords, Transvouch>) msg.obj;

                        Bundle data = msg.getData();
                        String errMsg = data.getString("msg");
                        // UI界面的更新等相关操作
                        if(errMsg!=null && errMsg.length()>0){
                            showToast(errMsg);
                            return;
                        }else{
                            if(respThree!=null && respThree.getData()!=null&& respThree.getData().size()>0){
                                //出入库订单
                                Rdrecord rd = respThree.getData().get(0);

                                List<Rdrecords> rds = respThree.getDataTwo();
                                //单据类型，业务类型，单据来源，单据日期，正负单，制单人
                                String record = rd.getCvouchTypeName()+"|"+rd.getCbustype()+"|"+rd.getCsource()+"|"+rd.getDdateStr()+"|"+rd.getOrderType()+"|"+rd.getCmaker();
                                ((TextView) findViewById(R.id.biaoTou)).setText(record);
                                adapter.loadAll(respThree.getDataTwo());
                                rdIdOrCtvcode = rd.getId()+"";
                                scanBroadcastReceiver.setRdIdOrCtvcode(rdIdOrCtvcode);//将订单号存到广播对象，扫描枪扫了之后需要知道是否已经进行过查询
                            }
                            if(respThree!=null && respThree.getDataThree()!=null){
                                //调拨单的情况
                                Transvouch tv = respThree.getDataThree().get(0);
                                String record = tv.getDdateStr()+"|"+tv.getOutWhName()+"->"+tv.getInWhName()+"|"+tv.getCmaker()+"|"+tv.getCverifyperson()+"|"+tv.getCtvmemo();
                                ((TextView) findViewById(R.id.biaoTou)).setText(record);
                                adapter.loadAll(respThree.getDataTwo());
                                rdIdOrCtvcode=tv.getCtvcode();
                                scanBroadcastReceiver.setRdIdOrCtvcode(rdIdOrCtvcode);//将订单号存到广播对象，扫描枪扫了之后需要知道是否已经进行过查询
                            }

//                            Intent intent = new Intent();
//                            intent.setClass(LoginActivity.this, RdRecordActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivityForResult(intent, 3);
                        }
                    }
                };
                String ccode = ((EditText) findViewById(R.id.keyword_et)).getText().toString();
                RetrofitUtil.getIns().queryRecordeByCcode(ccode,handler);
            }
        });


//        ((TextView) findViewById(R.id.biaoTou)).setText("");
//        ((TextView) findViewById(R.id.err_tv)).setText("3000581601511000013,3000581601511000015,3000581601511000017,3000581601511000023,3000581601511000056,3000581601511000078,3000581601511000079,3000581601511000080,3000581601511000081,3000581601511000082,33000581601511000012,33000581601511000013,33000581601511000013,3000581601511000013,3000581601511000013,3000581601511000013,3000581601511000013,");
    }

    private void initList() {
        mRecordListView = (ListView) findViewById(R.id.listview);
        List<Rdrecords> data = new ArrayList<>();
        adapter = new MyAdapter(data);

        mRecordListView.setAdapter(adapter);

//        adapter.notifyDataSetChanged();
    }

    /**
     * 扫完一个二维码，更新列表输入中已经输入的数量。
     * @param cinvcode 商品编码
     * @param inOrOut 1或者-1
     */
    public void changeOne(String cinvcode,int inOrOut){
        if(this.adapter!=null){
            adapter.changeOne(cinvcode,inOrOut);
        }
    }



    private class MyAdapter extends BaseAdapter {
        private List<Rdrecords> data;


        public void loadAll(List<Rdrecords> list){
            data = list;
            this.notifyDataSetChanged();
        }

        /**
         * 扫完一个二维码，更新列表输入中已经输入的数量。
         * @param cinvcode 商品编码
         * @param inOrOut 1或者-1
         */
        public void changeOne(String cinvcode,int inOrOut){
            if(data==null){
                return;
            }
            for (Rdrecords rds:data){
                if(StringUtils.equals(rds.getCinvcode(),cinvcode)){
                    rds.setHasInput(rds.getHasInput()+inOrOut);
                    this.notifyDataSetChanged();
                }
            }
        }

        private MyAdapter(List<Rdrecords> list) {
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

            Rdrecords rd = data.get(position);
            holder.text1.setText(rd.getInvName());
            holder.text2.setText(rd.getIquantity()+"");
            holder.text3.setText(rd.getHasInput()+"");

            return convertView;
        }

        private class Holder {
            TextView text1;
            TextView text2;
            TextView text3;

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
            Intent intent = new Intent();
            intent.setClass(RdRecordActivity.this, QrLog.class);
            startActivity(intent);
        } else if (item.getItemId() == 2) {

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 接受到扫描枪扫到的数据之后的处理
     */
    class ScanBroadcastReceiver extends BroadcastReceiver {




        /**

         * 调拨单的ctvcode或者出入库但的rdid
         */
        private String rdIdOrCtvcode;

        private RdRecordActivity rdAct;

        public ScanBroadcastReceiver(RdRecordActivity rdAct) {
            this.rdAct = rdAct;
        }

        public ScanBroadcastReceiver() {
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            final String text = intent.getExtras().getString("code");
            //扫描之后，会吧内容自动写入查询的条件文本框，这里需要手工删除一下
            EditText editText = (EditText) findViewById(R.id.keyword_et);
            String s = editText.getText().toString();
            s = s.replaceAll(text, "").replaceAll("\n", "");
            editText.setText(s);
            //判断是否已查询到订单
            if(StringUtils.isBlank(rdIdOrCtvcode)){
                showToast("请先查询订单，在执行扫码操作。");
                return;
            }

            //判断下是否是删除模式，是的话，直接删除已经扫入的二维码

            ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton);
            String tbVal = tb.getText().toString();
            if(StringUtils.equals(tbVal,"删除")){
                //删除已经扫描的二维码
                TextView tv = (TextView) findViewById(R.id.err_tv);
                tv.setText(tv.getText().toString().replaceAll(text+",",""));
            }else{
                //新增，需要调用接口进行验证，然后填充回去
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        CommonRespOne<Object> respThree = (CommonRespOne<Object>) msg.obj;
                        Bundle data = msg.getData();
                        String errMsg = data.getString("msg");
                        // UI界面的更新等相关操作
                        if(errMsg!=null && errMsg.length()>0){
                            showToast(errMsg);
                            return;
                        }else{
                            //没有出错，需要更新二维码到已扫描列表，还有listview视图
                            TextView tv = (TextView) findViewById(R.id.err_tv);
                            tv.setText(text+","+tv.getText());
                        }
                    }
                };

                RetrofitUtil.getIns().checkQrSingle(rdIdOrCtvcode,text,handler);
            }



        }

        public void showToast(String content){
            Toast toast=Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 80);
            toast.show();
        }

        public String getRdIdOrCtvcode() {
            return rdIdOrCtvcode;
        }

        public void setRdIdOrCtvcode(String rdIdOrCtvcode) {
            this.rdIdOrCtvcode = rdIdOrCtvcode;
        }
    }
}
