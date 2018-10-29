package com.charles.www.testDemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.charles.www.testDemo.BuildConfig;
import com.charles.www.testDemo.R;
import com.charles.www.testDemo.view.X5WebView;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebVeiwActivity extends AppCompatActivity {

    @BindView(R.id.webview) X5WebView mWebView;
    private String url = "https://ocj-test7.ocj.com.cn/groupIndex.html";
//    private String url_home = "https://ocj-test5.ocj.com.cn/shopllMobile/shopllMobileappIndex.html";
    private String url_home = "https://m.ocj.com.cn/image_site/event/2018/08/everydayflash/undefined#/";
    private String url_video = "http://m.ocj.com.cn/tvLive01";
//    private String url_home = "https://m.ocj.com.cn/common/event/jianjinkuang";
//    private String url_video = "https://m.ocj.com.cn/event/g/9661";
    private String ua = "OCJ_A1Japp_log Mozilla/5.0 (Linux; U; Android 4.3; zh-cn; C6802 Build/14.2.A.0.290) AppleWebKit/534.30 (KHTML, like Gecko) Android Mobile[355066062891876#f0:25:b7:b3:15:69#SM-G9008V#6.0.1#WIFIMAC:f0:25:b7:b3:15:69]app_reconsitution";

    // webview全屏显示设置
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessages;
    private static final int FILECHOOSER_RESULTCODE = 11;
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private IX5WebChromeClient.CustomViewCallback customViewCallback;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_veiw);
        ButterKnife.bind(this);
        initHardwareAccelerate();
        initView();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        mContext = this;
    }


    /**
     * 启用硬件加速
     */
    private void initHardwareAccelerate() {
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }
    }

    private void initView() {
        mWebView = findViewById(R.id.webview);
        initWebViewSettings();
        mWebView.loadUrl(url_home);
    }

    private void initWebViewSettings() {
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
//        webSetting.setUserAgentString("OCJ_A1JKapp_log Mozilla/5.0 (Linux; U; zh-cn; C6802 Build/14.2.A.0.290) AppleWebKit/534.30 (KHTML, like Gecko) Android Mobile[355066062891876#f0:25:b7:b3:15:69#SM-G9008V#6.0.1#WIFIMAC:f0:25:b7:b3:15:69]app_reconsitution");
//        webSetting.setUserAgentString("OCJ_A1app_log Mozilla/5.0 (Linux; U; Android 4.3; zh-cn; C6802 Build/14.2.A.0.290) AppleWebKit/534.30 (KHTML, like Gecko) Android Mobile[863673039588777#02:00:00:00:00:00#Redmi 4X#7.1.2#18071adc0331cf6eb94]");
        webSetting.setUserAgentString(ua);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
        mWebView.setWebViewClient(new WebViewClientNew());
//        mWebView.setLayerType();
        mWebView.setDrawingCacheEnabled(true);
        mWebView.setWebChromeClient(new OCJWebChromeClient());
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }


    class WebViewClientNew extends WebViewClient{
        @Override
        public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
            super.onPageStarted(webView, url, bitmap);

        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {

//            Intent intent = new Intent();
//            intent.setClass(mContext,GoodsDetailsActivity.class);
//            webView.getContext().startActivity(intent);
            webView.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {

            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);

            Log.v("reallyUrl:",url);
        }
    }


    class OCJWebChromeClient extends WebChromeClient {
        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            if (mUploadMessage != null)
                return;
            mUploadMessage = uploadMsg;
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            openFileChooser(uploadMsg);
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooser(uploadMsg, acceptType);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, com.tencent.smtt.sdk.ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
            }
            mUploadMessages = valueCallback;
//            Log.v("reactmanager",mUploadMessages.toString());
            setmUploadMessages(mUploadMessages);
            setmUploadMessage(mUploadMessage);
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] type = fileChooserParams.getAcceptTypes();
            if (type != null && type.length > 0) {
                contentSelectionIntent.setType("*/*");
                contentSelectionIntent.putExtra(Intent.EXTRA_MIME_TYPES, type);
            } else {
                contentSelectionIntent.setType("image/*");
            }
            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

            startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

            return true;
        }

        @Override
        public boolean onConsoleMessage(com.tencent.smtt.export.external.interfaces.ConsoleMessage consoleMessage) {
            if (BuildConfig.DEBUG) {
                return super.onConsoleMessage(consoleMessage);
            }
            // Ignore console logs in non debug builds.
            return true;
        }


        @Override
        public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
            geolocationPermissionsCallback.invoke(s, true, false);
        }





        @Override
        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout(WebVeiwActivity.this);
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            return frameLayout;
        }

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
            showCustomView(view, callback);
        }

        @Override
        public void onHideCustomView() {
            hideCustomView();
        }

        @Override
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
        }
        //        @Override
//        public void onProgressChanged(android.webkit.WebView view, int newProgress) {
//            super.onProgressChanged(view, newProgress);
////            dispatchEvent(view,new ProgressEvent(view.getId(),newProgress));
//        }
    }


    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(WebVeiwActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        customViewCallback = callback;
    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        mWebView.setVisibility(View.VISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public  void setmUploadMessages(ValueCallback<Uri[]> uploadMessages) {
        mUploadMessages = uploadMessages;
    }

    /**
     * 解决从OcjwebView当中上传图片，传递信息
     *
     * @param uploadMessage
     */
    public  void setmUploadMessage(ValueCallback<Uri> uploadMessage) {
        mUploadMessage = uploadMessage;
    }

// 用来计算返回键的点击间隔时间
  private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
            mWebView.goBack();
            return true;
        }else {
            if (System.currentTimeMillis() - exitTime < 2000){
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出webview", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                finish();
            }
           return true;
        }
    }
}
