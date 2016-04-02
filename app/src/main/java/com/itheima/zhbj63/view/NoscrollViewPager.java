package com.itheima.zhbj63.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoscrollViewPager extends ViewPager {

	public NoscrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoscrollViewPager(Context context) {
		super(context);
	}
	
	// 把原始ViewPager的默认滑动事件去除
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}
	
	// 不让拦截事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}
	
}
