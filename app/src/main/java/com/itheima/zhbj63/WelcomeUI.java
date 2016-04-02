package com.itheima.zhbj63;

import com.itheima.zhbj63.utils.CacheUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class WelcomeUI extends Activity {
	public static final String IS_FIRST_OPEN = "is_first_open";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		init();
	}

	private void init() {
		RelativeLayout rl_welcome_bg = (RelativeLayout) findViewById(R.id.rl_welcome_bg);
		
		// 旋转动画，0~360
		RotateAnimation rotateAnimation = new RotateAnimation(
				0, 
				360, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, .5f);
		rotateAnimation.setFillAfter(true);// 让动画保存旋转后的状态
		rotateAnimation.setDuration(1000);
		
		// 缩放动画，从无到有
		ScaleAnimation scaleAnimation = new ScaleAnimation(
				0, 1, 
				0, 1, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(1000);
		scaleAnimation.setFillAfter(true);
		
		// 渐变动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);
		
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(rotateAnimation);
		
		rl_welcome_bg.startAnimation(animationSet);
		// 监听动画
		animationSet.setAnimationListener(new MyAnimationListener());
	}
	
	class MyAnimationListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// 动画执行后回调
			// 判断是否用户第一次打开应用，是就跳到引导界面，不是就直接跳到主界面
			boolean isFirstOpen = CacheUtils.getBoolean(getApplicationContext(), IS_FIRST_OPEN, true);
			if(isFirstOpen){
				// 跳到引导界面
				System.out.println("跳到引导界面");
				startActivity(new Intent(WelcomeUI.this,GuideUI.class));
			}else{
				// 跳到主界面
				System.out.println("跳到主界面");
				startActivity(new Intent(WelcomeUI.this,MainUI.class));
			}
			finish();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
	}

}
