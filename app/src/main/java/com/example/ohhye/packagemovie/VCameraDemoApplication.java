package com.example.ohhye.packagemovie;

import android.app.Application;
import android.os.Environment;

import com.yixia.camera.VCamera;
import com.yixia.camera.util.DeviceUtils;

import java.io.File;

public class VCameraDemoApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// 设置拍摄视频缓存路径
		File movies = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
		if (DeviceUtils.isZte()) {
			if (movies.exists()) {
				VCamera.setVideoCachePath(movies + "/PackageMovie/");
			} else {
				VCamera.setVideoCachePath(movies.getPath().replace("/sdcard/", "/sdcard-ext/") + "/PackageMovie/");
			}
		} else {
			VCamera.setVideoCachePath(movies + "/PackageMovie/");
		}
		// 开启log输出,ffmpeg输出到logcat
		VCamera.setDebugMode(true);
		// 初始化拍摄SDK，必须
		VCamera.initialize(this);
	}

}
