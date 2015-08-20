package com.neo.mobilesafe.service;

import com.neo.mobilesafe.db.dao.NumberAddressQueryUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
/*
 * ������Ӧ����ɵĹ�������
 * 1.������ʱʹ���Զ���TOAST��չ�ֹ����صĵط������ҵ�ʱȡ����ʾ
 * 2.���һ�����������÷���Ŀ���״̬
 * 
 * ���õ���֪ʶ
 * 1.windownmanager 
 * 2.�Զ���TOAST  �ο�ϵͳtoast�ı�дģʽ
 * 
 */
import android.view.WindowManager;

public class AddressService extends Service {

	/**
	 * ���������
	 * 
	 */
	private WindowManager wm;
	private View view;

	/**
	 * �绰����
	 * 
	 */
	private TelephonyManager tm;
	private MyListenerPhone listenerPhone;
	private OutCallReceiver receiver;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// ����ҵ��
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listenerPhone = new MyListenerPhone();
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);

		// �ô���ȥע��㲥������
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);

		// ʵ��������
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	class MyListenerPhone extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// ������������
				String address = NumberAddressQueryUtils
						.queryNumber(incomingNumber);
				myToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// �绰�Ŀ���״̬���ҵ绰������ܾ�
				if (view != null) {
					wm.removeView(view);
				}
				break;
			default:
				break;
			}
		}

	}

	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//����������õ��Ĳ���ȥ�ĵ绰����
			String phoneString = getResultData();
			//�����ݿ�
			String address = NumberAddressQueryUtils.queryNumber(phoneString);
			
			myToast(address);
		}
	}

	public void myToast(String address) {
		
		
		

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver = null;
	}

}
