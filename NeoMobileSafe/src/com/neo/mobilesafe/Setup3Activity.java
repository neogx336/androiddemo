package com.neo.mobilesafe;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {
	
	/**
	 * day3未完成功能
	 * 1.短信拒绝实现
	 * 2.视频15 一KEY锁
	 * 
	 * 要按视频的顺序写代码，不然会乱
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	 private EditText et_setup3_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_setup3_phone=(EditText) findViewById(R.id.et_setup3_phone);
	}

	@Override
	public void btn_next(View view) {
		// TODO Auto-generated method stub
		super.btn_next(view);
	}

	@Override
	public void btn_pre(View view) {
		// TODO Auto-generated method stub
		super.btn_pre(view);
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
		
		
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		Intent intent =new Intent (this,Setup4Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
		
	}
	
	public void selectContact( View view) {
		Toast.makeText(getApplicationContext(), "333", 1).show();	
		Intent intent =new Intent(this,SelectContactActivity.class);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data==null) {
			return;
		}
		String phoneString=data.getStringExtra("phone").replace("-", "");
		et_setup3_phone.setText(phoneString);
		
	}
	
	
	
	
	

}
