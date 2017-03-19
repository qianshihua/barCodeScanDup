package web;
import java.text.DateFormat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import eclipse.Rdrecord;
import eclipse.Rdrecords;
import eclipse.Transvouch;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import util.ContextUtil;

/**
 * Created by qiansh on 2017/3/8.
 */

public class RetrofitUtil {
    MainService service;
    Retrofit retrofit;

    private  static RetrofitUtil ru;

    private RetrofitUtil(){}

    public static RetrofitUtil getIns(){
        if(ru==null)
            ru=new RetrofitUtil();
        return ru;
    }

    public static void init(String url){
        if(url==null || url.trim().equals("")){
            return;
        }
        url = "http://"+url+"/qr/";
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
//                .addInterceptor(new LogInterceptor())//拦截器
                .cookieJar(new CookieManger(ContextUtil.getInstance()))//cookie保持
                .build();

        RetrofitUtil ins = getIns();

        GsonBuilder gb=new GsonBuilder();

        gb.registerTypeAdapter(java.util.Date.class , new  UtilDateSerializer()).setDateFormat(DateFormat.LONG);
        gb.registerTypeAdapter(java.util.Date.class , new  UtilDateDeserializer()).setDateFormat(DateFormat.LONG);
        Gson gson=gb.create();

//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ins.retrofit = new Retrofit.Builder().baseUrl(url).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();
        ins.service = ins.retrofit.create(MainService.class);

    }

    /**
     * 登录接口。失败的话返回错误信息，成功的话返回空串
     * @param username
     * @param password
     * @return
     */
    public  void login(String username, String password, final Handler handler){
        String check = check();
        if(check!=null && !check.trim().equals("")){
            handler.sendMessage(buildMsg(check));
            return;
        }

        RetrofitUtil ins = getIns();
        Call<LoginResp<Person>> call = ins.service.login(username, password);
        String rm = "";
//        try {
//            Response<LoginResp<Person>> execute = call.execute();//主线程中网络通讯，抛出异常：
//            rm = execute.body().getMsg();
//        } catch (Throwable e) {
//            rm = e.getMessage();
//        }



        final String[] finalRmt = new String[1];
        finalRmt[0]="";
        call.enqueue(new Callback<LoginResp<Person>>() {
            @Override
            public void onResponse(Call<LoginResp<Person>> call, Response<LoginResp<Person>> response) {
                LoginResp body = response.body();
                Message msg = buildMsg(body.getMsg());
                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Call<LoginResp<Person>> call, Throwable t) {
                Message msg = buildMsg(t.getMessage());
                handler.sendMessage(msg);
            }
        });

    }

    private Message buildMsg(String msgTxt) {
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString("msg", msgTxt);
        msg.setData(data);
        return msg;
    }

    private static String check() {
        if(getIns().service==null){
            return "服务器地址未配置";
//            throw new RuntimeException("服务器地址未配置");
        }
        return "";
    }

    /**
     * 根据调拨单或者出入库单编号查询对象
     * @param ccode
     * @param handler
     */
    public void queryRecordeByCcode(String ccode, final Handler handler){
        check();
        RetrofitUtil ins = getIns();
        Call<CommonRespThree<Rdrecord, Rdrecords,Transvouch>> call = ins.service.queryRecord(ccode);
        call.enqueue(new Callback<CommonRespThree<Rdrecord, Rdrecords, Transvouch>>() {
            @Override
            public void onResponse(Call<CommonRespThree<Rdrecord, Rdrecords,Transvouch>> call, Response<CommonRespThree<Rdrecord, Rdrecords,Transvouch>> response) {
                CommonRespThree<Rdrecord, Rdrecords,Transvouch> body = response.body();
                List<Rdrecord> data = body.getData();
                List<Rdrecords> dataTwo = body.getDataTwo();
                String errMsg ="";
                if(StringUtils.isNoneBlank(body.getErrMsg())){
                    errMsg=body.getErrMsg();
                }
                Message message = buildMsg(errMsg);
                message.obj=body;
                handler.sendMessage(message);

            }

            @Override
            public void onFailure(Call<CommonRespThree<Rdrecord, Rdrecords,Transvouch>> call, Throwable t) {
                t.printStackTrace();
                handler.sendMessage(buildMsg(t.getMessage()) );
            }
        });
    }


    /**
     * 验证扫描枪读取的二维码
     * @param rdIdOrCtvcode
     * @param qr
     * @param handler
     */
    public void checkQrSingle(String rdIdOrCtvcode, String qr, final Handler handler){
        check();
        RetrofitUtil ins = getIns();
        Call<CommonRespOne<Object>> call = ins.service.checkQrSingle(rdIdOrCtvcode, qr);
        call.enqueue(new Callback<CommonRespOne<Object>>() {
            @Override
            public void onResponse(Call<CommonRespOne<Object>> call, Response<CommonRespOne<Object>> response) {
                CommonRespOne<Object> body = response.body();

                String errMsg ="";
                if(StringUtils.isNoneBlank(body.getErrMsg())){
                    errMsg=body.getErrMsg();
                }
                Message message = buildMsg(errMsg);
                message.obj=body;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<CommonRespOne<Object>> call, Throwable t) {
                t.printStackTrace();
                handler.sendMessage(buildMsg(t.getMessage()) );
            }
        });


    }

}
