package com.itheima.zhbj63.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryUtils {
	private LruCache<String, Bitmap> lruCache;

	public MemoryUtils(){
		int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);// 取当前运行时最大内存的1/8
		lruCache = new LruCache<String, Bitmap>(maxSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 每次添加图片时，需要知道这张图片的空间
				return value.getRowBytes()*value.getHeight();
			}
		};
	}
	/**
	 * 从内存中获取缓存对象
	 * @param url
	 * @return
	 */
	public Bitmap getBitMap(String url){
		return lruCache.get(url);
	}
	/**
	 * 往内存中存对象
	 * @param url
	 * @param value
	 */
	public void addBitMap(String url,Bitmap value){
		lruCache.put(url, value);
	}
}
