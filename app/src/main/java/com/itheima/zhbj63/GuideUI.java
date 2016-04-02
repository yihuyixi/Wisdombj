package com.itheima.zhbj63;

import java.util.ArrayList;
import java.util.List;

import com.itheima.zhbj63.utils.CacheUtils;
import com.itheima.zhbj63.utils.CommonUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class GuideUI extends Activity implements OnClickListener {
	private List<ImageView> images;
	private LinearLayout ll_guide_points;
	private View guide_redPoint;
	private Button bt_guide_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 隐藏标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide);
		init();
	}

	private void init() {
		ViewPager vp_guide_bg = (ViewPager) findViewById(R.id.vp_guide_bg);
		ll_guide_points = (LinearLayout) findViewById(R.id.ll_guide_points);
		guide_redPoint = findViewById(R.id.guide_redPoint);
		bt_guide_start = (Button) findViewById(R.id.bt_guide_start);
		bt_guide_start.setOnClickListener(this);
		// 准备数据
		initData();
		// 设置数据适配器
		vp_guide_bg.setAdapter(new MyPagerAdapter());
		// 监听ViewPager滚动事件
		vp_guide_bg.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	private void initData() {
		int[] imageIds = new int[] { R.drawable.guide_1, R.drawable.guide_2,
				R.drawable.guide_3 };
		images = new ArrayList<ImageView>();
		for(int i=0;i<imageIds.length;i++){
			ImageView imageView = new ImageView(this);
//			imageView.setScaleType(ScaleType.FIT_XY);
//			imageView.setImageResource(imageIds[i]);
			imageView.setBackgroundResource(imageIds[i]);
			images.add(imageView);
			
			// 创建灰色点
			View view = new View(this);
			view.setBackgroundResource(R.drawable.guide_point_normal);
			int dp2px = CommonUtils.dp2px(getApplicationContext(), 10);
			LayoutParams params = new LayoutParams(dp2px, dp2px);
			if(i!=0){
				params.leftMargin = dp2px;
			}
			view.setLayoutParams(params);
			// 添加灰点
			ll_guide_points.addView(view);
		}
	}
	
	class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// 计算红点应该移动的距离
			System.out.println("postion:" + position + ":positionOffset:" + positionOffset +":positionOffsetPixels:" + positionOffsetPixels);
			int redMoveX = (int) ((position + positionOffset)*CommonUtils.dp2px(getApplicationContext(), 20));// 手指移动的距离/屏幕宽度 * 灰点间距
			// 设置红点的位置
			android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) guide_redPoint.getLayoutParams();
			params.leftMargin = redMoveX;
			guide_redPoint.setLayoutParams(params);
		}

		@Override
		public void onPageSelected(int position) {
			if(position==images.size()-1){
				bt_guide_start.setVisibility(View.VISIBLE);
			}else{
				bt_guide_start.setVisibility(View.INVISIBLE);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			
		}
		
	}
	
	class MyPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = images.get(position);
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		
		
	}

	@Override
	public void onClick(View v) {
		CacheUtils.putBoolean(this, WelcomeUI.IS_FIRST_OPEN, false);
		startActivity(new Intent(this,MainUI.class));
		finish();
	}
}
