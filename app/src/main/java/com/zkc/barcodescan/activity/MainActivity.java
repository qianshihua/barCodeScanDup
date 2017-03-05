package com.zkc.barcodescan.activity;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zkc.Service.CaptureService;
import com.zkc.barcodescan.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.serialport.api.SerialPort;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private ScanBroadcastReceiver scanBroadcastReceiver;
	Button btnOpen, btnEdit;
	public static EditText et_code;
	private Button emptyBtn;

	List<Map<String, String>> listData = new ArrayList<Map<String, String>>();

	/*
	 * private byte[] choosedData = new byte[] { 0x07, (byte) 0xC6, 0x04, 0x08,
	 * 0x00, (byte) 0x9C, 0x0A, (byte) 0xFE, (byte) 0x81 };// ???????????115200
	 * private byte[] choosedData2 = new byte[] { 0x07, (byte) 0xC6, 0x04, 0x08,
	 * 0x00, (byte) 0x9C, 0x0A, (byte) 0xFE, (byte) 0x81 };// ???????????115200
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_barcode_main);

		et_code = (EditText) findViewById(R.id.et_code);

		et_code.setText("");
		// ???
		btnEdit = (Button) findViewById(R.id.btnEdit);
		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exitActivity();
			}
		});
		// ??????
		emptyBtn = (Button) findViewById(R.id.emptyBtn);
		emptyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_code.setText("");
				SerialPort.CleanBuffer();
				CaptureService.scanGpio.openScan();
			}
		});
		// ???????
		btnOpen = (Button) findViewById(R.id.btnOpen);
		btnOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SerialPort.CleanBuffer();
				CaptureService.scanGpio.openScan();
			}

		});

		Intent newIntent = new Intent(MainActivity.this, CaptureService.class);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(newIntent);

		getOverflowMenu();

		scanBroadcastReceiver = new ScanBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.zkc.scancode");
		this.registerReceiver(scanBroadcastReceiver, intentFilter);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, R.string.action_1d);
		menu.add(0, 2, 2, R.string.action_2d);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == 1) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ActivityBarcodeSetting.class);
			startActivity(intent);
		} else if (item.getItemId() == 2) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ActivityQrcodeSetting.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		System.out.println("onResume" + "open");
		Log.v("onResume", "open");
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		exitActivity();
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(scanBroadcastReceiver);
		super.onDestroy();
	}

	private void exitActivity() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.popup_title)
				.setMessage(R.string.popup_message)
				.setPositiveButton(R.string.popup_yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								CaptureService.scanGpio.closeScan(); // ?????
								CaptureService.scanGpio.closePower();

								finish();
							}
						}).setNegativeButton(R.string.popup_no, null).show();
	}

	class ScanBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String text = intent.getExtras().getString("code");
			Log.i(TAG, "MyBroadcastReceiver code:" + text);
			et_code.setText(text);
		}
	}
}
