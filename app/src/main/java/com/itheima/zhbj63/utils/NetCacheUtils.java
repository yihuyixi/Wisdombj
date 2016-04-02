package com.itheima.zhbj63.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ListView;

public class NetCacheUtils {
	private ExecutorService pool;
	private InternalHandler handler;
	private int SUCCESS = 1;
	private MemoryUtils memoryUtils;
	private LocalCacheUtils localCacheUtils;
	private ListView listview;

	public NetCacheUtils(MemoryUtils memoryUtils, LocalCacheUtils localCacheUtils){
		pool = Executors.newFixedThreadPool(5);
		handler = new InternalHandler();
		this.memoryUtils = memoryUtils;
		this.localCacheUtils = localCacheUtils;
	}
	
	public void display(String url,ImageView imageView, ListView listview){
		this.listview = listview;
		pool.execute(new DownLoadRunnalbe(url,imageView));
	}
	
	class InternalHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// 更新界面
			if(msg.what==SUCCESS){
				Result result = (Result) msg.obj;
//				result.imageView.setImageBitmap(result.bitmap);
				// 根据positon找到要更新的ImageView
				ImageView imageView = (ImageView) listview.findViewWithTag(result.position);
				if(imageView!=null){
					imageView.setImageBitmap(result.bitmap);
				}
			}
		}
	}
	
	class DownLoadRunnalbe implements Runnable{
		private String mUrl;
		private ImageView mImageView;
		private int position;

		public DownLoadRunnalbe(String url, ImageView imageView) {
			this.mUrl = url;
			this.mImageView = imageView;
			// 在getView方法里给ImageView设置的位置
			position = (Integer) imageView.getTag();
		}

		@Override
		public void run() {
			try {
				Thread.sleep(3000);
				HttpURLConnection conn = (HttpURLConnection) new URL(mUrl).openConnection();
				// 建立连接
				conn.connect();
				int resCode = conn.getResponseCode();
				if(resCode==200){
					InputStream is = conn.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					// 展示图片
					Message.obtain(handler, SUCCESS, new Result(bitmap,position)).sendToTarget();
					// 往内存、本地缓存图片
					localCacheUtils.saveBitmap(mUrl, bitmap);
					memoryUtils.addBitMap(mUrl, bitmap);
				}else{
					
				}
				
			} catch (Exception e) {
			}
		}
	}
	
	class Result{
		public ImageView imageView;
		public Bitmap bitmap;
		public Result(ImageView imageView, Bitmap bitmap) {
			this.imageView = imageView;
			this.bitmap = bitmap;
		}
		
		public int position;
		public Result(Bitmap bitmap, int position) {
			this.bitmap = bitmap;
			this.position = position;
		}
		
		
	}
}
