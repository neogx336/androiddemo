package com.neo.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.R.drawable;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.neo.mobilesafe.domain.AppInfo;

/**
 * 业务方法 ，提供手机里面安装的所有的应用程序的信息
 * 
 * @author biostime11
 *
 */
public class AppInfoProvider {

	/**
	 * 获取所有的安装程序信息
	 * 
	 * @param context
	 * @return
	 */
	public static List<AppInfo> getAppInfos(Context context) {

		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		for (PackageInfo packinInfo : packageInfos) {
			AppInfo appInfo = new AppInfo();
			// packinfo 相当于一个应用程序APK包的清单文件
			String packname = packinInfo.packageName;
			Drawable icon = packinInfo.applicationInfo.loadIcon(pm);
			String name = packinInfo.applicationInfo.loadLabel(pm).toString();
			// 应用程序的标记,相当于用户提交的答卷
			int flags = packinInfo.applicationInfo.flags;

			// 判断是那一类的程序
			if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// 用户程序
				appInfo.setUserApp(true);
			} else {
				// 系统程序
				appInfo.setUserApp(false);
			}

			// 判断存放位置
			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				// 手机内存
				appInfo.setInRom(true);
			} else {
				// 手机外部存储设备
				appInfo.setInRom(true);
			}

			// 添加到列表
			appInfo.setPackname(packname);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			appInfos.add(appInfo);
		}
		// 返回列表
		return appInfos;
	}

}
