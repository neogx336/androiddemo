package com.neo.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {
	// ����һ������ʶ����
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
			
				//��ָ�ƶ����߼�
				
				//1.����̫����ʱ��
				if (Math.abs(velocityX)<200) {
					Toast.makeText(getApplicationContext(), "������̫��", Toast.LENGTH_SHORT).show();
					return true;
				}
				//2.б����ʱ��
				if (Math.abs(e1.getRawY()-e2.getY())<100) {
					Toast.makeText(getApplicationContext(), "������̫��", Toast.LENGTH_SHORT).show();					return true;
					
				}
				
				//3.��������   R--F
				if ((e2.getRawX()-e1.getRawX())<200) {
					Toast.makeText(getApplicationContext(), "��������   R--F", Toast.LENGTH_SHORT).show();					return true;
					
				}
				
				//4.�������һ�  L--R
				if ((e1.getRawX()-e2.getRawX())<200) {
					Toast.makeText(getApplicationContext(), "��������   R--F", Toast.LENGTH_SHORT).show();					return true;
					
					
				}
				return super.onFling(e1, e2, velocityX, velocityY);
				
			
				
			}
			
			
		});
		
		
		
	}

	/**
	 * ��ť�¼� ����һ����
	 */

	public void btn_next(View view) {
		showNext();

	}

	/**
	 * ��ť�¼� ����һ����
	 */
	public void btn_pre(View view) {
		showPre();
	}

	/**
	 * ������ķ�������һ��
	 * 
	 */

	public abstract void showPre();

	/**
	 * ������ķ�������һ��
	 */
	public abstract void showNext();

}
