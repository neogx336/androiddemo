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
 * 本服务应该完成的功能描述
 * 1.当来电时使用自定义TOAST来展现归属地的地方，当挂电时取消显示
 * 2.抽出一个开关了配置服务的开关状态
 * 
 * 运用到的知识
 * 1.windownmanager 
 * 2.自定义TOAST  参考系统toast的编写模式
 * 
 */
import android.view.WindowManager;
import android.widget.TextView;

import com.neo.mobilesafe.R;
import com.neo.mobilesafe.db.dao.NumberAddressQueryUtils;

public class AddressService extends Service {
	protected static final String TAG = "AddressService";

	/**
	 * 窗体管理者
	 * 
	 */
	private WindowManager wm;
	private View view;
	private WindowManager.LayoutParams params;
	long[] mHits = new long[2];
	SharedPreferences sp;

	/**
	 * 电话服务
	 * 
	 */
	private TelephonyManager tm;
	private MyListenerPhone listenerPhone;
	private OutCallReceiver receiver;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 监听业电
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listenerPhone = new MyListenerPhone();
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_CALL_STATE);

		// 用代码去注册广播接收者
		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		registerReceiver(receiver, filter);

		// 实例化窗体
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
			case TelephonyManager.CALL_STATE_RINGING:// 来电铃声响起
				String address = NumberAddressQueryUtils
						.queryNumber(incomingNumber);
				myToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// 电话的空闲状态：挂电话，来电拒绝
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
			// 这就是我们拿到的播出去的电话号码
			String phoneString = getResultData();
			// 查数据库
			String address = NumberAddressQueryUtils.queryNumber(phoneString);

			myToast(address);
		}
	}

	public void myToast(String address) {
		view = View.inflate(this, R.layout.address_show, null);
		TextView textView = (TextView) view.findViewById(R.id.tv_address);

		

		/*
		 * DAY5添加两个事件 1.双击事件 OnClick 2.移动View OnTouch
		 */

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 双击处理逻辑
				// 用一个数组记录每一次点击的时间,然后向左移动,然后在最后的位置记录系统时间
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

		// 写一个触摸的监听器
		view.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				// 点下
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					Log.i(TAG, "手指点到控件");

					break;
				// 移动
				case MotionEvent.ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					int dX = newX - startX;
					int dY = newY - startY;
					Log.i(TAG, "在控件上移动");
					params.x += dX;
					params.y += dY;
					
					Log.i(TAG,"px:"+ params.x+"--py:"+ params.y);

					// 考虑边界的问题 越出边界值需要限制
					// 1.移动到最上端
					if (params.y < 0) {
						params.y = 0;
					}
					// 2.移动到最下边
					if (params.y > (wm.getDefaultDisplay().getHeight() - view
							.getHeight())) {
						params.y = (wm.getDefaultDisplay().getHeight() - view
								.getHeight());
					}
					// 3.超出左边界
					if (params.x < 0) {
						params.x = 0;
					}
					// 4.移动到最右边
					if (params.x > (wm.getDefaultDisplay().getWidth() - view
							.getWidth())) {
						params.x = (wm.getDefaultDisplay().getWidth() - view
								.getWidth());
					}
					// 最后更新位置
					wm.updateViewLayout(view, params);
					// 结束就是开始，给下一个开始点赋值
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				// 抬高
				case MotionEvent.ACTION_UP:
					Log.i(TAG, "手抬起来了");
					Editor editor = sp.edit();
					// 存下最新的位位,便于下次进来使用
					editor.putInt("lastx", params.x);
					editor.putInt("lasty", params.y);
					editor.commit();
				default:
					break;
				}
				// 事件处理完了，不让你父人控件 父布局响应触摸事件
				return false;

			}
		});
		
		// "半透明","活力橙","卫士蓝","金属灰","苹果绿"
				int[] ids = { R.drawable.call_locate_white,
						R.drawable.call_locate_orange, R.drawable.call_locate_blue,
						R.drawable.call_locate_gray, R.drawable.call_locate_green };
				sp = getSharedPreferences("config", MODE_PRIVATE);
				view.setBackgroundResource(ids[sp.getInt("which", 0)]);
				textView.setText(address);// 加载号码
		// 窗体参数设置，参考系统的TOAST的写法
		params = new WindowManager.LayoutParams();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		// 事左上角对齐
		//
		
		params.gravity = Gravity.TOP+Gravity.LEFT;
		//初始化位置
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

		// 取消监听来电
		tm.listen(listenerPhone, PhoneStateListener.LISTEN_NONE);
		listenerPhone = null;

		// 用代码消取广播接收者
		unregisterReceiver(receiver);
		receiver = null;
	}

}
