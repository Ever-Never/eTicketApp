package com.icfcc.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.androidtest2.R;
import com.icfcc.example.Util.Utils;

public class OpenAccActivity extends Activity implements View.OnClickListener {
	private static final int RESPONSE_CODE = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_acc);
		((EditText) findViewById(R.id.editText_aid)).setText(getIntent().getExtras().getString("AID"));
		int[] ids = { R.id.return_icfccActivity1 };
		Utils.batchSetClickListener(ids, this, this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_acc, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.return_icfccActivity1:
			Bundle returnData = new Bundle();
			returnData.putString("retMsg", getString(R.string.open_acc_return_msg));
			Intent returnIntent = new Intent();
			returnIntent.putExtras(returnData);
			setResult(RESPONSE_CODE, returnIntent);
			finish();
			break;

		default:
			break;
		}
	}
}
