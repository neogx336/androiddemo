package com.neo.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.Space;
import android.widget.Toast;

public class GPSService extends Service {
	
	private LocationManager lm;
	private MyLocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		lm=(LocationManager) getSystemService(LOCATION_SERVICE);
		listener=new MyLocationListener();
		//注册监听位置服务
		//给们位置服务设置条件
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置参数细化：
		// criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置为最大精度
		// criteria.setAltitudeRequired(false);//不要求海拔信息
	    // criteria.setBearingRequired(false);//不要求方位信息
	    // criteria.setCostAllowed(true);//是否允许付费
	    // criteria.setPowerRequirement(Criteria.POWER_LOW);//对电量的要求		
		String proveder =lm.getBestProvider(criteria, true);
		lm.requestLocationUpdates(proveder, 0, 0, listener);
		
		
		
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		lm.removeUpdates(listener);
		listener=null;
	}
	
	class MyLocationListener  implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			String longitude="j"+location.getLongitude()+"\n";
			String latitude="j"+location.getLatitude()+"\n";
			String accuracy="j"+location.getAccuracy()+"\n";
			SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
			Editor editor =sp.edit();
			editor.putString("lastlocation", longitude + latitude + accuracy);
			editor.commit();
		}

		
		/**
		 * 当状态发生改变的时候回调 开启--关闭 ；关闭--开启
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "GPS状态更变为" +status, Toast.LENGTH_LONG).show();
			
		}

		/**
		 * 某一个位置提供者可以使用了
		 */
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "GPS定位方式" +provider+"可用", Toast.LENGTH_LONG).show();
			
			
		}

		
		/**
		 * 某一个位置提供者不可以使用了
		 */
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "GPS定位方式" +provider+"可用", Toast.LENGTH_LONG).show();
			
		}
		
		
		
	}
	
	

}
