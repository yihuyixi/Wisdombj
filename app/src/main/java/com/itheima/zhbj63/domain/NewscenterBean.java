package com.itheima.zhbj63.domain;

import java.util.List;

public class NewscenterBean {
	public List<MenuItem> data;
	public List<Integer> extend;
	public int retcode;
	
	public class MenuItem{
		public List<NewsTab> children;
		public int id;
		public String title;
		public int type;
		public String url;
		public String url1;
		public String dayurl;
		public String excurl;
		public String weekurl;
		@Override
		public String toString() {
			return "MenuItem [children=" + children + ", id=" + id + ", title="
					+ title + ", type=" + type + ", url=" + url + ", url1="
					+ url1 + ", dayurl=" + dayurl + ", excurl=" + excurl
					+ ", weekurl=" + weekurl + "]";
		}
		
	}
	
	public class NewsTab{
		public int id;
		public String title;
		public int type;
		public String url;
		@Override
		public String toString() {
			return "NewsTab [id=" + id + ", title=" + title + ", type=" + type
					+ ", url=" + url + "]";
		}
		
	}

	@Override
	public String toString() {
		return "NewscenterBean [data=" + data + ", extend=" + extend
				+ ", retcode=" + retcode + "]";
	}
	
}
