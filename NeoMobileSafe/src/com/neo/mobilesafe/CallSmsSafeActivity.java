package com.neo.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.neo.mobilesafe.db.dao.BlackNumberDao;
import com.neo.mobilesafe.domain.BlackNumberInfo;
/**
 * 
 * ����������ҳ��
 * 
 * @author Neo
 * 
 *
 */

public class CallSmsSafeActivity extends Activity {
	public static final String TAG="CallSmsSafeActivity";
	private ListView lv_callsms_safe;
	private List<BlackNumberInfo> infos;
	private BlackNumberDao dao;
	private CallSmsSafeAdapter adapter;
	
	/**
	 * �Զ���Ի�����ʹ�õ�Ԫ��
	 * 
	 */
	private EditText et_blacknumber;
	private CheckBox cb_phone;
	private CheckBox cb_sms;
	private Button bt_ok;
	private Button bt_cancel;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		//��ʼ������
		initView();
	}

	private void initView() {
		//��ʼ������
		lv_callsms_safe=(ListView) findViewById(R.id.lv_callsms_safe);
		dao=new BlackNumberDao(this);
		infos=dao.findAll();
		adapter=new CallSmsSafeAdapter();
		lv_callsms_safe.setAdapter(adapter);
	}
	
	//�Զ����б�������
	private class CallSmsSafeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			//1.�Ż�����
			View view;
			ViewHolder holder;
			if (convertView==null) {
				//2.�����û�вŴ�������
				view=View.inflate(getApplicationContext(), R.layout.list_item_callsms, null);
				holder=new ViewHolder();
				holder.tv_number=(TextView) view.findViewById(R.id.tv_black_number);
				holder.tv_mode=(TextView) view.findViewById(R.id.tv_block_mode);
				holder.iv_delete=(ImageView) view.findViewById(R.id.iv_delete);
				view.setTag(holder);
				
			} else {
				//������ǵĻ���ȡ����
				Log.i(TAG,"��������ʷ��view���󣬸�����ʷ�����view����"+position);
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			
			//���ȡ����Ĵ�����,��Ҫ������ֵ
			holder.tv_number.setText(infos.get(position).getNumber());
			String modeString=infos.get(position).getMode();
			if ("1".equals(modeString)) {
				holder.tv_mode.setText("�绰����");
				
				
			} else if ("2".equals(modeString)) {
				holder.tv_mode.setText("�绰����");
			} 
			else{
				holder.tv_mode.setText("ȫ������");
			}
			
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder builder=new Builder(CallSmsSafeActivity.this);
					builder.setTitle("����");
					builder.setMessage("ȷ��Ҫɾ����¼?");
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dao.delete(infos.get(position).getNumber());
							infos.remove(position);
							adapter.notifyDataSetChanged();
						}
					});
					builder.setNegativeButton("ȡ��", null);
					builder.show();
				}
			});
			return view;
		}
	}

	//Holder��
	
	static class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
	
	//�������Ӻ���Ե��Զ���Ի���
	public void addBlackNumber(View view) {
		AlertDialog.Builder builder=new Builder(this);
		final AlertDialog dialog =builder.create();
		View contentView=View.inflate(this, R.layout.dialog_add_blacknumber, null);
		et_blacknumber=(EditText) contentView.findViewById(R.id.et_blacknumber);
		cb_phone=(CheckBox) contentView.findViewById(R.id.cb_phone);
		cb_sms=(CheckBox) contentView.findViewById(R.id.cb_sms);
		bt_cancel=(Button) contentView.findViewById(R.id.cancel);
		bt_ok=(Button) contentView.findViewById(R.id.ok);
		dialog.setView(contentView,0,0,0,0);
		dialog.show();
		
		bt_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		

	
		
		
	
		bt_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String blacknumber=et_blacknumber.getText().toString().trim();
				if (TextUtils.isEmpty(blacknumber)) {
					Toast.makeText(getApplicationContext(), "���������벻�ܱ��",  Toast.LENGTH_LONG).show();
					return;
				}
				String mode;
				if (cb_phone.isChecked()&&cb_sms.isChecked()) {
					//ȫ������
					mode="3";
				}
				else if (cb_phone.isChecked()) {
					//�绰����
					mode="1";
				}
				else if (cb_sms.isChecked()) {
					//��������
					mode="2";
				}
				else {
					Toast.makeText(getApplicationContext(), "ѡ������ģʽ", Toast.LENGTH_LONG).show();
					return;
				}
				dao.add(blacknumber, mode);
				BlackNumberInfo info =new BlackNumberInfo();
				info.setMode(mode);
				info.setNumber(blacknumber);
				infos.add(0,info);
				//֪ͨListview �������������ݸ���
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		
		
		
		
	}
}