package com.neo.mobilesafe.ui;

import com.neo.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 我们自定义控制
 * @author biostime11
 *
 */

public class SettingClickView extends RelativeLayout {
	private TextView tv_desc;
	private TextView tv_title;
	private String desc_on;
	private String desc_off;


	public SettingClickView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	/*
	 * 带有两个参数的构造方法 ，布局文件合钐的时候调用
	 *
	 */
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
		String title=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.neo.mobilesafe", "title");
		 desc_on=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.neo.mobilesafe", "desc_on");
		 desc_off=attrs.getAttributeValue("http://schemas.android.com/apk/res/com.neo.mobilesafe", "desc_off");
		 tv_title.setText(title);
		 setDesc(desc_off);
		
	}

	public SettingClickView(Context context) {
		super(context);
		initView(context);
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 初始化布局
	 * 
	 */
	private void initView(Context context) {
		View.inflate(context, R.layout.setting_click_view, this);
		tv_desc=(TextView) this.findViewById(R.id.tv_desc);
		tv_title=(TextView) this.findViewById(R.id.tv_title);
	}
	/*
	 * 设置控件状态
	 * 
	 */
	public void setChecked(boolean checked) {
		if (checked) {
			setDesc(desc_on);
		} else {
			setDesc(desc_on);
		}
	}

	public void setDesc(String title) {
		// TODO Auto-generated method stub
		tv_desc.setText(title);
		
	}
	
	public void  setTitle(String title) {
		tv_title.setText(title);
		
	}
	
	
}
