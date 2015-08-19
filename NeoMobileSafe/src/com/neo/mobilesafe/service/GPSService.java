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
		//ע�����λ�÷���
		//����λ�÷�����������
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// ���ò���ϸ����
		// criteria.setAccuracy(Criteria.ACCURACY_FINE);//����Ϊ��󾫶�
		// criteria.setAltitudeRequired(false);//��Ҫ�󺣰���Ϣ
	    // criteria.setBearingRequired(false);//��Ҫ��λ��Ϣ
	    // criteria.setCostAllowed(true);//�Ƿ�������
	    // criteria.setPowerRequirement(Criteria.POWER_LOW);//�Ե�����Ҫ��		
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
		 * ��״̬�����ı��ʱ��ص� ����--�ر� ���ر�--����
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "GPS״̬����Ϊ" +status, Toast.LENGTH_LONG).show();
			
		}

		/**
		 * ĳһ��λ���ṩ�߿���ʹ����
		 */
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "GPS��λ��ʽ" +provider+"����", Toast.LENGTH_LONG).show();
			
			
		}

		
		/**
		 * ĳһ��λ���ṩ�߲�����ʹ����
		 */
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "GPS��λ��ʽ" +provider+"����", Toast.LENGTH_LONG).show();
			
		}
		
		
		
	}
	
	

}
