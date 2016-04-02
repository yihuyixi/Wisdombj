package com.itheima.zhbj63;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.itheima.zhbj63.fragment.ContentFragment;
import com.itheima.zhbj63.fragment.LeftFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainUI extends SlidingFragmentActivity {
	private String MAIN_TAG = "main_tag";
	private String LEFT_TAG = "left_tag";
	private FragmentManager fragmentManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置正文
		setContentView(R.layout.main);
		// 左侧菜单
		setBehindContentView(R.layout.left);
		SlidingMenu slidingMenu = getSlidingMenu();
		// 设置模式
		slidingMenu.setMode(SlidingMenu.LEFT);
		// 设置触摸范围
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置正文界面保留的宽度
		slidingMenu.setBehindOffset(200);
		initFragment();
	}

	private void initFragment() {
		// 获取管理器
		fragmentManager = getSupportFragmentManager();
		// 开启事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 替换Fragment
		transaction.replace(R.id.fl_main, new ContentFragment(), MAIN_TAG);
		transaction.replace(R.id.fl_left, new LeftFragment(), LEFT_TAG);
		// 提交事务
		transaction.commit();
	}
	// 获取左侧菜单对象
	public LeftFragment getLeftFragment(){
		return (LeftFragment) fragmentManager.findFragmentByTag(LEFT_TAG);
	}
	// 获取正文Fragment对象
	public ContentFragment getContentFragment(){
		return (ContentFragment) fragmentManager.findFragmentByTag(MAIN_TAG);
	}
}
