package com.neo.mobilesafe.ui;

import com.neo.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView   extends RelativeLayout{
	
	private TextView tv_title;
	private TextView tv_desc;
	private CheckBox cb_status;
	

	public SettingItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub		
		initview(context);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initview(context);
		// TODO Auto-generated constructor stub
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initview(context);
		// TODO Auto-generated constructor stub
	}

	private void initview(Context context) {
		// TODO Auto-generated method stub
		View.inflate(context, R.layout.setting_item_view, this);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_desc=(TextView) findViewById(R.id.tv_desc);
		cb_status=(CheckBox) findViewById(R.id.cb_status);		
		
	}
	
	/**
	 * 校验组合控制是否选中
	 */
	public  boolean isChecked(){
		return cb_status.isChecked();
	}
	
	public void setChecked(boolean checked){
		cb_status.setChecked(checked);
		
	}
	public void setDesc(String text){
		tv_desc.setText(text);
		
	}
	
	public void setTitle(String txtString) {
		tv_title.setText(txtString);
		
	}
	
	

}
