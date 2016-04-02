package com.itheima.zhbj63.base.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itheima.zhbj63.base.BasePager;

public class GovaffairsPager extends BasePager{
	
	public GovaffairsPager(Context context) {
		super(context);
	}
	
	@Override
	public void initData() {
		System.out.println("政务加载了");
		ib_basepager_menu.setVisibility(View.VISIBLE);
		tv_basepager_title.setText("政务");
		
		TextView textView = new TextView(mContext);
		textView.setText("政务");
		textView.setGravity(Gravity.CENTER);
		textView.setTextColor(Color.RED);
		container.removeAllViews();
		container.addView(textView);
	}
}
