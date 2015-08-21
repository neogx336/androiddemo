package com.neo.mobilesafe;


import com.neo.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NumberAddressQueryActivity extends Activity {
	
	private EditText ed_phone;
	private TextView result;
	private Vibrator vibrator;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_addres_query);
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
					String address=NumberAddressQueryUtils.queryNumber(s.toString());
					result.setText(address);		
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

	/**
	 * 查询号码归属地
	 * @param view
	 */
	
	
	public void  numberAddressQuery(View view){
		String phoneString=ed_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phoneString)) {
			Toast.makeText(this, "号码不能为空", Toast.LENGTH_LONG).show();
			
		} else {
			String addressString=NumberAddressQueryUtils.queryNumber(phoneString);
			result.setText(addressString);
			

		}
		
		
		
		
		
	}
}
