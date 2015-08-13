package com.neo.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	// 定义一个手势识别器
	private GestureDetector detector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		detector =new GestureDetector(this, new SimpleOnGestureListener()
		{
			
			
			@Override
			public boolean onFling (MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY){
			
				//手指移动的逻辑
				
				//1.滑得太慢的时候
				if (Math.abs(velocityX)<200) {
					Toast.makeText(getApplicationContext(), "滑动得太慢", Toast.LENGTH_SHORT).show();
					return true;
				}
				//2.斜滑的时候
				if (Math.abs(e1.getRawY()-e2.getY())<100) {
					Toast.makeText(getApplicationContext(), "滑动得太慢", Toast.LENGTH_SHORT).show();					return true;
					
				}
				
				//3.由右向左滑   R--F
				if ((e2.getRawX()-e1.getRawX())<200) {
					Toast.makeText(getApplicationContext(), "由右向左滑   R--F", Toast.LENGTH_SHORT).show();					return true;
					
				}
				
				//4.由左向右滑  L--R
				if ((e1.getRawX()-e2.getRawX())<200) {
					Toast.makeText(getApplicationContext(), "由右向左滑   R--F", Toast.LENGTH_SHORT).show();					return true;
					
					
				}
				return super.onFling(e1, e2, velocityX, velocityY);
				
			
				
			}
			
			
		});
		
		
		
	}

	/**
	 * 按钮事件 【上一步】
	 */

	public void btn_next(View view) {
		showNext();

	}

	/**
	 * 按钮事件 【下一步】
	 */
	public void btn_pre(View view) {
		showPre();
	}

	/**
	 * 抽出来的方法用上一步
	 * 
	 */

	public abstract void showPre();

	/**
	 * 抽出来的方法用下一步
	 */
	public abstract void showNext();

}
