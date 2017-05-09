package com.zkc.barcodescan.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zkc.barcodescan.R;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
        content=content.replaceAll("<br>","\n");
        Toast toast=Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG);
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
                        //不管成功还是失败之后，清除输入的内容
                        final TextView tvv = (TextView) findViewById(R.id.err_tv);
                        tvv.setText("");
                        final TextView bt = (TextView) findViewById(R.id.biaoTou);
                        bt.setText("");
                        adapter.loadAll(new ArrayList<Rdrecords>());
                        // UI界面的更新等相关操作
                        if(errMsg!=null && errMsg.length()>0){
                            //提交失败之后，删除文件
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

                            if(rdIdOrCtvcode!=null && checkFile(rdIdOrCtvcode)){
                                //查询到这个订单在本地有缓存数据,需要提示用户是否加载
                                new AlertDialog.Builder(RdRecordActivity.this).setTitle("系统提示")//设置对话框标题

                                        .setMessage("检测到存在提交失败的缓存数据，是否加载！")//设置显示的内容

                                        .setPositiveButton("加载",new DialogInterface.OnClickListener() {//添加确定按钮

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                if(checkFile(rdIdOrCtvcode)){
                                                    fillData(rdIdOrCtvcode);
                                                }
                                            }

                                        }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {//响应事件

                                    }

                                }).show();//在按键响应事件中显示此对话框

                            }

//                            Intent intent = new Intent();
//                            intent.setClass(LoginActivity.this, RdRecordActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivityForResult(intent, 3);
                        }

                    }
                };

                String ccode = ((EditText) findViewById(R.id.keyword_et)).getText().toString();
                if(ccode==null || ccode.trim().equals("")){
                    showToast("订单编号不能为空");
                    return;
                }
                RetrofitUtil.getIns().queryRecordeByCcode(ccode,handler);
            }
        });


        /**
         * 点击提交按钮的事件
         */
        findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断是否已查询到订单
                if(StringUtils.isBlank(rdIdOrCtvcode)){
                    showToast("请先查询订单，再执行提交操作。");
                    return;
                }

                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        Bundle data = msg.getData();
                        String errMsg = data.getString("msg");
                        // UI界面的更新等相关操作
                        if(!"操作成功".equals(errMsg)){
                            //有错误返回信息
                            showToast(errMsg);
                            if(rdIdOrCtvcode!=null){
                                //将界面中的内容先缓存起来
                                saveContent(rdIdOrCtvcode);
                            }
                            return;
                        }else{
                            //提交成功
                            showToast(errMsg);
                            final TextView tv = (TextView) findViewById(R.id.err_tv);
                            tv.setText("");
                            ((TextView) findViewById(R.id.biaoTou)).setText("");
                            ((TextView) findViewById(R.id.keyword_et)).setText("");
                            adapter.loadAll(new ArrayList<Rdrecords>());

                            //提交成功之后，删除缓存文件
                            delFile(rdIdOrCtvcode);
                        }
                    }
                };
                final TextView tv = (TextView) findViewById(R.id.err_tv);
                RetrofitUtil.getIns().outOrIn(rdIdOrCtvcode,tv.getText().toString(),handler);
            }
        });


