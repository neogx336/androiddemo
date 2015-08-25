package com.neo.mobilesafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AtoolsActivity  extends Activity{
	
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}

	/**
	 * 点击事件，进入号码归属地查询的页面
	 * @param view
	 */
	public void numberQuery(View view){
		Intent intent =new Intent( this,NumberAddressQueryActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 短信备份
	 * @param view
	 */
	public void smsBackup(View view){
		pd=new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在备份短信");
		pd.show();
		new Thread(){
			public void run() {
				
				
			}
			
			
		}.start();;
		
		
	}
	

	/**
	 * 短信恢复
	 * @param view
	 */
	public void smsRestore(View view){
		Intent intent =new Intent( this,NumberAddressQueryActivity.class);
		startActivity(intent);
		
	}
	

	
	
	

}
