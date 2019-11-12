package com.mofluid.fragment_new;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.model_new.RespoonseCodeMap;
import com.mofluid.utility_new.Callback;
import com.mofluid.utility_new.L;

public class WebViewFragment extends Activity {
    final String TAG="WebViewFragment";
    public static Callback callback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        String url = getIntent().getStringExtra("url");
        L.d(TAG,"url="+url);
        WebView webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebClient());
        webView.loadUrl(url);
    }

    public class WebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url=request.getUrl().toString();
            L.d(TAG,"loaded url="+url);
            if(url.contains("cancel")) {
                callback.callback("", RespoonseCodeMap.PAYMENT_FAILED);
                gotoHome();
            }
            if(url.contains("success")) {
                callback.callback("",RespoonseCodeMap.PAYMENT_SUCCESS);
                gotoHome();
            }

            return super.shouldOverrideUrlLoading(view, request);
        }

        void gotoHome() {
            finish();
        }
    }
}

