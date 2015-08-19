package com.neo.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {
	
	/*
	 * 
	 *校验某个服务务是否还活着
	 *
	 */

	public static  boolean isServiceRunning(Context context,String serviceName) {
		
		ActivityManager am =(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos=am.getRunningServices(100);
		for (RunningServiceInfo info:infos) {
			String nameString=info.service.getClassName();
			if (serviceName.equals(nameString)) {
				return true;
			}
		}
		return false;
	}
		
}
