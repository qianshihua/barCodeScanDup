package com.zkc.barcodescan.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.zkc.barcodescan.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import web.RetrofitUtil;

public class LoginActivity extends Activity {



    public void showToast(String content){
        Toast toast=Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 80);
        toast.show();
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);


		findViewById(R.id.loginBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


				final LoginActivity la = LoginActivity.this;
				Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						Bundle data = msg.getData();
						String errMsg = data.getString("msg");
						// TODO
						// UI界面的更新等相关操作
						if(errMsg!=null && errMsg.length()>0){
							showToast(errMsg);
							return;
						}else{
							Intent intent = new Intent();
							intent.setClass(LoginActivity.this, MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivityForResult(intent, 3);
						}
					}
				};

				String name = ((EditText) findViewById(R.id.unameText)).getText().toString();
				String pwd = ((EditText) findViewById(R.id.pwdText)).getText().toString();
				RetrofitUtil.getIns().login(name, pwd,handler);


			}
		});

		findViewById(R.id.cfgBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(LoginActivity.this, ServerCfgActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivityForResult(intent, 2);

				Intent intent = new Intent(LoginActivity.this, RdRecordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, 2);
			}
		});


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
