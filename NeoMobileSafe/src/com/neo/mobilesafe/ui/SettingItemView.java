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
	
	private String desc_on;
	private String desc_off;
	
	

	public SettingItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub		
		initview(context);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initview(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initview(context);
		// TODO Auto-generated constructor stub
		String title =attrs.getAttributeValue("http://schemas.android.com/apk/res/com.neo.mobilesafe", "title");
		desc_on =attrs.getAttributeValue("http://schemas.android.com/apk/res/com.neo.mobilesafe", "desc_on");
		desc_off =attrs.getAttributeValue("http://schemas.android.com/apk/res/com.neo.mobilesafe", "desc_off");
		tv_title.setText(title);
		setDesc(desc_off);
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
		if (checked) {
			setDesc(desc_on);
		}
		else {
			setDesc(desc_off);
		}
		cb_status.setChecked(checked);
	}
	public void setDesc(String text){
		tv_desc.setText(text);
		
	}
	
	public void setTitle(String txtString) {
		tv_title.setText(txtString);
	}
	
	

}
