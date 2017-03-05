package com.zkc.barcodescan.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.zkc.barcodescan.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoginActivity extends Activity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);


		findViewById(R.id.loginBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

//				Retrofit retrofit = new Retrofit.Builder()
//
//						.baseUrl("http://localhost:4567/")
//
//						.build();

				System.out.println(loadFileContent());
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, 3);
			}
		});

		findViewById(R.id.cfgBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, ServerCfgActivity.class);
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
