package com.itheima.zhbj63.domain;

import java.lang.reflect.Field;
import java.util.List;

public class TabDetailBean {

	public int retcode;
	public Data data;

	public class Data {

		public String more;
		public String countcommenturl;
		public String title;
		public List<News> news;
		public List<Topnews> topnews;
		public List<Topic> topic;


		public class News {

			public String commentlist;
			public String commenturl;
			public Boolean comment;
			public int id;
			public String title;
			public String type;
			public String listimage;
			public String url;
			public String pubdate;

			@Override
			public String toString() {
				String s = "";
				Field[] arr = this.getClass().getFields();
				for (Field f : getClass().getFields()) {
					try {
						s += f.getName() + "=" + f.get(this) + "\n,";
					} catch (Exception e) {
					}
				}
				return getClass().getSimpleName()
						+ "["
						+ (arr.length == 0 ? s : s.substring(0, s.length() - 1))
						+ "]";
			}
		}



		public class Topnews {

			public String commentlist;
			public String topimage;
			public String commenturl;
			public Boolean comment;
			public int id;
			public String title;
			public String type;
			public String url;
			public String pubdate;

			@Override
			public String toString() {
				String s = "";
				Field[] arr = this.getClass().getFields();
				for (Field f : getClass().getFields()) {
					try {
						s += f.getName() + "=" + f.get(this) + "\n,";
					} catch (Exception e) {
					}
				}
				return getClass().getSimpleName()
						+ "["
						+ (arr.length == 0 ? s : s.substring(0, s.length() - 1))
						+ "]";
			}
		}

		public class Topic {

			public String description;
			public int id;
			public int sort;
			public String title;
			public String listimage;
			public String url;

			@Override
			public String toString() {
				String s = "";
				Field[] arr = this.getClass().getFields();
				for (Field f : getClass().getFields()) {
					try {
						s += f.getName() + "=" + f.get(this) + "\n,";
					} catch (Exception e) {
					}
				}
				return getClass().getSimpleName()
						+ "["
						+ (arr.length == 0 ? s : s.substring(0, s.length() - 1))
						+ "]";
			}
		}

		@Override
		public String toString() {
			String s = "";
			Field[] arr = this.getClass().getFields();
			for (Field f : getClass().getFields()) {
				try {
					s += f.getName() + "=" + f.get(this) + "\n,";
				} catch (Exception e) {
				}
			}
			return getClass().getSimpleName() + "["
					+ (arr.length == 0 ? s : s.substring(0, s.length() - 1))
					+ "]";
		}
	}

	@Override
	public String toString() {
		String s = "";
		Field[] arr = this.getClass().getFields();
		for (Field f : getClass().getFields()) {
			try {
				s += f.getName() + "=" + f.get(this) + "\n,";
			} catch (Exception e) {
			}
		}
		return getClass().getSimpleName() + "["
				+ (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
	}
}