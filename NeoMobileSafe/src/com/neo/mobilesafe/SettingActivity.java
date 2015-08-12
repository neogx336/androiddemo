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
		
		//ʵʼ��
		siv_update=(SettingItemView) findViewById(R.id.siv_update);
		siv_update.setTitle("�Զ�����ѡ��");
		sp=getSharedPreferences("config", MODE_PRIVATE);
		boolean update=sp.getBoolean("update", false);
		
		if(update){
			siv_update.setChecked(true);
			siv_update.setDesc("����");
			
		}
		else {
			siv_update.setChecked(false);
			siv_update.setDesc("�ر�");
			
		}
		
		siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor editor=sp.edit();
				
				if (siv_update.isChecked()) {
					siv_update.setChecked(false);
					siv_update.setDesc("�ر�");
					editor.putBoolean("update", false);
					
				} else {
					siv_update.setChecked(true);
					siv_update.setDesc("����");
					editor.putBoolean("update", true);

				}
				editor.commit();
				
			}
		});
		
		
	}
	

}
