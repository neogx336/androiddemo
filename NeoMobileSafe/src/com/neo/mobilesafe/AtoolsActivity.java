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
	 * ����¼��������������ز�ѯ��ҳ��
	 * @param view
	 */
	public void numberQuery(View view){
		Intent intent =new Intent( this,NumberAddressQueryActivity.class);
		startActivity(intent);
	}
	
	/**
	 * ���ű���
	 * @param view
	 */
	public void smsBackup(View view){
		pd=new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("���ڱ��ݶ���");
		pd.show();
		new Thread(){
			public void run() {
				
				
			}
			
			
		}.start();;
		
		
	}
	

	/**
	 * ���Żָ�
	 * @param view
	 */
	public void smsRestore(View view){
		Intent intent =new Intent( this,NumberAddressQueryActivity.class);
		startActivity(intent);
		
	}
	

	
	
	

}
