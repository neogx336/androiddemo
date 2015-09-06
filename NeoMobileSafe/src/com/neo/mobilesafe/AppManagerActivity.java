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
	 * 所有的应用程序信息
	 */
	
	private List<AppInfo> appInfos;
	
	/**
	 * 所有的用户程序信息
	 */
	
	private List<AppInfo> userAppInfos;
	
	/**
	 * 所有的系统程序信息
	 */
	
	private List<AppInfo> systemAppInfos;
	
	/**
	 * 当前程序信息
	 */
	private TextView tv_status;
	
	
	/**
	 * 弹出悬浮窗体
	 */
	private PopupWindow popupWindow;
	
	/**
	 * 开启程序的布局
	 */
	private LinearLayout ll_start;
	/**
	 * 分享
	 */
	private LinearLayout ll_share;
	/**
	 * 卸载
	 */
	private LinearLayout ll_unintall;
	/**
	 * 被点击的条目
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
	 * 初始化监听器与适配器
	 */
	private void initListenerAndAdapter() {
		
		/**
		 * 给Listview 注册一个滚动的监听器
		 */
		lv_app_manager.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			
			//滚动时候调用的方法
			//firstVisibleItem  第一条可见条目在LISTVIEW集合里面的位置
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopupWindows();
				
				if (userAppInfos!=null&&systemAppInfos!=null) {
					if (firstVisibleItem>userAppInfos.size()) {
						tv_status.setText("系统程序："+systemAppInfos.size()+"个");
					}
					else {
						tv_status.setText("用户程序："+userAppInfos.size()+"个");
					}
				}
			}
		});
		
		
		
		
		/**
		 * 设置Listveiw 的点击事件
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
	 * 初始化数据
	 */
	private void initData() {
		fillData();
		
	}

	/**
	 * 填充数据
	 */

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		/**
		 * 开线程获取所有程序的信息
		 */
		new Thread(){
			public void run() {
				//获取所有应用程序 信息
				appInfos=AppInfoProvider.getAppInfos(AppManagerActivity.this);
				userAppInfos=new ArrayList<AppInfo>();
				systemAppInfos=new ArrayList<AppInfo>();
				//进行分类并存储
				for (AppInfo info:appInfos ) {
					if (info.isUserApp()) {
						userAppInfos.add(info);
					}else {
						systemAppInfos.add(info);
					}
				}
				
				//加载LISTVIEW的数据适配器
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
	 * 初始化VIEW
	 */
	private void initView() {
		// TODO Auto-generated method stub
		
		setContentView(R.layout.activity_app_manager);
		tv_status=(TextView) findViewById(R.id.tv_status);
		tv_avail_rom=(TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd=(TextView) findViewById(R.id.tv_avail_sd);
		long sdsize =getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
		long romsize =getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
		tv_avail_rom.setText("ROM可用空间:"+Formatter.formatFileSize(getApplicationContext(), romsize));
		tv_avail_sd.setText("SD可用空间:"+Formatter.formatFileSize(getApplicationContext(), sdsize));
		
		lv_app_manager=(ListView) findViewById(R.id.lv_app_manager);
		ll_loading=(LinearLayout) findViewById(R.id.ll_loading);
		
		
	}


	/**
	 * 获取某个目录可用空间
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

		//控制LISTVIEW有多少个条目
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
		//LISTVIEW悬浮窗体
			//显示的是用户程序有多少个小标签
			if (position==0) {
				TextView tv=new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("用户程序:"+userAppInfos.size()+"个");
				return tv;
			}
			else if(position==(userAppInfos.size()+1)) {
				TextView tv=new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统程序:"+systemAppInfos.size()+"个");
				return tv;
			} 
			
			/**
			 * 分组显示程序 
			 * 1.用户程序
			 * 2.系统程序
			 */
			    //显示用户程序
			else if(position<=userAppInfos.size()) {
				//因为多了一个TEXTView 的文用
				int newposition=position-1;
				appInfo=userAppInfos.get(newposition);
			}else {
				//显示系统程序
				int newposition=position-1-userAppInfos.size()-1;
				appInfo=systemAppInfos.get(newposition);
			}
			
			//回载过程
			View view;
			ViewHolder holder;
			
			//条目的VIEW不为空并且是RelativeLayout
			if (convertView!=null&convertView instanceof RelativeLayout) {
				//不公需要检查是否为空，还要判断是否是合适的类型去复用
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			else {
				//布局，初始化
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
				holder.tv_location.setText("手机内存");
			}
			else {
				holder.tv_location.setText("SD卡存储");
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
