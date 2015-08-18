package com.neo.mobilesafe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import org.json.JSONException;
import org.json.JSONObject;
import com.neo.mobilesafe.utils.StreamTools;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class SplashActivity extends Activity {

	protected static final String TAG = "SplashActivity";

	protected static final int ENTER_HOME = 1;

	protected static final int SHOW_UPDATE_DIALOG = 2;

	protected static final int URL_ERROR = 3;

	protected static final int NETWORK_ERROR = 4;

	protected static final int JSON_ERROR = 5;

	/**
	 
	 */
	private TextView tv_splash_version;
	private TextView tv_update_info;
	private TextView tv_state;
	private String description;
	private String version;

	/*
	
	 */
	private String apkurl;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);						
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tv_state=(TextView) findViewById(R.id.tv_state);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		tv_update_info = (TextView) findViewById(R.id.tv_update_info);
		tv_splash_version.setText("版本" + getVersionName());
		boolean update = sp.getBoolean("update", false);

		if (update) {
			checkUpdate();

		} else {
			
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				enterHome();				
			}
		}, 2000);
		
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOW_UPDATE_DIALOG:
				Log.i(TAG, "SHOW_UPDATE_DIALOG");				
				showUpdateDialog();
				break;				
			case URL_ERROR:		
				Toast.makeText(getApplicationContext(), "URL错误", Toast.LENGTH_LONG).show();
			//	tv_state.setText("URL错误");
				enterHome();				
				break;
			case JSON_ERROR:
				Toast.makeText(getApplicationContext(), "JSON错误", Toast.LENGTH_LONG).show();
				//tv_state.setText("JSON错误");
				break;
			case NETWORK_ERROR:	
				Toast.makeText(getApplicationContext(), "网络错误", Toast.LENGTH_LONG).show();
				//tv_state.setText("网络错误");
				enterHome();
				break;
			case ENTER_HOME:
				//tv_state.setText("进入主界面");
				Log.i(TAG, "进入主界面----------------------");
				enterHome();
				break;
				default:
					break;
		
			}
		}

	
	
	};

	/**
	 *
	 */

	private void checkUpdate() {
		// TODO Auto-generated method stub

		new Thread() {
			public void run() {
				Message meg = Message.obtain();
				long starttime = System.currentTimeMillis();
				try {
					URL url = new URL(getString(R.string.serverurl));
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(4000);
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream iStream = conn.getInputStream();
						String result = StreamTools.readFromString(iStream);
						Log.i(TAG, "网络连接成功");
						JSONObject oJsonObject = new JSONObject(result);
						version = (String) oJsonObject.get("version");
						description = (String) oJsonObject.get("description");
						apkurl = (String) oJsonObject.get("apkurl");										 
						if (getVersionName().equals(version)) {
							// 
							meg.what = ENTER_HOME;
						} else {
							meg.what = SHOW_UPDATE_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					// TODO: handle exception
					meg.what = URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// TODO: handle exception
					meg.what = NETWORK_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO: handle exception
					meg.what = JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long dTime = endTime - starttime;

					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);

						} catch (InterruptedException e2) {
							// TODO: handle exception
							e2.printStackTrace();
						}
					}
					handler.sendMessage(meg);
				}
			};
		}.start();
	}

	protected void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(SplashActivity.this);
		builder.setTitle("升级提示");		
		//强制选择
	//	builder.setCancelable(false);
		builder.setOnCancelListener(new OnCancelListener() {			
			@Override
			public void onCancel(DialogInterface dialog) {
				//取消后进入界面
				Toast.makeText(getApplicationContext(), "取消选择输入框", Toast.LENGTH_LONG).show();
				dialog.dismiss();
				enterHome();								
			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("马上升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "升级中。。。。。", Toast.LENGTH_LONG).show();
				
				//判断有没有SD卡
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					Toast.makeText(getApplicationContext(), "有SD卡，安装包下载中", Toast.LENGTH_LONG).show();
					FinalHttp finalHttp =new FinalHttp();
					finalHttp.download(apkurl,
									Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe2.0.apk",
							new AjaxCallBack<File>() {
								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									// TODO Auto-generated method stub
									t.printStackTrace();
									Toast.makeText(getApplicationContext(), "下败失败", Toast.LENGTH_LONG).show();
									super.onFailure(t, errorNo, strMsg);
								}

								@Override
								public void onLoading(long count, long current) {
									// TODO Auto-generated method stub
									super.onLoading(count, current);
									tv_update_info.setVisibility(View.VISIBLE);
									int progress= (int) (current*100/count);
									tv_update_info.setText("下载进度："+progress+"%");																											
								}

								@Override
								public void onSuccess(File t) {
									// TODO Auto-generated method stub
									super.onSuccess(t);
									installAPK(t);
								}

								private void installAPK(File t) {
									// TODO Auto-generated method stub
									Intent intent =new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
									startActivity(intent);
									
								}
						
						
							});
									
				} else {
					Toast.makeText(getApplicationContext(), "没有SD卡，请安装SD卡后再下载安装", Toast.LENGTH_LONG).show();

				}
			}
		});
		
		builder.setNegativeButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "下次再说。。。。。", Toast.LENGTH_LONG).show();		
				dialog.dismiss();
				enterHome();
			}
		});
		
		builder.show();
	}

	protected void enterHome() {
		// TODO Auto-generated method stub
		Intent intent =new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();		
	}

	/*
	 
	 */
	private String getVersionName() {
		// TODO Auto-generated method stub
		PackageManager pmManager = getPackageManager();
		try {
			PackageInfo info = pmManager.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}

}
