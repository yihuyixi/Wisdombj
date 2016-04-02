package com.itheima.zhbj63.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.itheima.zhbj63.R;
import com.itheima.zhbj63.view.RefreshListview.OnRefreshListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RefreshListview extends ListView {

	private int downY = -1;
	private int headerHeight;
	private View header;
	private static final int PULLDOWN_REFRESH = 0;// 下拉刷新状态
	private static final int RELEASE_REFRESH = 1;// 松开刷新状态
	private static final int REFRESHING = 2;// 正在刷新状态
	private int CURRENT_STATE = PULLDOWN_REFRESH;// 当前状态

	@ViewInject(R.id.iv_refresh_arrow)
	private ImageView iv_refresh_arrow;

	@ViewInject(R.id.pb_refresh_progress)
	private ProgressBar pb_refresh_progress;

	@ViewInject(R.id.tv_refresh_state)
	private TextView tv_refresh_state;

	@ViewInject(R.id.tv_refresh_time)
	private TextView tv_refresh_time;
	private RotateAnimation up;
	private RotateAnimation down;
	private OnRefreshListener mListener;

	private Handler handler = new Handler();
	private View footer;

	public RefreshListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeader();
		initFooter();
	}

	public RefreshListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeader();
		initFooter();
	}

	public RefreshListview(Context context) {
		super(context);
		initHeader();
		initFooter();
	}

	private void initFooter() {
		footer = View.inflate(getContext(), R.layout.refresh_footer, null);
		footer.measure(0, 0);
		footerHeight = footer.getMeasuredHeight();
		footer.setPadding(0, -footerHeight, 0, 0);
		this.addFooterView(footer);

		// 监听Listview的滚动事件
		this.setOnScrollListener(new MyOnScrollListener());
	}

	private void initHeader() {
		header = View.inflate(getContext(), R.layout.refresh_header, null);
		// 获取头布局的高度
		header.measure(0, 0);
		headerHeight = header.getMeasuredHeight();
		// 隐藏头布局
		header.setPadding(0, -headerHeight, 0, 0);

		ViewUtils.inject(this, header);
		this.addHeaderView(header);

		initAnimation();

//		 test();
	}

	private void test() {
		this.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefreshing() {
				new Thread() {
					public void run() {
						try {
							System.out.println("开始访问网络");
							Thread.sleep(3000);
							System.out.println("开始访问网络结束");

							handler.post(new Runnable() {

								@Override
								public void run() {
									// refreshFinished();
								}
							});
						} catch (InterruptedException e) {
						}
					};
				}.start();
			}

			@Override
			public void onLoadingMore() {

				new Thread() {
					public void run() {
						try {
							System.out.println("开始加载更多");
							Thread.sleep(3000);
							System.out.println("加载更多结束");

							handler.post(new Runnable() {

								@Override
								public void run() {
//									loadMoreFinished();
								}
							});
						} catch (InterruptedException e) {
						}
					};
				}.start();
			
			}
		});
	}

	private void initAnimation() {
		up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		up.setDuration(500);
		up.setFillAfter(true);

		down = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		down.setDuration(500);
		down.setFillAfter(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			// System.out.println("当前条目：" + getFirstVisiblePosition());
			// 当轮播图完全展示出来时，才处理下拉事件
			if (getFirstVisiblePosition() != 0) {
				break;
			}
			// 如果处于正在刷新状态，不处理事件
			if (CURRENT_STATE == REFRESHING) {
				break;
			}
			if (downY == -1) {
				downY = (int) ev.getY();
			}
			int moveY = (int) ev.getY();

			int diffY = moveY - downY;
			// 往下移动
			if (diffY > 0) {
				int paddingTop = diffY - headerHeight;
				// 下拉头没有完全显示
				if (paddingTop < 0 && CURRENT_STATE != PULLDOWN_REFRESH) {
					CURRENT_STATE = PULLDOWN_REFRESH;
					System.out.println("切到下拉刷新");
					changeRefreshState(CURRENT_STATE);
				}
				// 下拉头完全显示
				else if (paddingTop > 0 && CURRENT_STATE != RELEASE_REFRESH) {
					CURRENT_STATE = RELEASE_REFRESH;
					System.out.println("切到松开刷新");
					changeRefreshState(CURRENT_STATE);
				}

				header.setPadding(0, paddingTop, 0, 0);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			downY = -1;
			if (CURRENT_STATE == PULLDOWN_REFRESH) {
				header.setPadding(0, -headerHeight, 0, 0);
			} else if (CURRENT_STATE == RELEASE_REFRESH) {
				CURRENT_STATE = REFRESHING;
				System.out.println("切到正在刷新");
				changeRefreshState(CURRENT_STATE);
				header.setPadding(0, 0, 0, 0);
				if (mListener != null) {
					// 回调
					mListener.onRefreshing();
				}
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void changeRefreshState(int state) {
		switch (state) {
		case PULLDOWN_REFRESH:
			tv_refresh_state.setText("下拉刷新");
			pb_refresh_progress.setVisibility(View.INVISIBLE);
			iv_refresh_arrow.setVisibility(View.VISIBLE);
			iv_refresh_arrow.startAnimation(down);
			break;
		case RELEASE_REFRESH:
			tv_refresh_state.setText("松开刷新");
			iv_refresh_arrow.startAnimation(up);
			break;
		case REFRESHING:
			iv_refresh_arrow.clearAnimation();
			tv_refresh_state.setText("正在刷新");
			pb_refresh_progress.setVisibility(View.VISIBLE);
			iv_refresh_arrow.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
	}

	public void refreshFinished(boolean success) {
		CURRENT_STATE = PULLDOWN_REFRESH;
		tv_refresh_state.setText("下拉刷新");
		pb_refresh_progress.setVisibility(View.INVISIBLE);
		iv_refresh_arrow.setVisibility(View.VISIBLE);
		header.setPadding(0, -headerHeight, 0, 0);
		if (success) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String time = format.format(new Date()).toString();
			tv_refresh_time.setText("最后刷新时间：" + time);
		} else {
			Toast.makeText(getContext(), "网络错误，请检查网络配置", 0).show();
		}
	}

	public interface OnRefreshListener {
		void onRefreshing();
		void onLoadingMore();
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		this.mListener = listener;
	}
	
	public void loadMoreFinished(boolean success){
		// 恢复加载更多的状态 
		isLoadMore = false;
		footer.setPadding(0, -footerHeight, 0, 0);
		if(!success){
			Toast.makeText(getContext(), "没有更多数据了，亲", 0).show();
		}
	}
	private boolean isLoadMore;
	private int footerHeight;
	class MyOnScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// 监听空闲状态和惯性滑动状态
			if (OnScrollListener.SCROLL_STATE_IDLE == scrollState
					|| OnScrollListener.SCROLL_STATE_FLING == scrollState) {
				// 当前界面显示的最后一个条目，是否是Adapter里面的最后一个数据
				if(getLastVisiblePosition()==getCount()-1&&!isLoadMore){
					isLoadMore = true;
					// 显示底部加载更多布局
					System.out.println("加载更多了");
					footer.setPadding(0, 0, 0, 0);
					setSelection(getCount());
					// 回调外界加载更多的业务
					if(mListener!=null){
						mListener.onLoadingMore();
					}
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}

	}

}
