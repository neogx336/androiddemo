package com.neo.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import android.widget.TextView;

import com.neo.mobilesafe.R;
import com.neo.mobilesafe.db.dao.NumberAddressQueryUtils;

public class AddressService extends Service {
	protected static final String TAG = "AddressService";

	/**
	 * ���������
	 * 
	 */
	private WindowManager wm;
	private View view;
	private WindowManager.LayoutParams params;
	long[] mHits = new long[2];
	SharedPreferences sp;

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
			// ����������õ��Ĳ���ȥ�ĵ绰����
			String phoneString = getResultData();
			// �����ݿ�
			String address = NumberAddressQueryUtils.queryNumber(phoneString);

			myToast(address);
		}
	}

	public void myToast(String address) {
		view = View.inflate(this, R.layout.address_show, null);
		TextView textView = (TextView) view.findViewById(R.id.tv_address);

		

		/*
		 * DAY5��������¼� 1.˫���¼� OnClick 2.�ƶ�View OnTouch
		 */

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ˫�������߼�
				// ��һ�������¼ÿһ�ε����ʱ��,Ȼ�������ƶ�,Ȼ��������λ�ü�¼ϵͳʱ��
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					params.x = wm.getDefaultDisplay().getWidth() / 2
							- view.getWidth() / 2;
					params.y = wm.getDefaultDisplay().getHeight() / 2
							- view.getHeight() / 2;
					wm.updateViewLayout(view, params);
					Editor editor = sp.edit();
					editor.putInt("lastx", params.x);
					editor.putInt("lasty", params.y);
					editor.commit();
				}
			}
		});

		// дһ�������ļ�����
		view.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				// ����
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					Log.i(TAG, "��ָ�㵽�ؼ�");

					break;
				// �ƶ�
				case MotionEvent.ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dX = newX - startX;
					int dY = newY - startY;
					Log.i(TAG, "�ڿؼ����ƶ�");
					params.x += dX;
					params.y += dY;
					
					Log.i(TAG,"px:"+ params.x+"--py:"+ params.y);

					// ���Ǳ߽������ Խ���߽�ֵ��Ҫ����
					// 1.�ƶ������϶�
					if (params.y < 0) {
						params.y = 0;
					}
					// 2.�ƶ������±�
					if (params.y > (wm.getDefaultDisplay().getHeight() - view
							.getHeight())) {
						params.y = (wm.getDefaultDisplay().getHeight() - view
								.getHeight());
					}
					// 3.������߽�
					if (params.x < 0) {
						params.x = 0;
					}
					// 4.�ƶ������ұ�
					if (params.x > (wm.getDefaultDisplay().getWidth() - view
							.getWidth())) {
						params.x = (wm.getDefaultDisplay().getWidth() - view
								.getWidth());
					}
					// ������λ��
					wm.updateViewLayout(view, params);
					// �������ǿ�ʼ������һ����ʼ�㸳ֵ
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				// ̧��
				case MotionEvent.ACTION_UP:
					Log.i(TAG, "��̧������");
					Editor editor = sp.edit();
					// �������µ�λλ,�����´ν���ʹ��
					editor.putInt("lastx", params.x);
					editor.putInt("lasty", params.y);
					editor.commit();
				default:
					break;
				}
				// �¼��������ˣ������㸸�˿ؼ� ��������Ӧ�����¼�
				return false;

			}
		});
		
		// "��͸��","������","��ʿ��","������","ƻ����"
				int[] ids = { R.drawable.call_locate_white,
						R.drawable.call_locate_orange, R.drawable.call_locate_blue,
						R.drawable.call_locate_gray, R.drawable.call_locate_green };
				sp = getSharedPreferences("config", MODE_PRIVATE);
				view.setBackgroundResource(ids[sp.getInt("which", 0)]);
				textView.setText(address);// ���غ���
		// ����������ã��ο�ϵͳ��TOAST��д��
		params = new WindowManager.LayoutParams();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// �����ϽǶ���
		//
		
		params.gravity = Gravity.TOP+Gravity.LEFT;
		//��ʼ��λ��
		params.x=sp.getInt("lastx", 0);
		params.y=sp.getInt("lasty", 0);
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(view, params);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		// ȡ����������
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone = null;

		// �ô�����ȡ�㲥������
		unregisterReceiver(receiver);
		receiver = null;
	}

}
