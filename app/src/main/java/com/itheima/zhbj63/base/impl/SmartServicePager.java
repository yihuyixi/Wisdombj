package com.itheima.zhbj63.base.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itheima.zhbj63.base.BasePager;

public class SmartServicePager extends BasePager{
	
	public SmartServicePager(Context context) {
		super(context);
	}
	
	@Override
	public void initData() {
		System.out.println("智慧服务加载了");
		ib_basepager_menu.setVisibility(View.VISIBLE);
		tv_basepager_title.setText("智慧服务");
		
		TextView textView = new TextView(mContext);
		textView.setText("智慧服务");
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.RED);
		container.removeAllViews();
		container.addView(textView);
	}
}
