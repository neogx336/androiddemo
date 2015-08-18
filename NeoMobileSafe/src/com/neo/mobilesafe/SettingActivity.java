package com.neo.mobilesafe;

import com.neo.mobilesafe.ui.SettingItemView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {
	
	SettingItemView siv_update;
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_setting);
		
		//实始化
		siv_update=(SettingItemView) findViewById(R.id.siv_update);
		siv_update.setTitle("自动升级选项");
		sp=getSharedPreferences("config", MODE_PRIVATE);
		boolean update=sp.getBoolean("update", false);
		
		if(update){
			siv_update.setChecked(true);
			siv_update.setDesc("开启");
			
		}
		else {
			siv_update.setChecked(false);
			siv_update.setDesc("关闭");
			
		}
		
		siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor=sp.edit();
				
				if (siv_update.isChecked()) {
					siv_update.setChecked(false);
					siv_update.setDesc("关闭");
					editor.putBoolean("update", false);
					
				} else {
					siv_update.setChecked(true);
					siv_update.setDesc("开启");
					editor.putBoolean("update", true);

				}
				editor.commit();
				
			}
		});
		
		
	}
	

}
