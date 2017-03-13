package util;

import android.app.Application;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import web.RetrofitUtil;

/**
 * Created by qiansh on 2017/3/9.
 */

public class ContextUtil extends Application {
    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        String s = loadFileContent();
        if(s!=null && s.trim().length()>1){
            RetrofitUtil.getIns().init(s);
        }

    }



    public String loadFileContent()
    {
        String rm ="";
        try {
            FileInputStream inStream=this.openFileInput("serverCfg.txt");
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            byte[] buffer=new byte[1024];
            int length=-1;
            while((length=inStream.read(buffer))!=-1)   {
                stream.write(buffer,0,length);
            }

            stream.close();
            inStream.close();
            rm=stream.toString();

//			Toast.makeText(LoginActivity.this,"Loaded",Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            return rm;
        }
        return rm;

    }
}
