package com.itheima.zhbj63;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailUI extends Activity implements OnClickListener {
	private ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newsdetail_ui);
		
		init();
	}

	private void init() {
		WebView webView = (WebView) findViewById(R.id.wv_newsdetail_web);
		progress = (ProgressBar) findViewById(R.id.pb_newsdetail_progress);
		TextView title = (TextView) findViewById(R.id.tv_basepager_title);
		title.setVisibility(View.INVISIBLE);
		ImageButton back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		back.setVisibility(View.VISIBLE);
		ImageButton textsize = (ImageButton) findViewById(R.id.ib_titlebar_textsize);
		textsize.setVisibility(View.VISIBLE);
		ImageButton share = (ImageButton) findViewById(R.id.ib_titlebar_share);
		share.setVisibility(View.VISIBLE);
		
		back.setOnClickListener(this);
		textsize.setOnClickListener(this);
		share.setOnClickListener(this);
		
		// 监听Webview加载完成事件
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				progress.setVisibility(View.GONE);
			}
		});
		// 获取Webview的配置信息
		
		settings = webView.getSettings();
		settings.setBuiltInZoomControls(true);// 显示一组缩放按钮
		settings.setUseWideViewPort(true);// 启用双击放大缩小功能
		settings.setJavaScriptEnabled(true);// 启用JavaScript
		
		// 取传递过来的url
		String url = getIntent().getStringExtra("url");
		// 加载url
		webView.loadUrl(url);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finish();
			break;
		case R.id.ib_titlebar_share:
			showShare();
			break;
		case R.id.ib_titlebar_textsize:
			textsize();
			break;

		default:
			break;
		}
	}
	private int currentIndex = 2;
	private WebSettings settings;
	private int tempIndex; 
	private void textsize() {
		Builder builder = new Builder(this);
		String [] items = new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
		builder.setSingleChoiceItems(items, currentIndex, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				tempIndex = which;
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				currentIndex = tempIndex;
				changeTextsize(currentIndex);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	protected void changeTextsize(int index) {
		switch (index) {
		case 0:
			settings.setTextSize(TextSize.LARGEST);
			break;
		case 1:
			settings.setTextSize(TextSize.LARGER);
			break;
		case 2:
			settings.setTextSize(TextSize.NORMAL);
			break;
		case 3:
			settings.setTextSize(TextSize.SMALLER);
			break;
		case 4:
			settings.setTextSize(TextSize.SMALLEST);
			break;

		default:
			break;
		}
	}
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("学习累不");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 oks.setImagePath("/sdcard/test2.jpg");//确保SDcard下面存在此张图片

		// 启动分享GUI
		 oks.show(this);
		 }
}
