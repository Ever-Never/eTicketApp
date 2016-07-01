package com.icfcc.example.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.gov.pbc.tsm.client.mobile.android.bank.service.IServiceForBank;

import com.example.androidtest2.R;
import com.icfcc.example.Util.Utils;

public class BusProcessActivity extends Activity implements View.OnClickListener {
	private IServiceForBank serviceForCITIC;
	private EditText _etMsg;
	private EditText _etUrl;
	private EditText _etReturnMsg;
	private TextView _tvReturnData;
	private TextView _tvInputParam;
	private TextView _tvOpenSEChannel;
	private static final int RESPONSE_CODE = 2001;
	private Boolean isUnbind = false;
	private Bundle data;
	private byte[] TEST_DATA = { (byte) 0x00, (byte) 0x00, (byte) 0x00, };
	private Intent intent;
	private String SERVICE_PACKAGE = "cn.gov.pbc.apptsm.client.mobile.android.bank.service";
	private Byte[] returnCode;
	public static final byte[] APDU_RET_OPENSECHANNEL = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x90, (byte) 0x00, };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive);
		_etMsg = (EditText) findViewById(R.id.editText_msg);
		_etUrl = (EditText) findViewById(R.id.editText_url);
		_tvReturnData = (TextView) findViewById(R.id.textView_intentReturnData);
		_tvInputParam = (TextView) findViewById(R.id.textView_inputParam);
		_etReturnMsg = (EditText) findViewById(R.id.editText_returnMsg);
		_tvOpenSEChannel = (TextView) findViewById(R.id.textView_retCodeOpenSEChannel);
		if (null != getIntent().getExtras()) {
			_tvInputParam.setText(getIntent().getExtras().getString("inputParam"));
		}

		int[] ids = { R.id.invoke_icfcc_service, R.id.return_icfccActivity, R.id.invoke_openSEChannel };
		batchSetClickListener(ids, this, this);
		bindService(new Intent(SERVICE_PACKAGE), conn, Service.BIND_AUTO_CREATE);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.receive, menu);
		return true;
	}

	private static void batchSetClickListener(int[] ids, View.OnClickListener listener, Activity activity) {
		for (int i = 0; i < ids.length; i++) {
			View view = activity.findViewById(ids[i]);
			view.setOnClickListener(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.invoke_icfcc_service:
			isUnbind = true;
			try {
				_tvReturnData.setText(serviceForCITIC.closeSEChannel() == true ? "Y" : "null");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case R.id.return_icfccActivity:
			Bundle returnData = new Bundle();
			returnData.putString("returnMsg", _etReturnMsg.getText().toString());
			Intent returnIntent = new Intent();
			returnIntent.putExtras(returnData);
			setResult(RESPONSE_CODE, returnIntent);
			finish();
			break;
		case R.id.invoke_openSEChannel:
			isUnbind = true;
			try {
				
				 long startTime = Calendar.getInstance().getTimeInMillis();
				 long endTime;
				// // try {
				// // Thread.currentThread().sleep(5000);
				// // } catch (InterruptedException e) {
				// // // TODO Auto-generated catch block
				// // e.printStackTrace();
				// // }
				
//				 while (serviceForCITIC == null) {
//				 if (7000 <= Calendar.getInstance().getTimeInMillis() -
//				 startTime) {
//				 break;
//				 }
//				 }
//				
//				 System.out.println("==============================:" +
//				 (Calendar.getInstance().getTimeInMillis() - startTime));

				_tvOpenSEChannel.setText(Utils.toHexStringNoBlank(serviceForCITIC.openSEChannel(APDU_RET_OPENSECHANNEL)));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	private ServiceConnection conn = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder service) {
			serviceForCITIC = IServiceForBank.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			serviceForCITIC = null;
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isUnbind) {
			this.unbindService(conn);
		}
	}
}
