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
 * ҵ�񷽷� ���ṩ�ֻ����氲װ�����е�Ӧ�ó������Ϣ
 * 
 * @author biostime11
 *
 */
public class AppInfoProvider {

	/**
	 * ��ȡ���еİ�װ������Ϣ
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
			// packinfo �൱��һ��Ӧ�ó���APK�����嵥�ļ�
			String packname = packinInfo.packageName;
			Drawable icon = packinInfo.applicationInfo.loadIcon(pm);
			String name = packinInfo.applicationInfo.loadLabel(pm).toString();
			// Ӧ�ó���ı��,�൱���û��ύ�Ĵ��
			int flags = packinInfo.applicationInfo.flags;

			// �ж�����һ��ĳ���
			if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				// �û�����
				appInfo.setUserApp(true);
			} else {
				// ϵͳ����
				appInfo.setUserApp(false);
			}

			// �жϴ��λ��
			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				// �ֻ��ڴ�
				appInfo.setInRom(true);
			} else {
				// �ֻ��ⲿ�洢�豸
				appInfo.setInRom(true);
			}

			// ��ӵ��б�
			appInfo.setPackname(packname);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			appInfos.add(appInfo);
		}
		// �����б�
		return appInfos;
	}

}
