package com.itheima.zhbj63.base;

import android.content.Context;
import android.view.View;

public abstract class MenuBasePager {
	protected Context mContext;// 上下问对象
	public View rootView;
	public MenuBasePager(Context context){
		this.mContext = context;
		rootView = initView();
	}
	/**
	 * 子类实现，返回具体的控件、布局
	 * @return
	 */
	protected abstract View initView();
	/**
	 * 更新布局
	 */
	public void initData(){
		
	}
}
