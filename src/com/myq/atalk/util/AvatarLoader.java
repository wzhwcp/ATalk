package com.myq.atalk.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.myq.atalk.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * 头像异步加载类
 * 
 * @author quan
 * 
 */
public class AvatarLoader extends AsyncTask<String, Integer, Bitmap> {

	private Context context;
	private ImageView imageView;
	private boolean flag; // 是否在线
	private String str = "";

	public AvatarLoader() {

	}

	public AvatarLoader(Context context, ImageView imageview, boolean flag) {
		this.context = context;
		this.imageView = imageview;
		this.flag=flag;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		imageView.setImageResource(R.drawable.ic_launcher);
		super.onPreExecute();
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;
		str =params[0];
		File file = new File("/data/data/com.myq.atalk/files/" + params[0]);
		if (file.exists()) { // 如果他存在在手机文件中则直接获取并加入到内存缓存中
			Log.e("file", "exists");
			bitmap = BitmapFactory.decodeFile(file.getPath());
			MyUtil.avatarCache.put(params[0], bitmap);
		} else {
			Log.e("file", "not exists");
			bitmap = SmackUtil.getUserImage(params[0]);
			try {
				FileOutputStream fos = context.openFileOutput(params[0],
						context.MODE_WORLD_READABLE);
				bitmap.compress(CompressFormat.PNG, 100, fos);
				MyUtil.avatarCache.put(params[0], bitmap);
				fos.flush();
				fos.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		if (result != null&&imageView.getTag().equals(str)) {
			if (flag) {
					imageView.setImageBitmap(result);
			} else {
				imageView.setImageBitmap(MyUtil.toGrayscale(result));
			}
		} 
		super.onPostExecute(result);
	}
}
