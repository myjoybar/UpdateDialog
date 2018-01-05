package com.joybar.libupdate.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.joybar.libupdate.download.FileDownloadManager;


/**
 * Created by joybar on 6/17/16.
 */
public class ApkUpdateUtils {

    public static final String TAG = ApkUpdateUtils.class.getSimpleName();

    private static final String KEY_DOWNLOAD_ID = "downloadId";

    public static void download(Context context, String url, String title) {
        long downloadId = SpUtils.getInstance(context).getLong(KEY_DOWNLOAD_ID, -1L);
        if (downloadId != -1L) {
            Log.d(TAG,"开始下载");
            FileDownloadManager fdm = FileDownloadManager.getInstance(context);
            int status = fdm.getDownloadStatus(downloadId);
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                Log.d(TAG,"下载成功");
                //启动更新界面
                Uri uri = fdm.getDownloadUri(downloadId);
                if (uri != null) {
                    Log.d(TAG,"uri="+uri);
                    if (compare(getApkInfo(context, uri.getPath()), context)) {
                        startInstall(context, uri);
                        return;
                    } else {
                        Log.d(TAG,"delete");
                        fdm.getDm().remove(downloadId);
                    }
                }else{
                    Log.d(TAG,"uri==null");
                }
                start(context, url, title);
            } else if (status == DownloadManager.STATUS_FAILED) {
                Log.d(TAG,"下载失败");
                start(context, url, title);
            } else {
                Log.d(TAG, "apk is already downloading");
            }
        } else {
            start(context, url, title);
        }
    }

    private static void start(Context context, String url, String title) {
        long id = FileDownloadManager.getInstance(context).startDownload(url,
                title, "下载完成后点击打开");
        SpUtils.getInstance(context).putLong(KEY_DOWNLOAD_ID, id);
        Log.d(TAG, "apk start download " + id);
    }

    public static void startInstall(Context context, Uri uri) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }


    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param context Context
     * @param path    apk path
     */
    private static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            //String packageName = info.packageName;
            //String version = info.versionName;
            //Log.d(TAG, "packageName:" + packageName + ";version:" + version);
            //String appName = pm.getApplicationLabel(appInfo).toString();
            //Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
            return info;
        }
        return null;
    }


    /**
     * 下载的apk和当前程序版本比较
     *
     * @param apkInfo apk file's packageInfo
     * @param context Context
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    private static boolean compare(PackageInfo apkInfo, Context context) {
        if (apkInfo == null) {
            return false;
        }
        String localPackage = context.getPackageName();

        Log.d(TAG,"localPackage="+localPackage);
        Log.d(TAG,"apkInfo.packageName="+apkInfo.packageName);
        if (apkInfo.packageName.equals(localPackage)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}



