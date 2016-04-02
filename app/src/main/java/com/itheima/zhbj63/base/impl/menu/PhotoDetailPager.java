package com.itheima.zhbj63.base.impl.menu;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.itheima.zhbj63.R;
import com.itheima.zhbj63.base.MenuBasePager;
import com.itheima.zhbj63.domain.PhotoBean;
import com.itheima.zhbj63.domain.PhotoBean.News;
import com.itheima.zhbj63.utils.BitmapCacheUtils;
import com.itheima.zhbj63.utils.BitmapHelp;
import com.itheima.zhbj63.utils.ConstantUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PhotoDetailPager extends MenuBasePager {
	
	@ViewInject(R.id.lv_photos_list)
	private ListView listview;
	
	@ViewInject(R.id.gv_photos_grid)
	private GridView gridView;

	private List<News> photoData;

	private BitmapUtils bitmapUtils;

	public PhotoDetailPager(Context context) {
		super(context);
		bitmapCacheUtils = new BitmapCacheUtils();
	}

	@Override
	protected View initView() {
		View view = View.inflate(mContext, R.layout.photos, null);
		ViewUtils.inject(this,view);
		return view;
	}
	
	@Override
	public void initData() {
		
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, ConstantUtils.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("组图数据访问成功：" + responseInfo.result);
				processData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("组图数据访问失败");
			}
		});
	}

	protected void processData(String result) {
		Gson gson = new Gson();
		PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);
		photoData = photoBean.data.news;
		listview.setAdapter(new PhotoAdapter());
	}
	
	class PhotoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return photoData.size();
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
			PhotoHolder holder = null;
			if(convertView==null){
				convertView = View.inflate(mContext, R.layout.photo_item, null);
				holder = new PhotoHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.iv_photoitem_photo);
				holder.title = (TextView) convertView.findViewById(R.id.tv_photoitem_title);
				convertView.setTag(holder);
			}else{
				holder = (PhotoHolder) convertView.getTag();
			}
			// 给ImageView设置positon，为了线程下载时，对应上每一个ImageView
			holder.image.setTag(position);
			News news = photoData.get(position);
//			bitmapUtils.display(holder.image, news.listimage);
			bitmapCacheUtils.display(holder.image, news.listimage,listview);
			holder.title.setText(news.title);
			return convertView;
		}
		
	}
	
	class PhotoHolder{
		public ImageView image;
		public TextView title;
	}
	private boolean isListType = true;// 初始时，显示Listview

	private BitmapCacheUtils bitmapCacheUtils;
	public void changePhotoType(ImageButton image) {
		if(isListType){
			// 切换到Gridview
			isListType = false;
			image.setBackgroundResource(R.drawable.icon_pic_list_type);
			listview.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);
			gridView.setAdapter(new PhotoAdapter());
		}else{
			// 切换到Listview
			isListType = true;
			image.setBackgroundResource(R.drawable.icon_pic_grid_type);
			listview.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.INVISIBLE);
			listview.setAdapter(new PhotoAdapter());
		}
	}

}
