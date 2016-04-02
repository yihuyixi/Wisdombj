package com.itheima.zhbj63.base.impl.menu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.zhbj63.MainUI;
import com.itheima.zhbj63.R;
import com.itheima.zhbj63.base.MenuBasePager;
import com.itheima.zhbj63.domain.NewscenterBean.NewsTab;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailPager extends MenuBasePager {

	private List<NewsTab> mNewsData;// 新闻详情标题数据

	@ViewInject(R.id.pager)
	private ViewPager pager;// 新闻详情中页签对应的界面
	@ViewInject(R.id.indicator)
	private TabPageIndicator indicator;// 新闻详情中标题控件

	private List<TabDetailPager> tabPagers;
	public NewsDetailPager(Context context, List<NewsTab> children) {
		super(context);
		this.mNewsData = children;
	}

	@Override
	protected View initView() {
		View view = View.inflate(mContext, R.layout.newsdetail, null);
		ViewUtils.inject(this,view);
		return view;
	}
	
	@Override
	public void initData() {
		tabPagers = new ArrayList<TabDetailPager>();
		// 初始化新闻页签对象 
		for(int i=0;i<mNewsData.size();i++){
			tabPagers.add(new TabDetailPager(mContext,mNewsData.get(i)));
		}
		
		// 给ViewPager设置数据
		pager.setAdapter(new MyPagerAdapter());
		// 绑定标题控件
		indicator.setViewPager(pager);
		// 监听ViewPager
//		pager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 当Indicator绑定ViewPager时，必须把OnPageChangeListener设置给Indicator
		indicator.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			
		}

		@Override
		public void onPageSelected(int position) {
			// 如果是第0页，让侧滑菜单可以滑动，其他页就不让他滑动
			MainUI mainUI = (MainUI) mContext;
			SlidingMenu slidingMenu = mainUI.getSlidingMenu();
			if(position==0){
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			}else{
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			
		}
		
	}
	
	class MyPagerAdapter extends PagerAdapter{
		/**
		 * 返回的标题就是给TabPageIndicator用
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return mNewsData.get(position).title;
		}

		@Override
		public int getCount() {
			return mNewsData.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager tabDetailPager = tabPagers.get(position);
			container.addView(tabDetailPager.rootView);
			tabDetailPager.initData();
			return tabDetailPager.rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		
		
	}

}
