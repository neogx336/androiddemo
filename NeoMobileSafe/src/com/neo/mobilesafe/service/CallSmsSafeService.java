package com.neo.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.neo.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallSmsSafeService extends Service {
	public static final String TAG="CallSmsSafeService";
	private InnerSmsReceiver receiver;
	private BlackNumberDao dao;
	private TelephonyManager tm;
	private MyListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		dao=new BlackNumberDao(this);
		tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener=new MyListener();
		//�����绰״̬
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		//����ע��㲥������
		receiver=new InnerSmsReceiver();
		IntentFilter filter =new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		registerReceiver(receiver, filter);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		receiver=null;
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		super.onDestroy();
	}
	
	
	private class InnerSmsReceiver  extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i(TAG, "���յ���");
			
			//���յ�֮����Ҫ����Ƿ��ں�������
			//�õ�������
			Object[] objs=(Object[]) intent.getExtras().get("pdus");
			for (Object obj:objs) {
				SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) obj);
				String senderString= smsMessage.getOriginatingAddress();
				String resultString=dao.findMode(senderString);
				if ("2".equals(resultString)||"3".equals(resultString)) {
					Log.i(TAG, "���ű�����");
					abortBroadcast();
				}
				
				//��ʾ���� ���Ź����߼�
				String bodyString=smsMessage.getMessageBody();
				if (bodyString.contains("kaobao")) {
					Log.i(TAG, "�������ű�����");
					abortBroadcast();
				}
			}
		}
	}
	
	private class MyListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "�绰��������������");
			switch (state) {
			//����ʱ�Ĵ����߼�
			case TelephonyManager.CALL_STATE_RINGING:	
				String resultString=dao.findMode(incomingNumber);
				if ("1".equals(resultString)||"3".equals(resultString)) {
					Log.i(TAG, "�������绰�Ҷϡ�������");
					endCall();
					
					Uri uri=Uri.parse("content://call_log/calls");
					getContentResolver().registerContentObserver(uri, true, new CallLogObserver(incomingNumber,new Handler()));
					
					//ʹ�����ݹ۲��ߣ��۲�仯
					deleteCallog(incomingNumber);
					
				}
				break;
			}						
			super.onCallStateChanged(state, incomingNumber);
		}		
	}
	
	
	private class CallLogObserver extends ContentObserver{
		String incomnum;
		public CallLogObserver(String inString,Handler handler) {
			super(handler);
			this.incomnum=inString;
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			Log.i(TAG, "���ݱ仯��");
			getContentResolver().unregisterContentObserver(this);
			deleteCallog(incomnum);
			super.onChange(selfChange);
		}
		
	}
	
	/*
	 * �Ҷϵ绰
	 * 
	 */
	public void endCall() {
		//��ҪȨ��:android.permission.CALL_PHONE
		//ʹ�÷���ԭ��ȥ����ϵͳ���صķ���   
		//��Ҫ��֪ʶ����֪������ȥʹ��ϵͳ��AIDL
		/**
		 *1.�ҵ���Ҫ���õĶ���
		 *2.��Դ�����ҵ��ö����Դ��
		 *3.�ҵ���Ӧ�ĵ��õĽӿ�
		 *4.�ҵ�AIDL�ļ�\
		 *5.����AIDL�ļ�������
		 *6.�������ɷ������
		 *7.����IBINDER  
		 */
		try {
			Log.i(TAG, "���ڹҶ�.......");
			Class clazz=CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method method=clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder=(IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(iBinder).endCall();
			Log.i(TAG, "�Ҷϵ绰�ɹ�.......");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	private void deleteCallog(String number) {
		// TODO Auto-generated method stub
		ContentResolver resolver =getContentResolver();
		Uri uri=Uri.parse("content://call_log/calls");
		//Uri uri=CallLog.CONTENT_URI;
		resolver.delete(uri, "number=?", new String []{number});
		Log.i(TAG, "����ɹ�");
	}
	
	
	
	
	
	

}
