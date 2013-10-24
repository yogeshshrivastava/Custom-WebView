package com.example.customwebview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class WebViewActivity extends Activity {
	
	private WebView mWebView;
	private ImageView mCloseUrl;
	private EditText mUrlEditText;
	private Button mLoadUrl;
	private ProgressDialog mProgDailog; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		getLayout();
		setViewListeners();
		init();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState ) {
		super.onSaveInstanceState(outState);
		mWebView.saveState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		mWebView.restoreState(savedInstanceState);
	}
	
	private void getLayout() {
		mWebView = (WebView) findViewById(R.id.webview);
		mCloseUrl = (ImageView) findViewById(R.id.close_url);
		mUrlEditText = (EditText) findViewById(R.id.url);
		mLoadUrl = (Button) findViewById(R.id.load_url_and_hide);
		mProgDailog = new ProgressDialog(this);
	}
	
	private void setViewListeners() {
		mLoadUrl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = mUrlEditText.getText().toString();
				if(!TextUtils.isEmpty(url)) {
					mWebView.loadUrl(url);
					mLoadUrl.setVisibility(View.GONE);
					mUrlEditText.setVisibility(View.GONE);
					mCloseUrl.setVisibility(View.GONE);
				}
			}
		});
		
		mCloseUrl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mLoadUrl.getVisibility() == View.VISIBLE) {
					mLoadUrl.setVisibility(View.GONE);
					mUrlEditText.setVisibility(View.GONE);
					mCloseUrl.setVisibility(View.GONE);
				}
			}
		});
		
		mProgDailog.setMessage("Loading...");
		mProgDailog.setCancelable(true);
	}
	
	private void init() {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);	
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setUseWideViewPort(true);
		mWebView.setWebViewClient(new WebViewClient(){
			 @Override
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if(mProgDailog != null && !mProgDailog.isShowing()) {
						mProgDailog.show();
		            }   
				    view.loadUrl(url);
	                return true;                
	            }
			 
	            @Override
	            public void onPageFinished(WebView view, final String url) {
	            	if(mProgDailog != null && mProgDailog.isShowing()) {
	            		mProgDailog.dismiss();
	            	}
	            }
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_settings:
			
			if(mLoadUrl.getVisibility() == View.GONE) {
				mLoadUrl.setVisibility(View.VISIBLE);
				mUrlEditText.setVisibility(View.VISIBLE);
				mCloseUrl.setVisibility(View.VISIBLE);
			}
			else {
				mLoadUrl.setVisibility(View.GONE);
				mUrlEditText.setVisibility(View.GONE);
				mCloseUrl.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // Check if the key event was the Back button and if there's history
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
	        mWebView.goBack();
	        return true;
	    }
	    
	    if (event.getKeyCode() == KeyEvent.FLAG_EDITOR_ACTION) {
	    	mLoadUrl.performClick();
	    	return true;
	    }
	    // If it wasn't the Back key or there's no web page history, bubble up to the default
	    // system behavior (probably exit the activity)
	    return super.onKeyDown(keyCode, event);
	}

}
