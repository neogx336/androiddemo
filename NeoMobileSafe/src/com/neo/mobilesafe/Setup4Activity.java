package com.neo.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup4Activity  extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
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
		Intent intent =new Intent (this,Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
		
		
	}

	@Override
	public void showNext() {
		// TODO Auto-generated method stub
		
	}

}
