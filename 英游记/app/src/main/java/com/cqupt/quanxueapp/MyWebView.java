package com.cqupt.quanxueapp;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Map;

public class MyWebView extends android.webkit.WebView {
    private boolean isDestroy;

    public MyWebView(@NonNull Context context) {
        super(context);
    }

    @Override
    public void destroy() {
        this.isDestroy = true;
        super.destroy();
    }
    @Override
    public void loadUrl(String url, Map<String, String> extraHeaders) {
        if(this.isDestroy){
            return ;
        }
        super.loadUrl(url, extraHeaders);
    }
}
