package com.itheima.zhbj63.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalViewPager extends ViewPager {

	private int downX;
	private int downY;
	public HorizontalViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalViewPager(Context context) {
		super(context);
	}
	/**
	 * 上下滑动，自己不处理，父容器拦截
	 * 左右滑动：第0页，手指从左往右，自己不处理，父容器拦截
	 * 		       最后一页，手指从右往左，自己不处理，父容器拦截
	 * 			其他情况，自己处理，不让父容器拦截
	 * getParent().requestDisallowInterceptTouchEvent(true);true 不让父容器拦截,false 父容器拦截
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 不让父容器拦截事件，把剩下的事件都传递给我
			getParent().requestDisallowInterceptTouchEvent(true);
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			int diffX = moveX - downX;// x轴移动的距离
			int diffY = moveY - downY;// y轴移动的距离
			// 上下滑动，自己不处理，父容器拦截
			if(Math.abs(diffX)<Math.abs(diffY)){
				getParent().requestDisallowInterceptTouchEvent(false);
			}else{//左右滑动：
//				第0页，手指从左往右，自己不处理，父容器拦截
				if(0==getCurrentItem()&&diffX>0){
					getParent().requestDisallowInterceptTouchEvent(false);
				}
//				  最后一页，手指从右往左，自己不处理，父容器拦截
				else if(getAdapter().getCount()-1==getCurrentItem()&&diffX<0){
					getParent().requestDisallowInterceptTouchEvent(false);
				}
//				其他情况，自己处理，不让父容器拦截
				else{
					getParent().requestDisallowInterceptTouchEvent(true);
				}
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
