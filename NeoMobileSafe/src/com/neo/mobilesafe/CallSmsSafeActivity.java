package com.neo.mobilesafe;

import java.util.List;

import com.neo.mobilesafe.db.dao.BlackNumberDao;
import com.neo.mobilesafe.domain.BlackNumberInfo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 
 * ���������ҳ��
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
		public View getView(int position, View convertView, ViewGroup parent) {
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

			
			
			
			
			
			
			return null;
		}
		
	}

	//Holder��
	
	static class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
	
	//������Ӻ���Ե��Զ���Ի���
	public void addBlackNumber(View view) {
		
		
	}
}
