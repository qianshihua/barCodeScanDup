package web;

import eclipse.Rdrecord;
import eclipse.Rdrecords;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by qiansh on 2017/3/8.
 */

public interface MainService {
    @GET("/qr/all/rest/login.do")
    Call<LoginResp<Person>> login(@Query("username") String username,@Query("password") String password);
    @GET("/qr/all/rest/queryRecodeByCcode.do")
    Call<CommonRespTwo<Rdrecord, Rdrecords>> queryRecord(@Query("ccode") String ccode);
}