//        ((TextView) findViewById(R.id.biaoTou)).setText("");
//        ((TextView) findViewById(R.id.err_tv)).setText("3000581601511000013,3000581601511000015,3000581601511000017,3000581601511000023,3000581601511000056,3000581601511000078,3000581601511000079,3000581601511000080,3000581601511000081,3000581601511000082,33000581601511000012,33000581601511000013,33000581601511000013,3000581601511000013,3000581601511000013,3000581601511000013,3000581601511000013,");
    }


    /**
     * 保存内容到文件中
     * @param rdcode
     */
    public void saveContent(String rdcode)
    {
        try {
            String fileContent="";
            //先保存已输入的单品二维码的总数
            if(adapter!=null && adapter.getData()!=null){
                for (Rdrecords rds:adapter.getData()){
                    fileContent+=rds.getCinvcode()+"-"+rds.getHasInput()+";";
                }
            }
            fileContent=fileContent+"\n";
            //然后保存二维码列表
            final TextView tv = (TextView) findViewById(R.id.err_tv);
            fileContent=fileContent+tv.getText().toString();

            FileOutputStream outStream=RdRecordActivity.this.openFileOutput(rdcode+".txt", Context.MODE_WORLD_READABLE);
            outStream.write(fileContent.getBytes());
            outStream.close();
        } catch (Exception e) {
            Toast.makeText(RdRecordActivity.this,"保存临时扫描文件失败:"+e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

    }
    public void delFile(String rdcode){
        try{
            this.deleteFile(rdcode+".txt");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 判断是否存在订单编码对应的缓存文件
     * @param rdcode
     * @return
     */
    public boolean checkFile(String rdcode){
        try {
            FileInputStream inStream=this.openFileInput(rdcode+".txt");
        } catch (FileNotFoundException e) {
            return false;
        }
        return true;
    }

    public void fillData(String rdcode){
        String s = loadContent(rdcode);
        String[] split = s.split("\n");
        if(split.length!=2){
            this.showToast("缓存格式不正确，本次没有进行加载");
            return;
        }
        String[] invAndHasInputs = split[0].split(";");
        for (int i=0;i<invAndHasInputs.length;i++){
            String anvNHasInput = invAndHasInputs[i];
            if("".equals(anvNHasInput)){
                continue;
            }
            String[] invCodeInputArr = anvNHasInput.split("-");
            if(this.adapter!=null && adapter.getData()!=null){
                for(Rdrecords rds:adapter.getData()){
                    if(rds.getCinvcode().equals(invCodeInputArr[0])){
                        rds.setHasInput(Double.parseDouble(invCodeInputArr[1]));
                    }
                }
            }
        }
        this.adapter.notifyDataSetChanged();
        String qrs = split[1];
        final TextView tv = (TextView) findViewById(R.id.err_tv);
        tv.setText(qrs);
    }

    /**
     * 读取文件内容
     * @return
     */
    public String loadContent(String rdcode)
    {
        String rm ="";
        try {
            FileInputStream inStream=this.openFileInput(rdcode+".txt");
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int length=-1;
            while((length=inStream.read(buffer))!=-1)   {
                stream.write(buffer,0,length);
            }
            stream.close();
            inStream.close();
            rm=stream.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return rm;

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
     * @param qrCode 扫入的二维码，没有逗号
     */
    public void changeOne(String cinvcode, int inOrOut, String qrCode){
        if(this.adapter!=null){
            adapter.changeOne(cinvcode,inOrOut, qrCode);
        }
    }

    public void clear(){
        if(this.adapter!=null){
            adapter.loadAll(new ArrayList<Rdrecords>());

        }
    }



    private class MyAdapter extends BaseAdapter {
        private List<Rdrecords> data;

        public List<Rdrecords> getData(){
            return data;
        }


        public void loadAll(List<Rdrecords> list){
            data = list;
            this.notifyDataSetChanged();
        }


        /**
         * 扫完一个二维码，更新列表输入中已经输入的数量。
         * @param cinvcode 商品编码.删除的时候传null
         * @param inOrOut 1或者-1
         * @param qrCode 没有逗号的二维码
         */
        public void changeOne(String cinvcode, int inOrOut, String qrCode){
            if(data==null){
                return;
            }
            for (Rdrecords rds:data){
                if(inOrOut<0){
                    //删除的时候，单品编码为null
                    if(rds.getInputQrs()==null){
                        rds.setInputQrs("");
                    }
                    if(rds.getInputQrs().indexOf(qrCode)>-1){
                        final String inputQrs = rds.getInputQrs();
                        String newInputQrs = inputQrs.replaceAll(qrCode + ",", "");
                        rds.setInputQrs(newInputQrs);
//                        rds.setHasInput(rds.getHasInput()+inOrOut);
//                        this.notifyDataSetChanged();
                    }
                }else{
                    //新增的分支
                    if(StringUtils.equals(rds.getCinvcode(),cinvcode)){
                        if(inOrOut>0){
                            //新增的分支
                            if(rds.getInputQrs()==null){
                                rds.setInputQrs("");
                            }
                            rds.setInputQrs(rds.getInputQrs()+qrCode+",");
                        }
//                        rds.setHasInput(rds.getHasInput()+inOrOut);
//                        this.notifyDataSetChanged();
                    }
                }
            }
            for (Rdrecords rds:data){
                String inputQrs = rds.getInputQrs();
                int length = inputQrs.split(",").length;
                if(length<0){
                    length=0;
                }
                if(length==1 && inputQrs.split(",")[0].equals("")){
                    length=0;
                }
                rds.setHasInput((double)length);
            }
            this.notifyDataSetChanged();
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
         * 获取当前激活的activ
         * @return
         */
        private String getRunningActivityName(){
            ActivityManager activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            return runningActivity;
        }

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

            String runningActivityName = getRunningActivityName();
            if(runningActivityName.indexOf("Log")>-1){
                //轨迹查询
                return;
            }


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
                if(tv.getText().toString().indexOf(text+",")<0){
                    return;
                }else{
                    tv.setText(tv.getText().toString().replaceAll(text+",",""));
                    rdAct.changeOne(null,-1, text);
                }



            }else{
                //新增，先判断是否已经扫描，是的话直接跳过。不是的话需要调用接口进行验证，然后填充回去
                final TextView tv = (TextView) findViewById(R.id.err_tv);
                if(tv.getText().toString().indexOf(text+",")>-1){
                    return;
                }else{
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
//                                for(int i=0;i<2000;i++){
//                                    String fadeQr="9999999999999999";
//                                    String s1 = StringUtils.leftPad(i + "", 4, "0");
//                                    s1=fadeQr+s1;
//                                    tv.setText(s1+","+tv.getText());
//                                    rdAct.changeOne(respThree.getCinvcode(),1, s1);
//                                }
                                return;
                            }else{
                                //没有出错，需要更新二维码到已扫描列表，还有listview视图
                                if(tv.getText().toString().indexOf(text)>-1){

                                }else{
                                    tv.setText(text+","+tv.getText());
                                    rdAct.changeOne(respThree.getCinvcode(),1, text);
                                }
                            }
                        }
                    };
                    RetrofitUtil.getIns().checkQrSingle(rdIdOrCtvcode,text,handler);
                }

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
