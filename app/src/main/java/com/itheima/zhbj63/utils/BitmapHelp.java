package com.itheima.zhbj63.utils;

import android.content.Context;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelp {
	private BitmapHelp(){
		
	}
	private static BitmapUtils instance;
	
	public static BitmapUtils getBitmapUtils(Context context){
		if(instance==null){
			instance = new BitmapUtils(context);
		}
		return instance;
	}
}
