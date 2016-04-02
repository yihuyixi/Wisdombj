package com.itheima.zhbj63.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.itheima.zhbj63.MainUI;
import com.itheima.zhbj63.R;
import com.itheima.zhbj63.base.BaseFragment;
import com.itheima.zhbj63.base.BasePager;
import com.itheima.zhbj63.base.impl.GovaffairsPager;
import com.itheima.zhbj63.base.impl.HomePager;
import com.itheima.zhbj63.base.impl.NewscenterPager;
import com.itheima.zhbj63.base.impl.SettingsPager;
import com.itheima.zhbj63.base.impl.SmartServicePager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ContentFragment extends BaseFragment {

	@ViewInject(R.id.vp_content_pagers)
	private ViewPager vp_content_pagers;
	
	@ViewInject(R.id.rg_content_bottom)
	private RadioGroup rg_content_bottom;
	private List<BasePager> pagers;
	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.content_fragment, null);
		ViewUtils.inject(this,view);
		return view;
	}
	
	@Override
	protected void initData() {
		pagers = new ArrayList<BasePager>();
		pagers.add(new HomePager(mActivity));
		pagers.add(new NewscenterPager(mActivity));
		pagers.add(new SmartServicePager(mActivity));
		pagers.add(new GovaffairsPager(mActivity));
		pagers.add(new SettingsPager(mActivity));
		vp_content_pagers.setAdapter(new MyPagerAdapter());
		// 监听ViewPager的选中事件
		vp_content_pagers.setOnPageChangeListener(new MyOnPageChangeListener());
		// 监听底部单选按钮（RadioGroup）
		rg_content_bottom.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		// 初始化首页数据
		pagers.get(0).initData();
		// 初始化默认选中首页
		rg_content_bottom.check(R.id.rb_bottom_home);
	}
	
	class MyOnCheckedChangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_bottom_home:
				vp_content_pagers.setCurrentItem(0,false);// 参数2，false不带滑动效果
				enableSlidingMenu(false);
				break;
			case R.id.rb_bottom_newscenter:
				vp_content_pagers.setCurrentItem(1,false);
				enableSlidingMenu(true);
				break;
			case R.id.rb_bottom_smartservice:
				vp_content_pagers.setCurrentItem(2,false);
				enableSlidingMenu(true);
				break;
			case R.id.rb_bottom_govaffairs:
				vp_content_pagers.setCurrentItem(3,false);
				enableSlidingMenu(true);
				break;
			case R.id.rb_bottom_setting:
				vp_content_pagers.setCurrentItem(4,false);
				enableSlidingMenu(false);
				break;

			default:
				break;
			}
		}
		
	}
	
	private void enableSlidingMenu(boolean enable){
		MainUI mainUI = (MainUI) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if(enable){
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		}else{
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);// 设置左侧菜单禁止滑动
		}
	}
	
	class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			
		}
		// 某一页选择时，回调
		@Override
		public void onPageSelected(int position) {
			// 选中某一页，再去加载数据
			BasePager basePager = pagers.get(position);
			basePager.initData();
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			
		}
		
	}
	
	class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return pagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			/**
			 * 创建布局
			 * if(position==0){
			 * 
			 * }
			 * if(position==1){
			 * 
			 * }
			 * 
			 * 加载数据，更新UI
			 * if(position==1){
			 * 
			 * }
			 * 
			 * new Pager（）
			 * pager。initview
			 * pager。initData
			 */
			BasePager basePager = pagers.get(position);
			// 创建界面
			container.addView(basePager.rootView);
			// 加载数据 
//			basePager.initData();防止预加载，不在这调用
			return basePager.rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		
	}
	
	public NewscenterPager getNewscenterPager(){
		return (NewscenterPager) pagers.get(1);
	}
}
