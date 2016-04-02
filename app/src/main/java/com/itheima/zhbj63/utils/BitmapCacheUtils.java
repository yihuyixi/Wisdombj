package com.itheima.zhbj63.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ListView;

public class BitmapCacheUtils {
	
	private MemoryUtils memoryUtils;
	private LocalCacheUtils localCacheUtils;
	private NetCacheUtils netCacheUtils;
	private ListView listview;

	public BitmapCacheUtils(){
		memoryUtils = new MemoryUtils();
		localCacheUtils = new LocalCacheUtils(memoryUtils);
		netCacheUtils = new NetCacheUtils(memoryUtils,localCacheUtils);
	}
	
	public void display(ImageView imageView ,String url, ListView listview){
		this.listview = listview;
		Bitmap bitmap = null;
//		1、从内存中获取
		bitmap = memoryUtils.getBitMap(url);
		if(bitmap !=null){
			System.out.println("从内存中获取");
			imageView.setImageBitmap(bitmap);
			return;
		}
//		2、如果获取不到，从本地获取
		bitmap = localCacheUtils.getBitmap(url);
		if (bitmap != null) {
			System.out.println("从本地获取");
			imageView.setImageBitmap(bitmap);
			return;
		}
//		3、再获取不到，从网络获取
//		直接访问网络，下载到图片后，更新界面
		netCacheUtils.display(url, imageView,listview);
		System.out.println("从网络获取");
	}
}
