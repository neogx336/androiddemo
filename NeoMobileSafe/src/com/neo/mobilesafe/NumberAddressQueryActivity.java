package com.neo.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NumberAddressQueryActivity extends Activity {
	
	private EditText ed_phone;
	private TextView result;
	private Vibrator vibrator;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ed_phone=(EditText) findViewById(R.id.ed_phone);
		result=(TextView) findViewById(R.id.result);
		vibrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
		ed_phone.addTextChangedListener(new TextWatcher() {
			
			/*
			 * 当文本发生变化时调用
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s!=null&&s.length()>=3) {
					
					
				}
				
			}
			
			/*
			 * 当文本发生变之前调用
			 */
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			/*
			 * 当文本发生变之后调用
			 */
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		
	}

	
	
	
	public void  numberAddressQuery(View view){
		
		
		
		
	}
}
