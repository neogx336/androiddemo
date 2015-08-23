package com.neo.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.neo.mobilesafe.db.dao.BlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
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
		//监听电话状态
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		//代码注册广播接收者
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
			Log.i(TAG, "接收到了");
			
			//接收到之后需要检查是否在黑名单里
			//得到短信体
			Object[] objs=(Object[]) intent.getExtras().get("pdus");
			for (Object obj:objs) {
				SmsMessage smsMessage=SmsMessage.createFromPdu((byte[]) obj);
				String senderString= smsMessage.getOriginatingAddress();
				String resultString=dao.findMode(senderString);
				if ("2".equals(resultString)||"3".equals(resultString)) {
					Log.i(TAG, "短信被拦截");
					abortBroadcast();
				}
				
				//演示垃圾 短信过滤逻辑
				String bodyString=smsMessage.getMessageBody();
				if (bodyString.contains("kaobao")) {
					Log.i(TAG, "垃圾短信被拦截");
					abortBroadcast();
				}
			}
		}
	}
	
	private class MyListener extends PhoneStateListener{
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			//Log.i(TAG, "电话监听器。。。。");
			switch (state) {
			//响铃时的处的逻辑
			case TelephonyManager.CALL_STATE_RINGING:	
				String resultString=dao.findMode(incomingNumber);
				if ("1".equals(resultString)||"3".equals(resultString)) {
					Log.i(TAG, "黑名单电话挂断。。。。");
					endCall();
				}
				break;
			}						
			super.onCallStateChanged(state, incomingNumber);
		}		
	}
	
	
	/*
	 * 挂断电话
	 * 
	 */
	public void endCall() {
		//需要权限:android.permission.CALL_PHONE
		//使用反射原理去调用系统隐藏的方法   
		//主要的知识点是知道怎样去使用系统的AIDL
		/**
		 *1.找到需要引用的对象
		 *2.在源码里找到该对象的源码
		 *3.找到相应的调用的接口
		 *4.找到AIDL文件\
		 *5.导入AIDL文件到工程
		 *6.反射生成服务对象
		 *7.调用IBINDER  
		 */
		try {
			Log.i(TAG, "正在挂断.......");
			Class clazz=CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
			Method method=clazz.getDeclaredMethod("getService", String.class);
			IBinder iBinder=(IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony.Stub.asInterface(iBinder).endCall();
			
			Log.i(TAG, "挂断电话成功.......");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	
	

}
