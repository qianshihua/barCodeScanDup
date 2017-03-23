package web;

import eclipse.QrRecordLog;
import eclipse.Rdrecord;
import eclipse.Rdrecords;
import eclipse.Transvouch;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by qiansh on 2017/3/8.
 */

public interface MainService {
    @GET("/qr/all/rest/login.do")
    Call<LoginResp<Person>> login(@Query("username") String username,@Query("password") String password);

    @GET("/qr/all/rest/queryRecodeByCcode.do")
    Call<CommonRespThree<Rdrecord, Rdrecords, Transvouch>> queryRecord(@Query("ccode") String ccode);

    @GET("/qr/all/rest/checkQrSingle.do")
    Call<CommonRespOne<Object>> checkQrSingle(@Query("rdIdOrCtvcode") String rdIdOrCtvcode,@Query("qr") String qr);


    @GET("/qr/all/rest/outIn.do")
    Call<CommonRespOne<Object>> outOrIn(@Query("recordid") String recordid,@Query("qrs") String qrs);


    @GET("/qr/all/rest/queryLog.do")
    Call<CommonRespOne<QrRecordLog>> queryLog(@Query("qrcode") String qrcode);

}
