package com.zkc.barcodescan.activity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.zkc.barcodescan.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import web.RetrofitUtil;

public class ServerCfgActivity extends Activity {
	private final static int SCANNIN_GREQUEST_CODE = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_cfg);
		String ipAndPort = this.loadFileContent();
		if(ipAndPort!=null && ipAndPort.length()>1){
			String[] arr = ipAndPort.split(":");
			if(arr.length==2){
				((EditText) findViewById(R.id.ipText)).setText(arr[0]);
				((EditText) findViewById(R.id.portText)).setText(arr[1]);
			}
		}
		findViewById(R.id.saveCfgBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String ip = ((EditText) findViewById(R.id.ipText)).getText().toString();
				String port = ((EditText) findViewById(R.id.portText)).getText().toString();
				save(ip.trim()+":"+port.trim());
			}
		});

		findViewById(R.id.returnBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ServerCfgActivity.this.finish();
			}
		});


	}


	public void save(String fileContent)
	{


		try {
			FileOutputStream outStream=ServerCfgActivity.this.openFileOutput("serverCfg.txt", Context.MODE_WORLD_READABLE);
			outStream.write(fileContent.getBytes());
			outStream.close();
			Toast.makeText(ServerCfgActivity.this,"配置保存成功", Toast.LENGTH_LONG).show();
			finish();
		} catch (Exception e) {
			Toast.makeText(ServerCfgActivity.this,"配置保存失败:"+e.getMessage(), Toast.LENGTH_LONG).show();
			return;
		}
		RetrofitUtil.getIns().init(fileContent);;

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
