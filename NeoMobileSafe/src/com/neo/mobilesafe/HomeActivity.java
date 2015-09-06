package com.neo.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	
	protected static final String TAG = "HomeActivity";
	//GV�ؼ�
	private GridView list_home;
	private MyAdpter adpter ;
	public SharedPreferences sp;
	
	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	private Button ok;
	private Button cancel;
	private AlertDialog dialog;
	
	
	//����ģ������
	private static String[] name={
		"�ֻ�����","ͨѶ��ʿ","�������",
		"���̹���","����ͳ��","�ֻ�ɱ��",
		"��������","�߼�����","��������"		
	};
	//����ģ��ͼƬ
	private static int ids[]={
		R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
		R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
		R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
	};
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp=getSharedPreferences("config", MODE_PRIVATE);
		list_home=(GridView) findViewById(R.id.list_home);
		adpter =new MyAdpter();		
		list_home.setAdapter(adpter);
		//����¼�
		list_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {				
				Toast.makeText(getApplicationContext(), "����˵�"+position+"��", Toast.LENGTH_SHORT).show();		
				switch (position) {
				
				case 0:					
					showLostFindDialog();
					break;
				case 1:
					//�ֻ�ͨ
					Intent intent1=new Intent(HomeActivity.this, CallSmsSafeActivity.class);
					startActivity(intent1);
					break;
				case 2:
					//�������
					Intent intent2=new Intent(HomeActivity.this,AppManagerActivity.class);
					startActivity(intent2);
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					Intent intents = new Intent(HomeActivity.this,AtoolsActivity.class);
					startActivity(intents);
					break;
				case 8:
					Intent intent =new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);										
					break;

				default:
					break;
				}
				
			}
			
		});
		
		
		
	}
	
	protected void showLostFindDialog() {
		// TODO Auto-generated method stub
		
		if (isSetupPwd()) {
			showEnterDialog();
		}
		else {
			showSetupPwdDialog();
		}
		
	}

	private void showEnterDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder =new Builder(HomeActivity.this);
		View  view=View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);
		
		et_setup_pwd= (EditText) view.findViewById(R.id.et_setup_pwd);
		//et_setup_confirm=(EditText) view.findViewById(R.id.et_setup_confirm);
		ok=(Button) view.findViewById(R.id.ok);		
		cancel=(Button) view.findViewById(R.id.cancel);		
		cancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();				
			}
		});		
		ok.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
			String password=et_setup_pwd.getText().toString().trim();
			String savePassword=sp.getString("password", "");
			if (TextUtils.isEmpty(password)) {
				Toast.makeText(HomeActivity.this, "���벻��Ϊ��", 1).show();
				return;
			}
			if (password.equals(savePassword)) {
				Toast.makeText(HomeActivity.this, "������ȷ", 1).show();
				dialog.dismiss();
				Intent intent =new Intent(HomeActivity.this,Setup1Activity.class);
				startActivity(intent);
				
				
				return;								
			}
			else {
				Toast.makeText(HomeActivity.this, "���벻��ȷ������������", 1).show();
				et_setup_pwd.setText("");
				return;
			}			
		}
		});
		
		dialog=builder.create(); 
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		
	
		
	}

	private boolean isSetupPwd() {		
		String resultString=sp.getString("password", "");
		//Toast.makeText(getApplicationContext(), resultString, Toast.LENGTH_LONG).show();
		return !TextUtils.isEmpty(resultString);			
	}

	
	private void showSetupPwdDialog() {
		AlertDialog.Builder builder =new Builder(HomeActivity.this);
		View  view=View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);
		
		et_setup_pwd= (EditText) view.findViewById(R.id.et_setup_pwd);
		et_setup_confirm=(EditText) view.findViewById(R.id.et_setup_confirm);
		ok=(Button) view.findViewById(R.id.ok);		
		cancel=(Button) view.findViewById(R.id.cancel);		
		cancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();				
			}
		});		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password =et_setup_pwd.getText().toString().trim();
				String password_confirm =et_setup_confirm.getText().toString().trim();
				if (TextUtils.isEmpty(password)||TextUtils.isEmpty(password_confirm)) {
					Toast.makeText(HomeActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
					return;
					} 
				//����һ���򱣴�
				if (password.equals(password_confirm)) {
					Editor editor=sp.edit();
					editor.putString("password", password);
					editor.commit();
					dialog.dismiss();
					Toast.makeText(HomeActivity.this, "���뱣��ɹ���", 0).show();		
				}
				//���벻һ������������
				else {
					Toast.makeText(HomeActivity.this, "�������벻һ�£����ٴ����룡", 0).show();
					return;
				}				
			}
		});
		
		
		dialog=builder.create(); 
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		
	}
	
	private class MyAdpter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return name.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view=View.inflate(HomeActivity.this, R.layout.list_item_home, null);
			ImageView iv_item=(ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item=(TextView) view.findViewById(R.id.tv_item);
			tv_item.setText(name[position]);
			iv_item.setImageResource(ids[position]);			
			return view;
		}
		
	}
	

}
