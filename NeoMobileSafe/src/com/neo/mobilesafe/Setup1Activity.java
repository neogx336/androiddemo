package com.neo.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

public class Setup1Activity  extends BaseSetupActivity{
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void showPre() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		Intent intent =new Intent(this, Setup2Activity.class);
		startActivity(intent);
		
	}
	
	
	

}
