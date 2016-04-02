package com.itheima.zhbj63.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class LocalCacheUtils {
	private String CACHE_DIR;
	private MemoryUtils memoryUtils;

	public LocalCacheUtils(MemoryUtils memoryUtils){
		CACHE_DIR = "/sdcard/zhbj63/";
		this.memoryUtils = memoryUtils;
	}
	/**
	 * 从本地取图片
	 * @param url
	 * @return
	 */
	public Bitmap getBitmap(String url){
		Bitmap bitmap = null;
		try {
			String fileName = MD5Encoder.encode(url);// jlajfla.jpg
			File file = new File(CACHE_DIR, fileName);
			if(file.exists()){
				// 从本地读取图片
				bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
				// 往内存中存一份
				memoryUtils.addBitMap(url, bitmap);
			}
		} catch (Exception e) {
		}
		
		return bitmap;
	}
	/**
	 * 往本地存储对象
	 * @param url
	 * @param bitmap
	 */
	public void saveBitmap(String url,Bitmap bitmap){
		try {
			String fileName = MD5Encoder.encode(url);
			File file = new File(CACHE_DIR, fileName);// /sdcard/12313/zhbj63/jfladjfalk
			File parentFile = file.getParentFile();
			if(!parentFile.exists()){
				parentFile.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			// 参数1：图片保存的格式，参数2：保存的质量
			bitmap.compress(CompressFormat.JPEG, 100, fos);
		} catch (Exception e) {
		}
	}
}
