package com.neo.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
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
import android.widget.Toast;

import com.neo.mobilesafe.domain.AppInfo;
import com.neo.mobilesafe.engine.AppInfoProvider;
import com.neo.mobilesafe.utils.DensityUtil;

public class AppManagerActivity extends Activity implements OnClickListener {
	private static final String TAG = "AppManagerActivity";
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
	private LinearLayout ll_uninstall;
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

			// ����ʱ����õķ���
			// firstVisibleItem ��һ���ɼ���Ŀ��LISTVIEW���������λ��
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopupWindows();

				if (userAppInfos != null && systemAppInfos != null) {
					if (firstVisibleItem > userAppInfos.size()) {
						tv_status.setText("ϵͳ����" + systemAppInfos.size() + "��");
					} else {
						tv_status.setText("�û�����" + userAppInfos.size() + "��");
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
				
				Toast.makeText(getApplicationContext(), "������:"+position, 1000).show();
				// �����ǰ�ϵͳ����Ŀ���˵�������Ӧ����¼�
				if (position == 0) {
					return;
				} else if (position == (userAppInfos.size() + 1)) {
					return;
				} else if (position <= userAppInfos.size()) {
					// �û�����
					int netposition = position - 1;
					appInfo = userAppInfos.get(netposition);
				} else {
					// ϵͳ����
					int newposition = position - 1 - userAppInfos.size() - 1;
					appInfo = systemAppInfos.get(newposition);
				}
				dismissPopupWindows();

				// ��ʼ��������ť
				View contentView = View.inflate(getApplicationContext(),
						R.layout.popup_app_item, null);
				ll_start = (LinearLayout) contentView
						.findViewById(R.id.ll_start);
				ll_share = (LinearLayout) contentView
						.findViewById(R.id.ll_share);
				ll_uninstall = (LinearLayout) contentView
						.findViewById(R.id.ll_uninstall);

				ll_share.setOnClickListener(AppManagerActivity.this);
				ll_start.setOnClickListener(AppManagerActivity.this);
				ll_uninstall.setOnClickListener(AppManagerActivity.this);

				popupWindow = new PopupWindow(contentView, -2, -2);
				// ����Ч���Ĳ��ű���Ҫ�����б�����ɫ
				// ͸����ɫҲ����ɫ
				popupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));

				int[] location = new int[2];
				// ��ȡ�ڵ�ǰ�����ڵľ������� location [0]--->x����,location [1]--->y����
				view.getLocationInWindow(location);
				int dip = 60;
				int px = DensityUtil.dip2px(getApplicationContext(), dip);
				System.out.println("px=" + px);

				popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP,
						px, location[1]);
				ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0.5f);
				sa.setDuration(300);
				AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
				aa.setDuration(300);
				AnimationSet anset = new AnimationSet(false);
				anset.addAnimation(aa);
				anset.addAnimation(sa);
				contentView.startAnimation(anset);
			}
		});
	}

	protected void dismissPopupWindows() {
		//�Ѿɵĵ�������رյ�
		if (popupWindow!=null&&popupWindow.isShowing()) {
			popupWindow.dismiss();
		}

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
		new Thread() {
			public void run() {
				// ��ȡ����Ӧ�ó��� ��Ϣ
				appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				// ���з��ಢ�洢
				for (AppInfo info : appInfos) {
					if (info.isUserApp()) {
						userAppInfos.add(info);
					} else {
						systemAppInfos.add(info);
					}
				}

				// ����LISTVIEW������������
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (adapter == null) {
							adapter = new AppManagerAdapter();
							lv_app_manager.setAdapter(adapter);
						} else {
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
		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
		long sdsize = getAvailSpace(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		long romsize = getAvailSpace(Environment.getDataDirectory()
				.getAbsolutePath());
		tv_avail_rom.setText("ROM���ÿռ�:"
				+ Formatter.formatFileSize(getApplicationContext(), romsize));
		tv_avail_sd.setText("SD���ÿռ�:"
				+ Formatter.formatFileSize(getApplicationContext(), sdsize));

		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

	}

	/**
	 * ��ȡĳ��Ŀ¼���ÿռ�
	 * 
	 * @return
	 */
	private long getAvailSpace(String path) {
		StatFs statFs = new StatFs(path);
		statFs.getBlockCount();
		long size = statFs.getBlockSize();
		long count = statFs.getAvailableBlocks();
		return size * count;
	}

	class AppManagerAdapter extends BaseAdapter {

		// ����LISTVIEW�ж��ٸ���Ŀ
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userAppInfos.size() + systemAppInfos.size() + 2;
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
			// LISTVIEW��������
			// ��ʾ�����û������ж��ٸ�С��ǩ
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("�û�����:" + userAppInfos.size() + "��");
				return tv;
			} else if (position == (userAppInfos.size() + 1)) {
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("ϵͳ����:" + systemAppInfos.size() + "��");
				return tv;
			}

			/**
			 * ������ʾ���� 1.�û����� 2.ϵͳ����
			 */
			// ��ʾ�û�����
			else if (position <= userAppInfos.size()) {
				// ��Ϊ����һ��TEXTView ������
				int newposition = position - 1;
				appInfo = userAppInfos.get(newposition);
			} else {
				// ��ʾϵͳ����
				int newposition = position - 1 - userAppInfos.size() - 1;
				appInfo = systemAppInfos.get(newposition);
			}

			// ���ع���
			View view;
			ViewHolder holder;

			// ��Ŀ��VIEW��Ϊ�ղ�����RelativeLayout
			if (convertView != null & convertView instanceof RelativeLayout) {
				// ������Ҫ����Ƿ�Ϊ�գ���Ҫ�ж��Ƿ��Ǻ��ʵ�����ȥ����
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				// ���֣���ʼ��
				view = View.inflate(getApplicationContext(),
						R.layout.list_item_appinfo, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_app_icon);
				holder.tv_location = (TextView) view
						.findViewById(R.id.tv_app_location);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_app_name);
				view.setTag(holder);
			}

			holder.iv_icon.setImageDrawable(appInfo.getIcon());
			holder.tv_name.setText(appInfo.getName());
			if (appInfo.isInRom()) {
				holder.tv_location.setText("�ֻ��ڴ�");
			} else {
				holder.tv_location.setText("SD���洢");
			}
			return view;
		}
	}

	static class ViewHolder {
		TextView tv_name;
		TextView tv_location;
		ImageView iv_icon;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		dismissPopupWindows();

		switch (v.getId()) {
		case R.id.ll_share:
			Log.i(TAG, "����"+appInfo.getName());
			shareApplication();

			break;
		case R.id.ll_start:
			Log.i(TAG, "������"+appInfo.getName());
			startApplication();

			break;
		case R.id.ll_uninstall:
			if (appInfo.isUserApp()) {
				Log.i(TAG, "ж�أ�"+appInfo.getName());
				uninstallApplication();
			}else {
				Toast.makeText(this, "ϵͳӦ��ֻ�л�ȡROOTȨ�޲ſ���ж��", Toast.LENGTH_LONG).show();
			}
			
			break;

		default:
			break;
		}
	}

	
	/**
	 * ж��Ӧ��
	 */
	private void uninstallApplication() {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:"+appInfo.getPackname()));
		startActivityForResult(intent, 0);
	}

	/**
	 * ����Ӧ��
	 */
	private void startApplication() {
		PackageManager pmManager=getPackageManager();
		Intent intent=pmManager.getLaunchIntentForPackage(appInfo.getPackname());
		if (intent!=null) {
			startActivity(intent);
		}
		else {
			Toast.makeText(getApplicationContext(), "����������ǰӦ��", 0).show();
		}				
	}

	/**
	 * ����Ӧ��
	 */
	private void shareApplication() {
		Intent intent=new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "�Ƽ���һ������������ǣ�"+appInfo.getName());
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		fillData();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
