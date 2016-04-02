package com.itheima.zhbj63.base;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itheima.zhbj63.MainUI;
import com.itheima.zhbj63.R;

public class BasePager implements OnClickListener {
	protected ImageButton ib_basepager_menu;
	
	protected TextView tv_basepager_title;
	
	protected Context mContext;// 上下文对象
	public View rootView;// 返回给外界的布局、控件

	protected FrameLayout container;// 每一个pager界面的正文父容器

	protected ImageButton ib_titlebar_photo;
	public BasePager(Context context){
		this.mContext = context;
		rootView = initView();
	}
	/**
	 * 返回布局、控件
	 * @return
	 */
	protected View initView(){
		View view = View.inflate(mContext, R.layout.basepager, null);
		ib_basepager_menu = (ImageButton) view.findViewById(R.id.ib_basepager_menu);
		tv_basepager_title = (TextView) view.findViewById(R.id.tv_basepager_title);
		container = (FrameLayout) view.findViewById(R.id.fl_basepager_container);
		ib_titlebar_photo = (ImageButton) view.findViewById(R.id.ib_titlebar_photo);
		ib_basepager_menu.setOnClickListener(this);
		return view;
	}
	/**
	 * 子类加载数据，可选覆盖
	 */
	public void initData(){
		
	}
	@Override
	public void onClick(View v) {
		MainUI mainUI = (MainUI) mContext;
		mainUI.getSlidingMenu().toggle();
	}
}
