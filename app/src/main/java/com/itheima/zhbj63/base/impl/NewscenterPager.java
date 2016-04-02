package com.itheima.zhbj63.base.impl;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.Gson;
import com.itheima.zhbj63.MainUI;
import com.itheima.zhbj63.base.BasePager;
import com.itheima.zhbj63.base.MenuBasePager;
import com.itheima.zhbj63.base.impl.menu.InterractDetailPager;
import com.itheima.zhbj63.base.impl.menu.NewsDetailPager;
import com.itheima.zhbj63.base.impl.menu.PhotoDetailPager;
import com.itheima.zhbj63.base.impl.menu.TopicDetailPager;
import com.itheima.zhbj63.domain.NewscenterBean;
import com.itheima.zhbj63.domain.NewscenterBean.MenuItem;
import com.itheima.zhbj63.fragment.LeftFragment;
import com.itheima.zhbj63.utils.CacheUtils;
import com.itheima.zhbj63.utils.ConstantUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class NewscenterPager extends BasePager{
	
	private List<MenuBasePager> menuPagers;
	private final String NEWSCENTER_CACHE_JSON = "newscenter_cache_json";
	private List<MenuItem> menuData;

	public NewscenterPager(Context context) {
		super(context);
	}
	
	@Override
	public void initData() {
		System.out.println("新闻中心加载了");
		ib_basepager_menu.setVisibility(View.VISIBLE);
		tv_basepager_title.setText("新闻中心");
		
		// 取缓存数据，先展示
		String cacheJson = CacheUtils.getString(mContext, NEWSCENTER_CACHE_JSON, "");
		if(!TextUtils.isEmpty(cacheJson)){
			// 展示缓存数据
			processData(cacheJson);
		}
		
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		// T 服务器返回数据的类型，json就是String
		httpUtils.send(HttpMethod.GET, ConstantUtils.NEWSCENTER_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("访问新闻中心成功：" + responseInfo.result);
				// 缓存新闻中心json数据
				CacheUtils.putString(mContext, NEWSCENTER_CACHE_JSON, responseInfo.result);
				processData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("访问新闻中心失败：");
			}
		});
	}

	protected void processData(String result) {
		// 解析json
		Gson gson = new Gson();
		NewscenterBean newscenterBean = gson.fromJson(result, NewscenterBean.class);
//		System.out.println("json解析结果：" + newscenterBean);
		// 左侧菜单条目数据
		
		menuData = newscenterBean.data;
		// 把上下文对象强转为MainUI
		MainUI mainUI = (MainUI) mContext;
		LeftFragment leftFragment = mainUI.getLeftFragment();
		// 初始化左侧菜单对应的对象
		menuPagers = new ArrayList<MenuBasePager>();
		menuPagers.add(new NewsDetailPager(mContext,menuData.get(0).children));
		menuPagers.add(new TopicDetailPager(mContext));
		menuPagers.add(new PhotoDetailPager(mContext));
		menuPagers.add(new InterractDetailPager(mContext));
		
		// 把菜单数据传递给左侧菜单对象
		leftFragment.setMenuData(menuData);
		
	}

	public void switchMenuPager(final int position) {
		// 根据左侧菜单索引修改标题
		tv_basepager_title.setText(menuData.get(position).title);
		MenuBasePager menuBasePager = menuPagers.get(position);
		container.removeAllViews();
		container.addView(menuBasePager.rootView);
		menuBasePager.initData();
		
		if(position==2){
			ib_titlebar_photo.setVisibility(View.VISIBLE);
			ib_titlebar_photo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PhotoDetailPager pager = (PhotoDetailPager) menuPagers.get(position);
					pager.changePhotoType(ib_titlebar_photo);
				}
			});
		}else{
			ib_titlebar_photo.setVisibility(View.INVISIBLE);
		}
	}
}
