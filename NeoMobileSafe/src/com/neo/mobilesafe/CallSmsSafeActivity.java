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
 * 黑名单添加页面
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
	 * 自定义对话框所使用的元素
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
		//初始化界面
		initView();
	}

	private void initView() {
		//初始化参数
		lv_callsms_safe=(ListView) findViewById(R.id.lv_callsms_safe);
		dao=new BlackNumberDao(this);
		infos=dao.findAll();
		adapter=new CallSmsSafeAdapter();
		lv_callsms_safe.setAdapter(adapter);
	}
	
	//自定义列表适配器
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
			//1.优化对像
			View view;
			ViewHolder holder;
			if (convertView==null) {
				//2.如真的没有才创建对象
				view=View.inflate(getApplicationContext(), R.layout.list_item_callsms, null);
				holder=new ViewHolder();
				holder.tv_number=(TextView) view.findViewById(R.id.tv_black_number);
				holder.tv_mode=(TextView) view.findViewById(R.id.tv_block_mode);
				holder.iv_delete=(ImageView) view.findViewById(R.id.iv_delete);
				view.setTag(holder);
				
			} else {
				//如果不是的话就取出来
				Log.i(TAG,"厨房有历史的view对象，复用历史缓存的view对象："+position);
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			
			//完成取对象的创建后,需要给对象赋值
			holder.tv_number.setText(infos.get(position).getNumber());
			String modeString=infos.get(position).getMode();
			if ("1".equals(modeString)) {
				holder.tv_mode.setText("电话拦截");
				
				
			} else if ("2".equals(modeString)) {
				holder.tv_mode.setText("电话拦截");
			} 
			else{
				holder.tv_mode.setText("全部拦截");
			}

			
			
			
			
			
			
			return null;
		}
		
	}

	//Holder类
	
	static class ViewHolder{
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}
	
	//弹出添加号码对的自定义对话框
	public void addBlackNumber(View view) {
		
		
	}
}
