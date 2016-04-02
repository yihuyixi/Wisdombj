package com.itheima.zhbj63.fragment;

import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.zhbj63.MainUI;
import com.itheima.zhbj63.R;
import com.itheima.zhbj63.base.BaseFragment;
import com.itheima.zhbj63.base.impl.NewscenterPager;
import com.itheima.zhbj63.domain.NewscenterBean.MenuItem;

public class LeftFragment extends BaseFragment {

	private List<MenuItem> mMenuData;
	private ListView listView;
	
	private int currentClickItem;// 当前点击的条目索引
	private MyAdapter adapter;
	private MainUI mainUI;

	@Override
	protected View initView() {
		listView = new ListView(mActivity);
		listView.setBackgroundColor(Color.BLACK);// 修改背景颜色
		listView.setCacheColorHint(Color.TRANSPARENT);// 设置滑动背景颜色
		listView.setDividerHeight(0);// 设置没有间隔线
		listView.setSelector(android.R.color.transparent);// 设置条目选择器
		listView.setPadding(0, 40, 0, 0);// Listview往下移动
		// 监听条目点击事件
		listView.setOnItemClickListener(new MyOnItemClickListener());
		mainUI = (MainUI) mActivity;
		return listView;
	}

	public void setMenuData(List<MenuItem> menuData) {
		// 初始化数据
		this.mMenuData = menuData;
		// 设置Adapter
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		
		// 默认显示新闻详情界面
		switchMenuPager(0);
	}
	
	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			currentClickItem = position;
			adapter.notifyDataSetChanged();
			mainUI.getSlidingMenu().toggle();// 自动弹出，收回菜单
			switchMenuPager(position);
		}
		
	}
	private void switchMenuPager(int position){
		ContentFragment contentFragment = mainUI.getContentFragment();
		NewscenterPager newscenterPager = contentFragment.getNewscenterPager();
		newscenterPager.switchMenuPager(position);
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mMenuData.size();
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
			TextView view = (TextView) View.inflate(mActivity, R.layout.left_menu_item, null);
			view.setText(mMenuData.get(position).title);
			view.setEnabled(currentClickItem==position);
			return view;
		}
		
	}
	
}
