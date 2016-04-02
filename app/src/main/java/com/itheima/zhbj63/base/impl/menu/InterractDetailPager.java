package com.itheima.zhbj63.base.impl.menu;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itheima.zhbj63.base.MenuBasePager;

public class InterractDetailPager extends MenuBasePager {

	public InterractDetailPager(Context context) {
		super(context);
	}

	@Override
	protected View initView() {
		TextView textView = new TextView(mContext);
		textView.setText("互动详情界面");
		textView.setGravity(Gravity.CENTER);
		return textView;
	}

}
