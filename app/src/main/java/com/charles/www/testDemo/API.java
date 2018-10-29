package com.charles.www.testDemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.charles.httpsdk.novate.Novate;
import com.charles.httpsdk.novate.NovateHttpsFactroy;
import com.charles.httpsdk.novate.conver.DefineConverter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.Interceptor;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;


/**
 * Created by Administrator on 2018/2/13.
 */

public class API extends Application {
    private static Novate mNovate;
    private static Map<String, String> mHeader = new HashMap<>();
    private static Map<String, String> mParames = new HashMap<>();
    private static Interceptor mInterceptor;
    private static File mFile;
    private static Cache mCache;
    private static Context mContext;
    private static int[] certificates = {R.raw.ca};

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        getmNovate();

//        initX5WebView();
    }

    // 初始化noval参数
    public static Novate getmNovate() {
        mHeader.put("Accept", "application/json");
        mHeader.put("X-access-token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDE4MDMzMjExNzQiLCJpc3MiOiJvY2otc3RhcnNreSIsImxvZ2lkIjoiNjQxODQ0MjcwOTc5ODc1MjI1NiIsImV4cCI6MTUzODA1MTk5MCwiaWF0IjoxNTMwMjc1OTkwLCJkZXZpY2VpZCI6IiJ9.EKxba8VdlotGH7Ta9kDscyB33n2IW4kAUdKXNWGVdaY");
//        mHeader.put("Content-type","application/x-www-form-urlencoded;charset=UTF-8");
//        mHeader.put("Content-type","application/x-www-form-urlencoded");
//        mHeader.put("Accept-Encoding", "gzip, deflate");
        mNovate = null;
//        mHeader.put("Content-type", "application/json");
//        if (mNovate == null) {
//            mFile = new File(mContext.getExternalCacheDir(), "cache");
//            mCache = new Cache(mFile, 10 * 1024);
//            mNovate = new Novate.Builder(getmContext())
//                    .addHeader(mHeader)
////                        .addApiMapList(PATH.initAPIMapList.getMap())
////                        .addParameters(mParames)
//                    .connectTimeout(20)
////                        .addCookie(true)
////                        .addCache(true)
////                        .addCache(mCache, 10*60)
//                    .baseUrl(PATH.BASE_URL)
//                    .addLog(true)
////                        .addConverterFactory(DefineConverter.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .addSSLSocketFactory(NovateHttpsFactroy.getSSLSocketFactory())
//                    .skipSSLSocketFactory(false)
//                    .addCache(false)
////                        .addInterceptor()  // 不需要拦截的时候则不用设置
////                        .addNetworkInterceptor()
////                        .proxy()
////                        .client()
//                    .build();
//        }
        return new Novate.Builder(getmContext())
                .addHeader(mHeader)
                        .addApiMapList(PATH.initAPIMapList.getMap())
                        .addParameters(mParames)
                .connectTimeout(30)  //连接时间 可以忽略
                .writeTimeout(30)
//                        .addCookie(true)
//                        .addCache(true)
//                        .addCache(mCache, 10*60)
                .baseUrl(PATH.BASE_URL)
                .addLog(true)
//                        .addConverterFactory(DefineConverter.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addSSLSocketFactory(NovateHttpsFactroy.getSSLSocketFactory())
                .skipSSLSocketFactory(false)
                .addCache(false)
//                        .addInterceptor()  // 不需要拦截的时候则不用设置
//                        .addNetworkInterceptor()
//                        .proxy()
//                        .client()
                .build();
    }

    // 初始化noval参数
    public static Novate getmNovate(String baseUrl) {
        mNovate = null;
        mHeader.put("Accept", "application/json");
        mHeader.put("X-access-token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDE4MDMzMjExNzQiLCJpc3MiOiJvY2otc3RhcnNreSIsImxvZ2lkIjoiNjQwMzA5ODkwMTIwODg5NTQ4OCIsImV4cCI6MTUzNDM5Mzc0MSwiaWF0IjoxNTI2NjE3NzQxLCJkZXZpY2VpZCI6IiJ9.D2HVl4kS7LRGdhsd5ZM8ly3RZFebRYqk9G7EnkREzig");
        mHeader.put("Content-type", "application/json");
//        mHeader.put("Content-type","application/x-www-form-urlencoded;charset=UTF-8");
//        mHeader.put("Accept-Encoding", "gzip, deflate");
        if (mNovate == null) {
            mNovate = new Novate.Builder(getmContext())
                    .addHeader(mHeader)
//            .addApiMapList(PATH.initAPIMapList.getMap())
                    .connectTimeout(20)
                    .baseUrl(baseUrl)
                    .addLog(true)
                    .addConverterFactory(DefineConverter.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//            .addSSLSocketFactory(NovateHttpsFactroy.getSSLSocketFactory(mContext, certificates))
                    .addSSLSocketFactory(NovateHttpsFactroy.getSSLSocketFactory())
                    .skipSSLSocketFactory(true)
                    .build();
        }
        return mNovate;
    }

    public static Context getmContext() {
        return mContext;
    }


    /**
     * eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDE4MDMyOTQxNjQiLCJpc3MiOiJvY2otc3RhcnNreSIsImxvZ2lkIjoiNjM3OTE0Mjg0MTgxMzE3NjMyMCIsImV4cCI6MTUyODY4MjE3MiwiaWF0IjoxNTIwOTA2MTcyLCJkZXZpY2VpZCI6IiJ9.LvriJ3DOd9A-Dxst3eWiCiSy8ZU8b194334jlF-JXW4
     * <p>
     * eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMDE3MDg4NTE1OTMiLCJpc3MiOiJvY2otc3RhcnNreSIsImxvZ2lkIjoiNjM3OTE0MzcyMzY0ODgxNTEwNCIsImV4cCI6MTUyODY4MjM4MiwiaWF0IjoxNTIwOTA2MzgyLCJkZXZpY2VpZCI6IiJ9.uTHsjvL8Tmt_uO-4zGH8-ZOi8eaccfJv09UHr_E2Ju8
     */

    private void initX5WebView() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    // 实现绑定生命周期的接口



}
