package com.neo.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.R.integer;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.neo.mobilesafe.domain.AppInfo;
import com.neo.mobilesafe.engine.AppInfoProvider;

public class AppManagerActivity extends Activity  implements OnClickListener{
	private static final String TAG="AppManagerActivity";
	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	
	private ListView lv_app_manager;
	private LinearLayout ll_loading;
	
	
	/**
	 * ���е�Ӧ�ó�����Ϣ
	 */
	
	private List<AppInfo> appInfos;
	
	/**
	 * ���е��û�������Ϣ
	 */
	
	private List<AppInfo> userAppInfos;
	
	/**
	 * ���е�ϵͳ������Ϣ
	 */
	
	private List<AppInfo> systemAppInfos;
	
	/**
	 * ��ǰ������Ϣ
	 */
	private TextView tv_status;
	
	
	/**
	 * ������������
	 */
	private PopupWindow popupWindow;
	
	/**
	 * ��������Ĳ���
	 */
	private LinearLayout ll_start;
	/**
	 * ����
	 */
	private LinearLayout ll_share;
	/**
	 * ж��
	 */
	private LinearLayout ll_unintall;
	/**
	 * ���������Ŀ
	 */
	private AppInfo appInfo;
	
	private AppManagerAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initFun();				
	}

	
	private void initFun() {
		// TODO Auto-generated method stub
		initView();
		initData();
		initListenerAndAdapter();
	}


	/**
	 * ��ʼ����������������
	 */
	private void initListenerAndAdapter() {
		
		/**
		 * ��Listview ע��һ�������ļ�����
		 */
		lv_app_manager.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			
			//����ʱ����õķ���
			//firstVisibleItem  ��һ���ɼ���Ŀ��LISTVIEW���������λ��
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopupWindows();
				
				if (userAppInfos!=null&&systemAppInfos!=null) {
					if (firstVisibleItem>userAppInfos.size()) {
						tv_status.setText("ϵͳ����"+systemAppInfos.size()+"��");
					}
					else {
						tv_status.setText("�û�����"+userAppInfos.size()+"��");
					}
				}
			}
		});
		
		
		
		
		/**
		 * ����Listveiw �ĵ���¼�
		 */
		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
	}


	protected void dismissPopupWindows() {
		// TODO Auto-generated method stub
		
	}


	/**
	 * ��ʼ������
	 */
	private void initData() {
		fillData();
		
	}

	/**
	 * �������
	 */

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		/**
		 * ���̻߳�ȡ���г������Ϣ
		 */
		new Thread(){
			public void run() {
				//��ȡ����Ӧ�ó��� ��Ϣ
				appInfos=AppInfoProvider.getAppInfos(AppManagerActivity.this);
				userAppInfos=new ArrayList<AppInfo>();
				systemAppInfos=new ArrayList<AppInfo>();
				//���з��ಢ�洢
				for (AppInfo info:appInfos ) {
					if (info.isUserApp()) {
						userAppInfos.add(info);
					}else {
						systemAppInfos.add(info);
					}
				}
				
				//����LISTVIEW������������
				runOnUiThread(new  Runnable() {
					@Override
					public void run() {
					if (adapter==null) {
						adapter=new AppManagerAdapter();
						lv_app_manager.setAdapter(adapter);
					}else {
						adapter.notifyDataSetChanged();
					}
					ll_loading.setVisibility(View.INVISIBLE);
					}
				});
				
			}
		}.start();
		
	}


	/**
	 * ��ʼ��VIEW
	 */
	private void initView() {
		// TODO Auto-generated method stub
		
		setContentView(R.layout.activity_app_manager);
		tv_status=(TextView) findViewById(R.id.tv_status);
		tv_avail_rom=(TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd=(TextView) findViewById(R.id.tv_avail_sd);
		long sdsize =getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
		long romsize =getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
		tv_avail_rom.setText("ROM���ÿռ�:"+Formatter.formatFileSize(getApplicationContext(), romsize));
		tv_avail_sd.setText("SD���ÿռ�:"+Formatter.formatFileSize(getApplicationContext(), sdsize));
		
		lv_app_manager=(ListView) findViewById(R.id.lv_app_manager);
		ll_loading=(LinearLayout) findViewById(R.id.ll_loading);
		
		
	}


	/**
	 * ��ȡĳ��Ŀ¼���ÿռ�
	 * @return
	 */
	private long getAvailSpace(String path) {
		StatFs statFs =new StatFs(path);
		statFs.getBlockCount();
		long size=statFs.getBlockSize();
		long count=statFs.getAvailableBlocks();
		return size*count;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
	
	class AppManagerAdapter extends BaseAdapter{

		//����LISTVIEW�ж��ٸ���Ŀ
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userAppInfos.size()+systemAppInfos.size()+2;
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
		//LISTVIEW��������
			//��ʾ�����û������ж��ٸ�С��ǩ
			if (position==0) {
				TextView tv=new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("�û�����:"+userAppInfos.size()+"��");
				return tv;
			}
			else if(position==(userAppInfos.size()+1)) {
				TextView tv=new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("ϵͳ����:"+systemAppInfos.size()+"��");
				return tv;
			} 
			
			/**
			 * ������ʾ���� 
			 * 1.�û�����
			 * 2.ϵͳ����
			 */
			    //��ʾ�û�����
			else if(position<=userAppInfos.size()) {
				//��Ϊ����һ��TEXTView ������
				int newposition=position-1;
				appInfo=userAppInfos.get(newposition);
			}else {
				//��ʾϵͳ����
				int newposition=position-1-userAppInfos.size()-1;
				appInfo=systemAppInfos.get(newposition);
			}
			
			//���ع���
			View view;
			ViewHolder holder;
			
			//��Ŀ��VIEW��Ϊ�ղ�����RelativeLayout
			if (convertView!=null&convertView instanceof RelativeLayout) {
				//������Ҫ����Ƿ�Ϊ�գ���Ҫ�ж��Ƿ��Ǻ��ʵ�����ȥ����
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			else {
				//���֣���ʼ��
				view=View.inflate(getApplicationContext(), R.layout.list_item_appinfo, null);
				holder=new ViewHolder();
				holder.iv_icon=(ImageView) view.findViewById(R.id.iv_app_icon);
				holder.tv_location=(TextView) view.findViewById(R.id.tv_app_location);
				holder.tv_name=(TextView) view.findViewById(R.id.tv_app_name);
				view.setTag(holder);
			}
			
			holder.iv_icon.setImageDrawable(appInfo.getIcon());
			holder.tv_name.setText(appInfo.getName());
			if (appInfo.isInRom()) {
				holder.tv_location.setText("�ֻ��ڴ�");
			}
			else {
				holder.tv_location.setText("SD���洢");
			}
			return view;
		}
	}
	
	
	static class ViewHolder{
		TextView tv_name;
		TextView tv_location;
		ImageView iv_icon;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	

}
