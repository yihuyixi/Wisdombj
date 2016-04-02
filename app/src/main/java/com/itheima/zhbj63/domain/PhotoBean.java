package com.itheima.zhbj63.domain;

import java.util.List;

public class PhotoBean {

	public int retcode;
	public Data data;

	public class Data {

		public String more;
		public String countcommenturl;
		public String title;
		public List<News> news;
		public List<Topic> topic;

	}
	public class News {
		public String commentlist;
		public String largeimage;
		public String commenturl;
		public Boolean comment;
		public int id;
		public String title;
		public String type;
		public String listimage;
		public String smallimage;
		public String url;
		public String pubdate;
	}
	public class Topic {
	}
}