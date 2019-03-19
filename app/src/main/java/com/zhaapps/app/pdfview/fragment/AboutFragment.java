package com.zhaapps.app.pdfview.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.zhaapps.app.pdfview.R;


public class AboutFragment extends android.support.v4.app.Fragment {

    private Toolbar toolbar;
    public WebView mWebView;
    private ProgressBar progressBar;
    private LinearLayout layoutProgress;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /** Inflating the layout for this fragment **/

        View v = inflater.inflate(R.layout.webpage, null);

        mWebView = (WebView) v.findViewById(R.id.webpage);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        layoutProgress = (LinearLayout) v.findViewById(R.id.layoutProgress);
        mWebView.setVisibility(View.GONE);


        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAppCacheMaxSize(7 * 1024 * 1024); // 7MB
        webSettings.setAppCachePath(getActivity().getCacheDir().getAbsolutePath());
        webSettings.setAllowFileAccess(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.setVisibility(View.VISIBLE);
                layoutProgress.setVisibility(View.GONE);
                progressBar.setIndeterminate(false);
                super.onPageFinished(view, url);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                layoutProgress.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                super.onPageStarted(view, url, favicon);
            }
        });
        mWebView.loadUrl(getString(R.string.website));
        return v;

    }

}
