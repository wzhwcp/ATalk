package com.myq.atalk.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v4.util.LruCache;
import android.util.Log;

public class MyUtil {

	/** Fragment的缓存 */
	public static LruCache<String, Fragment> fragmentCache = new LruCache<String, Fragment>(
			1024 * 1024 * 4);
	/**
	 * 头像缓存空间
	 */
	public static LruCache<String, Bitmap> avatarCache = new LruCache<String, Bitmap>(
			1024 * 1024 * 2) {
		protected int sizeOf(String key, Bitmap value) {
			return value.getRowBytes() * value.getHeight();
		};
	};

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmp
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmp) {
		int width, height;
		height = bmp.getHeight();
		width = bmp.getWidth();
		Bitmap grayBmp = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(grayBmp);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(colorFilter);
		canvas.drawBitmap(bmp, 0, 0, paint);
		return grayBmp;
	}

	
	/**
	 * 获取当前栈顶的activity名称
	 * @param context
	 * @return
	 */
	public static String getTopActivity(Context context){
		ActivityManager manager = (ActivityManager) context
				.getApplicationContext().getSystemService(
						Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos =manager.getRunningTasks(1);
		if(runningTaskInfos.size()!=0){
			String[] strs= runningTaskInfos.get(0).topActivity.toString().split("/");
			return strs[1].substring(0, strs[1].length()-1);
		}else{
			return "null";
		}
	}
}
