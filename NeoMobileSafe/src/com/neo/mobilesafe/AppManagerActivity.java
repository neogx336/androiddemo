package com.neo.mobilesafe;

import java.util.List;

import com.neo.mobilesafe.domain.AppInfo;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class AppManagerActivity {
	private static final String TAG="AppManagerActivity";
	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	
	private ListView lv_app_manager;
	private LinearLayout ll_loading;
	
	
	private List<AppInfo> appInfos;
	
	

}
