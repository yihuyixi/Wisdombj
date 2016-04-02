package com.itheima.zhbj63.base.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.itheima.zhbj63.base.BasePager;

public class HomePager extends BasePager{
	public HomePager(Context context) {
		super(context);
	}
	
	@Override
	public void initData() {
		System.out.println("首页加载了");
		tv_basepager_title.setText("首页");
		
		TextView textView = new TextView(mContext);
		textView.setText("首页");
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.RED);
		container.removeAllViews();
		container.addView(textView);
	}
	
}
