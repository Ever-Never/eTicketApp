package com.icfcc.example.activity;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.gov.pbc.mobile.electicket.service.ElecTicketService;
import cn.gov.pbc.mobile.electicket.service.SeService;
import cn.gov.pbc.tsm.client.mobile.android.bank.service.IServiceForBank;

import com.example.androidtest2.R;
import com.icfcc.example.Util.AppContext;

public class MainActivity extends Activity {
	private static final String LOG_TAG = "test1 run";
	// /** Called when the activity is first created. */
	private static ElecTicketService seService;
	private String SERVICE_PACKAGE = "cn.gov.pbc.tsm.client.mobile.android.bank.service";
	private IServiceForBank serviceForCITIC;
	private String strLog = "";
	private String strLogFirst = "";
	EditText editText_aid;
	Button btn;
	TextView showInfo;
	boolean flag=false;
	String aid = "D156000005100101";
    //每个AID对应得密钥都不同，测试环境需要用11111111111111111111111111111111对每个实例所对应的机构号做分散。
	//D156000005100111(AID):0200085500(机构号):A852CAE519855A95434AE07C8208D9F5（密钥）：名称：黄山旅游电子票
	//D156000005100101(AID):0900010000(机构号)：936FFA04A52E4DD42E1C5C3437B78438（密钥）：名称：电子票务
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn = (Button) this.findViewById(R.id.button1);
		showInfo = (TextView) this.findViewById(R.id.showResult);
		strLog = showInfo.getText().toString() + "\n";
		editText_aid = (EditText) this.findViewById(R.id.editText_aid);
		editText_aid.setText(aid);
		bindService(new Intent(SERVICE_PACKAGE), conn, Service.BIND_AUTO_CREATE);
		
//		editText_no=(EditText) this.findViewById(R.id.editText_aid2);
//		editText_no.setText(1);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void btnOnclick(View view) {
		String strAid = editText_aid.getText().toString();
		showInfo.setText("");
		strLog = strLogFirst + "\n";
		strLogFirst = "\n*****new ElecTicketService*****";
		try {
			seService = new ElecTicketService(serviceForCITIC, aid);
		} catch (Exception e) {
			Log.e("初始化", "new ElecTicketService", e);
			strLogFirst += "\new ElecTicketService异常：" + e.toString();
			showInfo.setText(strLog);
			return;
		}
		strLogFirst += "\n*****new ElecTicketService执行完毕*****";	
		showInfo.setText(strLogFirst);
		
		
		try {
			String keyLevel3=seService.getKeyLevel3("936FFA04A52E4DD42E1C5C3437B78438");
			 flag=seService.insert(strAid,"302CE5AE81E6B3A2E6B5B7E4B88AE59BBDE99985E5BDB1E59F8E2CE7949CE89C9CE69D80E69CBA2C3230313430333133313934302C31E58FB7E58E852C35E68E923034E5BAA70000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
					keyLevel3,1);
		} catch (Exception e) {
			Log.e("写电影票失败", "btnOnclick",e);
			e.printStackTrace();
		}
		

		
		if(flag)
		{
			strLogFirst+= "\n*****write ticket success*****";
			showInfo.setText(strLogFirst);
		}
		else
		{
			strLogFirst+= "\n*****write ticket failed*****";
			showInfo.setText(strLogFirst);
		}
		
	}
	public void btnOnclick2(View view) {
		try {
		String[] s=seService.getRecord();
		strLogFirst += "\n-----------"+s[0];
		showInfo.setText(strLogFirst);
	} catch (Exception e) {
		e.printStackTrace();
	}
		
	}
	public void serviceConnected(SeService ses) {
	}

	public static void selectSE(String se) {
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(AppContext
				.getInstance());
		Class<? extends NfcAdapter> clazz = nfcAdapter.getClass();
		Method selectMethod = null;
		try {
			selectMethod = clazz.getMethod("selectDefaultSecureElement",
					String.class);
			selectMethod.invoke(nfcAdapter, se);
		} catch (Exception e) {
			Log.e("abc", "error when select se: " + se, e);
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

}
