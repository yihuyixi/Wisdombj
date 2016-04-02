package com.itheima.zhbj63.base.impl.menu;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.itheima.zhbj63.NewsDetailUI;
import com.itheima.zhbj63.R;
import com.itheima.zhbj63.base.MenuBasePager;
import com.itheima.zhbj63.domain.NewscenterBean.NewsTab;
import com.itheima.zhbj63.domain.TabDetailBean;
import com.itheima.zhbj63.domain.TabDetailBean.Data.News;
import com.itheima.zhbj63.domain.TabDetailBean.Data.Topnews;
import com.itheima.zhbj63.utils.BitmapHelp;
import com.itheima.zhbj63.utils.CacheUtils;
import com.itheima.zhbj63.utils.ConstantUtils;
import com.itheima.zhbj63.view.RefreshListview;
import com.itheima.zhbj63.view.RefreshListview.OnRefreshListener;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class TabDetailPager extends MenuBasePager {

	@ViewInject(R.id.vp_tabdetail_topimage)
	private ViewPager vp_tabdetail_topimage;// 顶部轮播图
	
	@ViewInject(R.id.tv_tabdetail_imageinfo)
	private TextView tv_tabdetail_imageinfo;// 图片描述信息
	
	@ViewInject(R.id.ll_tabdetail_points)
	private LinearLayout ll_tabdetail_points;// 轮播图下面点的容器
	
	@ViewInject(R.id.lv_tabdetail_news)
	private RefreshListview lv_tabdetail_news;// 新闻列表Listview

	private NewsTab mTabData;

	private String url;

	private List<Topnews> topnewsData;// 轮播图的数据

	private BitmapUtils bitmapUtils;// xutils中加载图片的工具
	
	private int preRedPointIndex;// 记录前一个点的位置

	private List<News> newsData;// 新闻列表的数据
	
	private static final String NEWS_ID_READED = "news_id_readed";
	
	public TabDetailPager(Context context, NewsTab newsTab) {
		super(context);
		this.mTabData = newsTab;
		url = ConstantUtils.BASE_URL + mTabData.url;
		bitmapUtils = BitmapHelp.getBitmapUtils(context);
	}

	@Override
	protected View initView() {
		View view = View.inflate(mContext, R.layout.tabdetail, null);
		ViewUtils.inject(this,view);
		// 加载轮播图布局
		View header = View.inflate(mContext, R.layout.topnews_header, null);
		ViewUtils.inject(this,header);
		
		// 让Listview添加轮播图
		lv_tabdetail_news.addHeaderView(header);
		
		// 监听正在刷新回调
		lv_tabdetail_news.setOnRefreshListener(new MyRefreshListener());
		vp_tabdetail_topimage.setOnPageChangeListener(new TopOnPageChangeListener());
		// 监听新闻列表条目点击事件
		lv_tabdetail_news.setOnItemClickListener(new MyOnItemClickListener());
		return view;
	}
	
	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
//			System.out.println("新闻条目position:" + position);
			// 以为加了两个头布局，需要-2
			int realPosition = position - 2;
			News news = newsData.get(realPosition);
			Intent intent = new Intent(mContext,NewsDetailUI.class);
			intent.putExtra("url", news.url);
			mContext.startActivity(intent);
			
			// 存储用户看过的新闻id
			// 先去已经存储过的id
			String readedId = CacheUtils.getString(mContext, NEWS_ID_READED, "");
			String newsId = String.valueOf(news.id);
			String tempId = "";
			// 当前新闻id没有存储过，才保存
			if(!readedId.contains(newsId)){
				tempId = readedId + "," + newsId;
				CacheUtils.putString(mContext, NEWS_ID_READED, tempId);
				adapter.notifyDataSetChanged();
			}
		}
		
	}
	
	@Override
	public void initData() {
		// 取缓存数据
		String cacheJson = CacheUtils.getString(mContext, url, "");
		if(!TextUtils.isEmpty(cacheJson)){
			processData(cacheJson);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println(mTabData.title + "访问数据成功：" + responseInfo.result);
				// 缓存新闻页签数据 url 作为key
				CacheUtils.putString(mContext, url, responseInfo.result);
				processData(responseInfo.result);
				if(isRefreshing){
					isRefreshing = false;
					lv_tabdetail_news.refreshFinished(true);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				if(isRefreshing){
					isRefreshing = false;
					lv_tabdetail_news.refreshFinished(false);
				}
			}
		});
	}

	protected void processData(String result) {
		Gson gson = new Gson();
		TabDetailBean tabDetailBean = gson.fromJson(result, TabDetailBean.class);
		// 取出加载更多的url
		moreUrl = tabDetailBean.data.more;
		if(!isLoadMore){
			
			// 更新顶部轮播图的数据
			topnewsData = tabDetailBean.data.topnews;
			vp_tabdetail_topimage.setAdapter(new TopPagerAdapter());
			// 填充轮播图下面的点
			ll_tabdetail_points.removeAllViews();
			for(int i=0;i<topnewsData.size();i++){
				View view = new View(mContext);
				view.setBackgroundResource(R.drawable.topnews_point_selector);
				LayoutParams params = new LayoutParams(5,5);
				params.leftMargin = 10;
				view.setLayoutParams(params);
				view.setEnabled(false);
				ll_tabdetail_points.addView(view);
			}
			// 重新初始化前一个点的位置
			preRedPointIndex = 0;
			// 初始化顶部轮播图的图片描述文字
			tv_tabdetail_imageinfo.setText(topnewsData.get(0).title);
			// 初始化红点
			ll_tabdetail_points.getChildAt(0).setEnabled(true);
			
			// 给新闻列表设置数据
			
			newsData = tabDetailBean.data.news;
			adapter = new NewsAadapter();
			lv_tabdetail_news.setAdapter(adapter);
		}
		// 加载更多
		else{
			// 添加更多数据
			newsData.addAll(tabDetailBean.data.news);
			// 通知Listview更新
			adapter.notifyDataSetChanged();
			
		}
		
		// 轮播图开始播放
		if(internalHandler==null){
			internalHandler = new InternalHandler();
		}
		// 移除掉上一次发送的消息
		internalHandler.removeCallbacksAndMessages(null);
		internalHandler.postDelayed(new InternalRunnable(), 4000);
	}
	
	class NewsAadapter extends BaseAdapter{

		@Override
		public int getCount() {
			return newsData.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NewsHolder holder = null;
			// 复用convertView
			if(convertView==null){
				convertView = View.inflate(mContext, R.layout.tabdetail_item, null);
				holder = new NewsHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.iv_newsitem_photo);
				holder.title = (TextView) convertView.findViewById(R.id.tv_newsitem_title);
				holder.time = (TextView) convertView.findViewById(R.id.tv_newsitem_time);
				convertView.setTag(holder);
			}else{
				holder = (NewsHolder) convertView.getTag();
			}
			News news = newsData.get(position);
			
			// 根据存储的已读新闻id，修改文字颜色
			String readedId = CacheUtils.getString(mContext, NEWS_ID_READED, "");
			if(readedId.contains(String.valueOf(news.id))){
				// 已读
				holder.title.setTextColor(Color.RED);
			}else{
				// 未读
				holder.title.setTextColor(Color.BLACK);
			}
			
			// 更新数据
			bitmapUtils.display(holder.image, news.listimage);
			holder.title.setText(news.title);
			holder.time.setText(news.pubdate);
			
			return convertView;
		}
		
	}
	
	class NewsHolder {
		public ImageView image;
		public TextView title;
		public TextView time;
	}
	
	class TopOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			
		}

		@Override
		public void onPageSelected(int position) {
			// 设置图片描述文字
			tv_tabdetail_imageinfo.setText(topnewsData.get(position).title);
			// 把前一个点设置为白色
			ll_tabdetail_points.getChildAt(preRedPointIndex).setEnabled(false);
			// 改变红点
			ll_tabdetail_points.getChildAt(position).setEnabled(true);
			preRedPointIndex = position;
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			
		}
		
	}
	
	class TopPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return topnewsData.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mContext);
			container.addView(imageView);
			imageView.setScaleType(ScaleType.FIT_XY);
			// 加载图片
			bitmapUtils.display(imageView, topnewsData.get(position).topimage);
			
			imageView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// 手指按下，停止轮播
						System.out.println("手指按下，停止轮播");
						internalHandler.removeCallbacksAndMessages(null);// 移除所有的任务和消息
						break;
					case MotionEvent.ACTION_UP:
						// 手指抬起，继续播放
						System.out.println("手指抬起，继续播放");
						internalHandler.postDelayed(new InternalRunnable(), 4000);
						break;
					case MotionEvent.ACTION_CANCEL:
						// 事件取消，继续播放
						System.out.println("事件取消，继续播放");
						internalHandler.postDelayed(new InternalRunnable(), 4000);
						break;

					default:
						break;
					}
					return true;// 必须return true，自己消费事件
				}
			});
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		
	}
	private boolean isRefreshing;

	private String moreUrl;
	class MyRefreshListener implements OnRefreshListener{

		@Override
		public void onRefreshing() {
			isRefreshing = true;
			getDataFromServer();
		}

		@Override
		public void onLoadingMore() {
			if(!TextUtils.isEmpty(moreUrl)){
				isLoadMore = true;
				loadMoreFromServer();
			}else{
				lv_tabdetail_news.loadMoreFinished(false);
			}
		}
		
	}
	private boolean isLoadMore;

	private NewsAadapter adapter;

	private InternalHandler internalHandler;
	private void loadMoreFromServer(){

		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, ConstantUtils.BASE_URL + moreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println(mTabData.title + "加载更多数据成功：" + responseInfo.result);
				processData(responseInfo.result);
				isLoadMore = false;
				lv_tabdetail_news.loadMoreFinished(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println(mTabData.title + "加载更多数据失败");
				lv_tabdetail_news.loadMoreFinished(false);
				isLoadMore = false;
			}
		});
	
	}
	
	class InternalRunnable implements Runnable{

		@Override
		public void run() {
			internalHandler.sendEmptyMessage(0);
		}
		
	}
	
	class InternalHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// 跳到下一张图片
			int nextIndex = (vp_tabdetail_topimage.getCurrentItem()+1)%topnewsData.size();
			vp_tabdetail_topimage.setCurrentItem(nextIndex);
			// 开始循环
			internalHandler.postDelayed(new InternalRunnable(), 4000);
		}
	}

}
