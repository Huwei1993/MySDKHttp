/*
 *    Copyright (C) 2016 Tamic
 *
 *    link :https://github.com/Tamicer/Novate
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.charles.httpsdk.novate;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.charles.httpsdk.BuildConfig;
import com.charles.httpsdk.novate.cache.CookieCacheImpl;
import com.charles.httpsdk.novate.callback.ResponseCallback;
import com.charles.httpsdk.novate.config.ConfigLoader;
import com.charles.httpsdk.novate.cookie.NovateCookieManager;
import com.charles.httpsdk.novate.cookie.SharedPrefsCookiePersistor;
import com.charles.httpsdk.novate.download.DownLoadCallBack;
import com.charles.httpsdk.novate.download.DownSubscriber;
import com.charles.httpsdk.novate.exception.NovateException;
import com.charles.httpsdk.novate.model.BaseModel;
import com.charles.httpsdk.novate.request.NovateRequest;
import com.charles.httpsdk.novate.request.NovateRequestBody;
import com.charles.httpsdk.novate.request.RequestInterceptor;
import com.charles.httpsdk.novate.util.FileUtil;
import com.charles.httpsdk.novate.util.LogWraper;
import com.charles.httpsdk.novate.util.Utils;
import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.FieldMap;
import retrofit2.http.Part;


/**
 * Novate adapts a Java interface to Retrofit call by using annotations on the declared methods to
 * define how requests are made. Create instances using {@linkplain Builder
 * the builder} and pass your interface to {@link #} to generate an implementation.
 * <p/>
 * For example,
 * <pre>{@code
 * Novate novate = new Novate.Builder()
 *     .baseUrl("http://api.example.com")
 *     .addConverterFactory(GsonConverterFactory.create())
 *     .build();
 * <p/>
 * MyApi api = Novate.create(MyApi.class);
 * Response<User> user = api.getUser().execute();
 * }</pre>
 *
 * @author Tamic (skay5200@163.com)
 */
public final class Novate extends BaseModel {

    private static Map<String, String> headers;
    private static Map<String, String> parameters;
    private static Map<String, Object> initparameters = new HashMap<>();
    private static Retrofit.Builder retrofitBuilder;
    private static Retrofit retrofit;
    private static OkHttpClient.Builder okhttpBuilder;
    public static BaseApiService apiManager;
    private static OkHttpClient okHttpClient;
    private static Context mContext;
    private final okhttp3.Call.Factory callFactory;
    private final String baseUrl;
    private final List<Converter.Factory> converterFactories;
    private final List<CallAdapter.Factory> adapterFactories;
    private final Executor callbackExecutor;
    private final boolean validateEagerly;
    private Observable<ResponseBody> downObservable;
    private Map<Object, Observable<ResponseBody>> downMaps = new HashMap<Object, Observable<ResponseBody>>() {
    };
    private ObservableTransformer exceptTransformer = null;
    public static final String TAG = "Novate";


    private static  Map<String,String> mMapinit ;


    /**
     * Mandatory constructor for the Novate
     */
    Novate(okhttp3.Call.Factory callFactory, String baseUrl, Map<String, String> headers,
           Map<String, String> parameters, BaseApiService apiManager,
           List<Converter.Factory> converterFactories, List<CallAdapter.Factory> adapterFactories,
           Executor callbackExecutor, boolean validateEagerly,Map<String,String> mapKey,Context context) {
        super(context);
        this.callFactory = callFactory;
        this.baseUrl = baseUrl;
        this.headers = headers;
        this.parameters = parameters;
        this.apiManager = apiManager;
        this.converterFactories = converterFactories;
        this.adapterFactories = adapterFactories;
        this.callbackExecutor = callbackExecutor;
        this.validateEagerly = validateEagerly;
        this.mMapinit = mapKey;

    }

    /**
     * create ApiService
     */
    public <T> T create(final Class<T> service) {

        return retrofit.create(service);
    }



    /**
     * @param subscriber
     */
    public <T> T call(Observable<T> observable, BaseSubscriber<T> subscriber) {

        observable.compose(schedulersTransformer)
//                .compose(getLifecycleProvider().bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(subscriber);
        return  (T) observable;
    }

    /**
     * @param subscriber
     */
    public <T> T execute(NovateRequest request,  BaseSubscriber<T> subscriber) {
        return call(request, subscriber);
    }

    private <T> T call(NovateRequest request,  BaseSubscriber<T> subscriber) {
        //todo dev
        //okHttpClient.newCall().execute();
     return null;
    }

    /**
     * Novate execute get
     * <p>
     * return parsed data
     * <p>
     * you don't need to parse ResponseBody
     */
    public <T> T executeGet(final String url, final Map<String, Object> maps, final ResponseCallBack<T> callBack) {
        LogWraper.e("novate parames:",maps.toString());
        Observable observable = apiManager.executeGet(url,maps);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new NovateSubscriber<T>(mContext, callBack));
        return  (T) observable;

    }




    /**
     * Novate execute get request
     * @param keyid path or url
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxGetKey(final String keyid, ResponseCallback<T, ResponseBody> callBack) {
        return rxGetKey( keyid, initparameters, callBack);
    }



    /**
     * Novate execute get request
     * @param tag request tag
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
//     */
    public <T> T rxGetKey( final String key, @NonNull final Map<String, Object> maps, final ResponseCallback<T, ResponseBody> callBack) {
        String url = "";
        String tag ="";
        if (mMapinit.get(key) != null){
            url = mMapinit.get(key);
            tag = url;
        }else {
            LogWraper.e("novate","key 没有对应的value值");
            url = "";
        }
        if(maps == null) {
            LogWraper.e("novate parames:","maps is  null");
            Observable observable = apiManager.executeGet(url,initparameters);
            observable.compose(new OndoTransformer(tag, callBack))
                    .compose(schedulersTransformer)
                    .compose(handleErrTransformer())
                    .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
            return  (T) observable;
        }
        LogWraper.e("novate  parames:",maps.toString());

        Observable observable = apiManager.executeGet(url,maps);
        observable.compose(new OndoTransformer(tag, callBack))
                .compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;


    }


    /**
     *  需要拼接的URL 带参数
     * @param key
     * @param spUrl  需要拼接的参数
     * @param maps
     * @param callBack
     * @param <T>
     * @return
     */
    public <T> T rxGetKeySp( final String key,final  String spUrl, @NonNull final Map<String, Object> maps, final ResponseCallback<T, ResponseBody> callBack) {
        String url = "";
        String tag ="";
        if (mMapinit.get(key) != null){
            url = mMapinit.get(key);
            if (spUrl != null && spUrl.trim().length() != 0){
                url = url + spUrl;
            }
            tag = url;
        }else {
            LogWraper.e("novate","key 没有对应的value值");
            url = "";
        }

        return rxGetUrl(url,maps,callBack);
    }



    /**
     *  需要拼接的URL 不带参数
     * @param key
     * @param spUrl  需要拼接的参数
     * @param maps
     * @param callBack
     * @param <T>
     * @return
     */
    public <T> T rxGetKeySp( final String key,final  String spUrl, final ResponseCallback<T, ResponseBody> callBack) {
        String url = "";
        String tag ="";
        if (mMapinit.get(key) != null){
            url = mMapinit.get(key);
            if (spUrl != null && spUrl.trim().length() != 0){
                url = url + spUrl;
            }
            tag = url;
        }else {
            LogWraper.e("novate","key 没有对应的value值");
            url = "";
        }

        return rxGetUrl(url,callBack);
    }



    /**
     * Novate execute get request
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxGetUrl(final String url, ResponseCallback<T, ResponseBody> callBack) {
        return rxGetUrl(url, url, null, callBack);
    }



    /**
     * Novate execute get request
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxGetUrl(final String url,final Map<String, Object> maps, ResponseCallback<T, ResponseBody> callBack) {
        return rxGetUrl(url, url, maps, callBack);
    }


    /**
     * Novate execute get request
     * @param tag request tag
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
    //     */
    public <T> T rxGetUrl( final String tag,final String url, @NonNull final Map<String, Object> maps, final ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("novate url:",url);
        if(maps == null) {
            LogWraper.e("novate parames:","maps is  null");
            Observable observable = apiManager.executeGet(url,initparameters);
            observable.compose(new OndoTransformer(tag, callBack))
                    .compose(schedulersTransformer)
                    .compose(handleErrTransformer())
                    .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
            return  (T) observable;



        }else {
            LogWraper.e("novate  parames:",maps.toString());
            Observable observable = apiManager.executeGet(url,maps);
            observable.compose(new OndoTransformer(tag, callBack))
                    .compose(schedulersTransformer)
                    .compose(handleErrTransformer())
                    .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
            return  (T) observable;



        }
    }

    /**
     * Novate execute post request
     * @param keyid path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxPostKey(final String keyid, @FieldMap(encoded = true) Map<String, Object> maps, ResponseCallback<T, ResponseBody> callBack) {
        return rxPostKey(keyid, keyid, maps, callBack);
    }

    public <T> T rxPostKey( final String keyid, ResponseCallback<T, ResponseBody> callBack) {
        return rxPostKey(keyid,initparameters,callBack);
    }

    public <T> T rxPostKeySp( final String keyid,String spUrl, ResponseCallback<T, ResponseBody> callBack) {
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            if (spUrl != null && spUrl.trim().length() != 0){
                url = url + spUrl;
            }
            LogWraper.e("novate","keyid "+ keyid +" 对应的value值 " + url);
        }else {
            LogWraper.e("novate","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return rxPostUrl(url,initparameters,callBack);
    }

    /**
     * Novate execute post request
     * @param keyid path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxPostKeySp(final String keyid,String spUrl, @FieldMap(encoded = true) Map<String, Object> maps, ResponseCallback<T, ResponseBody> callBack) {
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            if (spUrl != null && spUrl.trim().length() != 0){
                url = url + spUrl;
            }
            LogWraper.e("novate","keyid "+ keyid +" 对应的value值 " + url);
        }else {
            LogWraper.e("novate","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return rxPostUrl(url, url, maps, callBack);
    }

        /**
         * Novate execute Post request
         * @param tag request tag
         * @param keyid path or url
         * @param maps parameters  maps
         * @param callBack  ResponseCallback
         * @param <T>  T return parsed data
         * @return Subscription
         */
    public <T> T rxPostKey(String tag, final String keyid, @FieldMap(encoded = true) Map<String, Object> maps, ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("parames:",maps.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate","keyid "+ keyid +" 对应的value值 " + url);
        }else {
            LogWraper.e("novate","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        Observable<ResponseBody> obser = apiManager.executePostBody(url, maps);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return (T) obser;
    }


    /**
     *   直接使用API进行访问网络 无参构造
     * @param url
     * @param callBack
     * @param <T>
     * @return
     */

    public <T> T rxPostUrl( final String url, ResponseCallback<T, ResponseBody> callBack) {
        return rxPostUrl(url,initparameters,callBack);
    }


    /**
     * Novate execute post request
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxPostUrl(final String url, @FieldMap(encoded = true) Map<String, Object> maps, ResponseCallback<T, ResponseBody> callBack) {
        return rxPostUrl(url, url, maps, callBack);
    }


    /**
     * Novate execute Post request
     * @param tag request tag
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxPostUrl(String tag, final String url, @FieldMap(encoded = true) Map<String, Object> maps, ResponseCallback<T, ResponseBody> callBack) {

        LogWraper.e("novate url:",url);
        LogWraper.e("parames:",maps.toString());
        Observable observable = apiManager.executePostBody(url,maps);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;



    }

    /**
     * Novate execute Put request
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxPutKey(final String keyid, final @FieldMap(encoded = true) Map<String, T>  maps, ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("parames:",maps.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate","keyid "+ keyid +" 对应的value值 " + url);
        }else {
            LogWraper.e("novate","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return rxPutUrl(url, maps, callBack);
    }

    /**
     * Novate execute Put request
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxPutKeySp(final String keyid,String spUrl, final @FieldMap(encoded = true) Map<String, T>  maps, ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("parames:",maps.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid) + spUrl;
            LogWraper.e("novate","keyid "+ keyid +" 对应的value值 " + url);
        }else {
            LogWraper.e("novate","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return rxPutUrl(url, maps, callBack);
    }



    /**
     * Novate execute Put request
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxPutUrl(final String url, final @FieldMap(encoded = true) Map<String, T>  maps, ResponseCallback<T, ResponseBody> callBack) {
        return rxPutUrl(url, url, maps, callBack);
    }


    /**
     * Novate execute Put request
     * @param tag request tag
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxPutUrl(String tag, final String url, final @FieldMap(encoded = true) Map<String, T>  maps, ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("parames:",maps.toString());
        Observable observable = apiManager.executePut(url,(Map<String, Object>) maps);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;



    }

    /**
     * Novate execute Delete request
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxDeleteKey(final String keyid, final Map<String, T> maps, ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("parames:",maps.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate","keyid "+ keyid +" 对应的value值 " + url);
        }else {
            LogWraper.e("novate","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return rxDeleteUrl(url, url, maps, callBack);
    }

    /**
     * Novate execute Delete request
     * @param keyid path or url
     * @param spUrl path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxDeleteKeySp(final String keyid,String spUrl, final Map<String, T> maps, ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("parames:",maps.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            if (spUrl != null && spUrl.trim().length() != 0){
                url = url + spUrl;
            }
            LogWraper.e("novate","keyid "+ keyid +" 对应的value值 " + url);
        }else {
            LogWraper.e("novate","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return rxDeleteUrl(url, url, maps, callBack);
    }

    /**
     * Novate execute Delete request
     * @param tag request tag
     * @param url path or url
     * @param maps parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxDeleteUrl(String tag, final String url, final Map<String, T> maps, ResponseCallback<T, ResponseBody> callBack) {
//        return (T) apiManager.executeDelete(url, (Map<String, Object>) maps)
//                .compose(schedulersTransformer)
//                .compose(handleErrTransformer())
//                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));


        Observable observable = apiManager.executeDelete(url,(Map<String, Object>) maps);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;


    }


    /**
     *  Novate rxUpload by post With Part
     *
     * 默认上传图片，
     * @param url url
     * @param file file
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithPart(String tag, String url, File file, ResponseCallback<T, ResponseBody> callBack) {

        return rxUploadWithPart(tag, url, ContentType.IMAGE, file, callBack);
    }

    /**
     *  Novate rxUpload by post With Part
     *
     * 默认上传图片，
     * @param url url
     * @param file file
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithPartKey(String keyid, File file, ResponseCallback<T, ResponseBody> callBack) {
        String url = "";
        String tag ="";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            tag = url;
        }else {
            LogWraper.e("novate","key 没有对应的value值");
            url = "";
        }
        return rxUploadWithPart(url, url, ContentType.IMAGE, file, callBack);
    }

    /**
     *  Novate rxUpload by post With Part
     *
     * 默认上传图片，
     * @param url url
     * @param file file
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithPartKeySp(String keyid,String spUrl ,File file, ResponseCallback<T, ResponseBody> callBack) {
        String url = "";
        String tag ="";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid) + spUrl;
            tag = url;
        }else {
            LogWraper.e("novate","key 没有对应的value值");
            url = "";
        }
        return rxUploadWithPart(url, url, ContentType.IMAGE, file, callBack);
    }


    /**
     * @param tag request tag
     * @param url   request url
     * @param type request ContentType See {@link ContentType}
     * @param file  file
     * @param callBack
     * @param <T> T
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithPart(Object tag, String url, ContentType type, File file, ResponseCallback<T, ResponseBody> callBack) {
        if (!file.exists()) {
            throw new Resources.NotFoundException(file.getPath() + "file 路径无法找到");
        }
        if (callBack == null) {
            callBack = ResponseCallback.CALLBACK_DEFAULT;
        }
        NovateRequestBody requestBody = Utils.createRequestBody(file, type, callBack);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        return rxUploadWithPartUrl(tag, url, body, callBack);    }


    /**
     *  Novate rxUpload by post With Part
     * @param keyid path or url
     * @param requestBody  requestBody
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxUploadWithPartKey(String keyid, MultipartBody.Part requestBody, ResponseCallback<T, ResponseBody> callBack) {
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate","keyid "+ keyid +" 对应的value值 " + url);
        }else {
            LogWraper.e("novate","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return rxUploadWithPartUrl(url, requestBody, callBack);
    }
    /**
     *  Novate rxUpload by post With Part
     * @param url path or url
     * @param requestBody  requestBody
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxUploadWithPartUrl(String url, MultipartBody.Part requestBody, ResponseCallback<T, ResponseBody> callBack) {
        return rxUploadWithPartUrl(url, url, requestBody, callBack);
    }

    /**
     * Novate rxUpload by post With Part
     * @param tag request tag
     * @param keyid keyId
     * @param requestBody requestBody
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxUploadWithPartUrl(Object tag, String url, MultipartBody.Part requestBody, ResponseCallback<T, ResponseBody> callBack) {
        Observable observable = apiManager.uploadFlieWithPart(url,requestBody);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;



    }

    /**
     *  Novate rxUpload by post
     * @param url path or url
     * @param description description
     * @param requestBody  requestBody
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxUploadKey(String keyid, RequestBody description, MultipartBody.Part requestBody, ResponseCallback<T, ResponseBody> callBack) {

        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate","keyid "+ keyid +" 对应的value值 " + url);
        }else {
            LogWraper.e("novate","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return rxUploadFileUrl(url, url, description, requestBody, callBack);
    }

    /**
     *  Novate rxUpload by post
     * @param url path or url
     * @param description description
     * @param requestBody  requestBody
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxUploadUrl(String url, RequestBody description, MultipartBody.Part requestBody, ResponseCallback<T, ResponseBody> callBack) {
        return rxUploadFileUrl(url, url, description, requestBody, callBack);
    }

    /**
     * Novate rxUpload by post
     * @param tag request tag
     * @param url path or url
     * @param description description
     * @param requestBody     MultipartBody.Part
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Rxjava Subscription
     */
    public <T> T rxUploadFileUrl(Object tag, String url, RequestBody description, MultipartBody.Part requestBody, ResponseCallback<T, ResponseBody> callBack) {
        Observable observable = apiManager.uploadFlie(url,description,requestBody);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;


    }


    /**
     *  Novate rxUpload by post With Body
     *
     * 默认上传图片，更多UploadWithBody(Object tag, String url, ContentType type, File file, ResponseCallback<T, ResponseBody> callBack)
     * @param url url
     * @param file file
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithBody(String url, File file, ResponseCallback<T, ResponseBody> callBack) {

        return rxUploadWithBody(url, url, ContentType.IMAGE, file, callBack);
    }

    /**
     *  Novate rxUpload by post With Body
     *
     *
     * 默认上传图片，更多UploadWithBody(Object tag, String url, ContentType type, File file, ResponseCallback<T, ResponseBody> callBack)
     * @param tag tag
     * @param url url
     * @param file file
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithBody(Object tag, String url, File file, ResponseCallback<T, ResponseBody> callBack) {

        return rxUploadWithBody(tag, url, ContentType.IMAGE, file, callBack);
    }


    /**
     * @param tag
     * @param url
     * @param type  request ContentType See {@link ContentType}
     * @param file
     * @param callBack ResponseCallback
     * @param <T> T
     * @return RxJava 1.X Subscription
     */
    public <T> T rxUploadWithBody(Object tag, String url, ContentType type, File file, ResponseCallback<T, ResponseBody> callBack) {
        if (!file.exists()) {
            throw new Resources.NotFoundException(file.getPath() + "file 路径无法找到");
        }
        //NovateRequestBody  requestFile = Utils.createRequestBody(file, type, callBack);
        if (callBack == null) {
            callBack = ResponseCallback.CALLBACK_DEFAULT;
        }
        callBack.setTag(tag);
        return rxUploadWithBody(tag, url, Utils.createRequestBody(file, type, callBack), callBack);
    }


    /**
     *  Novate rxUpload by post With Body
     * @param url url
     * @param requestBody requestBody
     * @param callBack back
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithBody(String url, NovateRequestBody requestBody, ResponseCallback<T, ResponseBody> callBack) {
        return rxUploadWithBody(url, url, requestBody, callBack);
    }

    /**
     *  Novate rxUpload by post With Body
     * @param tag tag
     * @param url url
     * @param requestBody requestBody
     * @param callBack back
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithBody(Object tag, String url, RequestBody requestBody, ResponseCallback<T, ResponseBody> callBack) {
        Observable observable = apiManager.postRequestBody(url,requestBody);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;


    }

    /**
     *  Novate rxUpload by post Body Maps
     * @param url url
     * @param maps File files
     * @param callBack back
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithPartMapByFileKey(String url, Map<String, File> maps, ResponseCallback<T, ResponseBody> callBack) {

        return rxUploadWithPartMapByFile(url, url, ContentType.IMAGE, maps, callBack);
    }


    /**
     *  Novate rxUpload by post Body Maps
     * @param url url
     * @param maps RequestBody files
     * @param callBack back
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithBodyMap(String url, Map<String, RequestBody> maps, ResponseCallback<T, ResponseBody> callBack) {
        return rxUploadWithBodyMap(url, url,maps, callBack);
    }


    /**
     * Novate rxUpload by post With PartMaps
     * @param tag tag
     * @param url url
     * @param maps File files
     * @param callBack ResponseCallback
     * @param <T>
     * @return Rxjava 1.x Subscription
     */
    public <T> T rxUploadWithPartMapByFile(Object tag, String url, ContentType type, Map<String, File> maps, ResponseCallback<T, ResponseBody> callBack) {

        if (callBack == null) {
            callBack = ResponseCallback.CALLBACK_DEFAULT;
        }
        Observable<ResponseBody> obser = apiManager.uploadFlieWithPartMap(url,  Utils.createParts("image", maps, type, callBack));
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return (T) obser;

    }

    /**
     *  Novate rxUpload by post PartBody List
     * @param url url
     * @param list File files
     * @param callBack back
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithPartListByFile(String url, List<File> list, ResponseCallback<T, ResponseBody> callBack) {
        return rxUploadWithPartListByFile(url, url, ContentType.IMAGE, list, callBack);
    }


    /**
     *  Novate rxUpload by post PartBody List
     * @param url url
     * @param list RequestBody files
     * @param callBack back
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithPartListByFile(Object tag, String url, List<File> list, ResponseCallback<T, ResponseBody> callBack) {
        return rxUploadWithPartListByFile(tag, url, ContentType.IMAGE, list, callBack);
    }

    /**
     * Novate Novate rxUpload by post PartBody List
     * @param tag tag
     * @param url url
     * @param list File files
     * @param callBack ResponseCallback
     * @param <T>
     * @return Rxjava 1.x Subscription
     */
    public <T> T rxUploadWithPartListByFile(Object tag, String url, ContentType type, List<File> list, ResponseCallback<T, ResponseBody> callBack) {

        if (callBack == null) {
            callBack = ResponseCallback.CALLBACK_DEFAULT;
        }
        Observable<ResponseBody> obser = apiManager.uploadFlieWithPartList(url, Utils.createPartLists("image", list, type, callBack));
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return (T) obser;


    }

    /**
     *  Novate rxUpload by post Body Maps
     * @param url url
     * @param maps RequestBody files
     * @param callBack ResponseCallback
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithBodyMapByFile(String url, Map<String, File> maps, ResponseCallback<T, ResponseBody> callBack) {
        return rxUploadWithBodyMapByFile(url, url, ContentType.IMAGE, maps, callBack);
    }

    /**
     *  Novate rxUploadWithBodyMapByFile
     * @param tag tag
     * @param url  url
     * @param maps maps
     * @param callBack  ResponseCallback
     * @param <T>
     * @return Rxjava Subscription
     */
    public <T> T rxUploadWithBodyMapByFile(Object tag, String url, Map<String, File> maps, ResponseCallback<T, ResponseBody> callBack) {
        return rxUploadWithBodyMapByFile(tag, url, ContentType.IMAGE, maps, callBack);
    }

    /**
     * Novate rxUpload by post With BodyMaps
     * @param tag tag
     * @param url url
     * @param maps File files
     * @param callBack ResponseCallback
     * @param <T>
     * @return Rxjava 1.x Subscription
     */
    public <T> T rxUploadWithBodyMapByFile(Object tag, String url, ContentType type, Map<String, File> maps, ResponseCallback<T, ResponseBody> callBack) {
        Map<String, RequestBody> bodys = new HashMap<>();

        if (callBack == null) {
            callBack = ResponseCallback.CALLBACK_DEFAULT;
        }

        if (maps != null && maps.size() > 0) {
            Iterator<String> keys = maps.keySet().iterator();
            NovateRequestBody requestBody = null;
            while(keys.hasNext()){
                String i = keys.next();
                File file = maps.get(i);
                if (FileUtil.exists(file)) {
                    throw new Resources.NotFoundException(file.getPath() + "file 路径无法找到");
                } else {
                    requestBody = Utils.createRequestBody(file, type, callBack);
                    bodys.put(i, requestBody);
                }
            }
        }

        Observable<ResponseBody> obser = apiManager.uploadFiles(url, bodys);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return (T) obser;


    }


    /**
     * Novate rxUpload by post With BodyMaps
     * @param tag tag
     * @param url url
     * @param maps RequestBody files
     * @param callBack ResponseCallback
     * @param <T>
     * @return Rxjava 1.x Subscription
     */
    public <T> T rxUploadWithBodyMap(Object tag, String url, Map<String, RequestBody> maps, ResponseCallback<T, ResponseBody> callBack) {
        Observable observable = apiManager.uploadFiles(url,maps);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;


    }


    /**
     * Novate rxUpload by post With BodyMaps
     * @param url url
     * @param files  MultipartBody.Part files
     * @param callBack
     * @param <T>
     * @return Rxjava 1.x Subscription
     */
    public <T> T rxUploadWithPartMapKey(String keyid, Map<String, MultipartBody.Part> files, ResponseCallback<T, ResponseBody> callBack) {
        String url = "",tag = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            tag = url;
            LogWraper.e(TAG,"key"+ keyid +" 对应的value值:"+ url);
        }else {
            LogWraper.e(TAG,keyid+" 没有对应的value值");
            url = "";
        }
        return rxUploadWithPartMapUrl(url, url, files, callBack);
    }


    /**
     * Novate rxUpload by post With BodyMaps
     * @param url url
     * @param files  MultipartBody.Part files
     * @param callBack
     * @param <T>
     * @return Rxjava 1.x Subscription
     */
    public <T> T rxUploadWithPartMapUrl(String url, Map<String, MultipartBody.Part> files, ResponseCallback<T, ResponseBody> callBack) {
        return rxUploadWithPartMapUrl(url, url, files, callBack);
    }

    /**
     * Novate rxUpload by post With BodyMaps
     * @param tag tag
     * @param url url
     * @param files MultipartBody.Part files
     * @param callBack ResponseCallback
     * @param <T>
     * @return Rxjava 1.x Subscription
     */
    public <T> T rxUploadWithPartMapUrl(Object tag, String url, Map<String, MultipartBody.Part> files, ResponseCallback<T, ResponseBody> callBack) {
        Observable observable = apiManager.uploadFlieWithPartMap(url,files);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;


    }

    /**
     * Rx Download file
     * @param callBack
     */
    public <T> T rxDownloadKeySp(String keyid,String spUrl ,final ResponseCallback callBack) {
        String url = "",tag = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid) + spUrl;
            tag = url;
            LogWraper.e(TAG,"key"+ keyid +" 对应的value值:"+ url);
        }else {
            LogWraper.e(TAG,keyid+" 没有对应的value值");
            url = "";
        }

        return rxDownloadUrl(url, url, callBack);
    }

    /**
     * Rx Download file
     * @param callBack
     */
    public <T> T rxDownloadKey(String keyid, final ResponseCallback callBack) {
        String url = "",tag = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            tag = url;
            LogWraper.e(TAG,"key"+ keyid +" 对应的value值:"+ url);
        }else {
            LogWraper.e(TAG,keyid+" 没有对应的value值");
            url = "";
        }

       return rxDownloadUrl(url, url, callBack);
    }

    /**
     * Rx Download file
     * @param tag request Tag
     * @param callBack
     */
    public <T> T rxDownloadUrl(Object tag, String url, final ResponseCallback callBack) {
       /* okhttpBuilder.networkInterceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                        new NovateResponseBody(originalResponse.body(), callBack))
                        .build();
            }
        });

        Retrofit retrofit = retrofitBuilder.client(okhttpBuilder.build()).build();
        BaseApiService apiManager = retrofit.create(BaseApiService.class);*/

        if (downMaps.get(tag) == null) {
            downObservable = apiManager.downloadFile(url);
        } else {
            downObservable = downMaps.get(tag);
        }
        downMaps.put(tag, downObservable);
//        return (T) downObservable.compose(schedulersTransformerDown)
//                .compose(handleErrTransformer())
//                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack));

        downObservable.compose(schedulersTransformerDown)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack));
        return  (T) downObservable;


    }

    /**
     * Novate Post by Form
     * @param url path or url
     * @param parameters  parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Rxjav 1.x Subscription
     */
    public <T> T rxFormKey(String keyid, @FieldMap(encoded = true) Map<String, Object> parameters, ResponseCallback<T, ResponseBody> callBack) {
        String url = "",tag = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            tag = url;
            LogWraper.e(TAG,"key"+ keyid +" 对应的value值:"+ url);
        }else {
            LogWraper.e(TAG,keyid+" 没有对应的value值");
            url = "";
        }

        return rxFormUrl(url, url, parameters, callBack);
    }

    /**
     * Novate Post by Form
     * @param tag request tag
     * @param url path or url
     * @param parameters  parameters  maps
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxFormUrl(Object tag, String url, @FieldMap(encoded = true) Map<String, Object> parameters, ResponseCallback<T, ResponseBody> callBack) {
        Observable observable = apiManager.postForm(url,parameters);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack));
        return  (T) observable;


    }

    /**
     * Novate  Post by Body
     * @param url path or url
     * @param bean Object bean
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxBodyPostKey(String key, Object bean, ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("novate parames:",bean.toString());
        String url = "",tag = "";
        if (mMapinit.get(key) != null){
            url = mMapinit.get(key);
            tag = url;
            LogWraper.e(TAG,"key"+ key +" 对应的value值:"+ url);
        }else {
            LogWraper.e(TAG,key+" 没有对应的value值");
            url = "";
        }
        return rxBodyPostUrl(url, url, bean, callBack);
    }

    /**
     * Novate  Post by Body
     * @param tag request tag
     * @param url path or url
     * @param bean Object bean
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxBodyPostUrl(Object tag, String url, Object bean,  ResponseCallback<T, ResponseBody> callBack) {
        Observable observable = apiManager.executePostBody(url,bean);
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;



    }



    /**
     * Novate  Post by Json
     * @param keyid path or url
     * @param jsonString   jsonString
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxJsonPostKey(String keyid, String jsonString, ResponseCallback<T, ResponseBody> callBack) {
        String url = "",tag = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            tag = url;
            LogWraper.e(TAG,"key"+ keyid +" 对应的value值:"+ url);
        }else {
            LogWraper.e(TAG,"key"+ keyid +" 没有对应的value值");
            url = "";
        }
        return rxJsonPostUrl(url, jsonString, callBack);
    }




    /**
     * Novate  Post by Json
     * @param key request tag
     * @param spUrl path or url
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxJsonPostKeySp(String key, String spUrl, String jsonString, ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("novate parames:",jsonString.toString());
        LogWraper.e("novate mMap:",mMapinit.get(key) + "");
        String url = "",tag = "";
        if (mMapinit.get(key) != null){
            url = mMapinit.get(key) + spUrl;
            tag = url;
            LogWraper.e(TAG,"key"+ key +" 对应的value值:"+ url);
        }else {
            LogWraper.e(TAG,"key"+ key +" 没有对应的value值");
            url = "";
        }

        return   rxJsonPostUrl(url,jsonString,callBack);
    }

    /**
     * Novate  Post by Json
     * @param url path or url
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxJsonPostUrl(String url, String jsonString, ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("novate parames:",jsonString.toString());
        return rxJsonPostUrl(url,url,jsonString,callBack);
    }
    /**
     * Novate  Post by Json
     * @param url path or url
     * @param tag request tag
     * @param callBack  ResponseCallback
     * @param <T>  T return parsed data
     * @return Subscription
     */
    public <T> T rxJsonPostUrl(String url,Object tag, String jsonString, ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("novate parames:",jsonString.toString());
        Observable observable = apiManager.postRequestBody(url,Utils.createJson(jsonString));
        observable.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(tag, callBack).addContext(mContext));
        return  (T) observable;


    }







    /**
     * RXJAVA schedulersTransformer
     * AndroidSchedulers.mainThread()
     */
    final ObservableTransformer onDdoTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.doOnDispose(new Action() {
                @Override
                public void run() throws Exception {

                }
            });
        }
    };

    private class OndoTransformer implements ObservableTransformer{


        private ResponseCallback callback;
        private Object tag;

        public OndoTransformer(Object tag, ResponseCallback callback) {
            this.tag = tag;
            this.callback = callback;
        }

        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.doOnDispose(new Action() {
                @Override
                public void run() throws Exception {

                }
            });
        }
    }

    /**
     * RXJAVA schedulersTransformer
     * AndroidSchedulers.mainThread()
     */
    final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
//                    .compose(getLifecycleProvider().bindUntilEvent(ActivityEvent.DESTROY));
        }
    };

    /**
     * RXJAVA schedulersTransformer
     *
     * Schedulers.io()
     */
    final ObservableTransformer schedulersTransformerDown =new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io());
        }
    };

    /**
     * handleException Transformer
     * @param <T>
     * @return  Transformer
     */
    public <T> ObservableTransformer<NovateResponse<T>, T> handleErrTransformer() {

        if (exceptTransformer != null) {
            return exceptTransformer;
        } else {
            return exceptTransformer = new ObservableTransformer() {
                @Override
                public ObservableSource apply(Observable upstream) {
                    return upstream.onErrorResumeNext(new HttpResponseFunc<T>());
                }
            };
        }
    }


    /**
     * HttpResponseFunc
     * @param <T> Observable
     */
    private static class HttpResponseFunc<T> implements Function<java.lang.Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(java.lang.Throwable throwable) throws Exception {
            return Observable.error(NovateException.handleException(throwable));
        }
    }

    /**  T
     * @param <T> response
     */
    private class HandleFuc<T> implements Function<NovateResponse<T>, T> {
        @Override
        public T apply(NovateResponse<T> response) throws Exception {
            if (response == null || (response.getData() == null && response.getResult() == null)) {
                throw new JsonParseException("后端数据不对");
            }
            return response.getData();
        }
    }

    /**
     * Retroift get
     *
     * @param <T>
     * @param url
     * @param maps
     * @param subscriber
     * @return no parse data
     */
    public <T> T getUrl(String url, Map<String, Object> maps, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",maps.toString());
        Observable<ResponseBody> obser = apiManager.executeGet(url, maps);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return  (T) obser;
    }

    /**
     * /**
     * Novate executePost
     *
     * @return no parse data
     * <p>
     * you must to be parse ResponseBody
     * <p>
     * <p/>
     * For example,
     * <pre>{@code
     * Novate novate = new Novate.Builder()
     *     .baseUrl("http://api.example.com")
     *     .addConverterFactory(GsonConverterFactory.create())
     *     .build();
     *
     * novate.post("url", parameters, new BaseSubscriber<ResponseBody>(context) {
     *    @Override
     *   public void onError(Throwable e) {
     *
     *   }
     *
     *  @Override
     *  public void onNext(ResponseBody responseBody) {
     *
     *   // todo you need to parse responseBody
     *
     *  }
     *  });
     * <p/>
     *
     * }</pre>
     */
    public <T> T postUrl(String url, @FieldMap(encoded = true) Map<String, Object> parameters, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",parameters.toString());
        Observable<ResponseBody> obser = apiManager.executePost(url, (Map<String, Object>) parameters);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return  (T) obser;
    }

    /**
     * Novate executePost
     *
     * @return parsed data
     * you don't need to   parse ResponseBody
     */
    public <T> T executePostKey(final String keyid, @FieldMap(encoded = true) Map<String, Object> parameters, final ResponseCallBack<T> callBack) {
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        LogWraper.e("novate parames:",parameters.toString());
        return executePostUrl(url,parameters,callBack);
    }
    /**
     * Novate executePost
     *
     * @return parsed data
     * you don't need to   parse ResponseBody
     */
    public <T> T executePostUrl(final String url, @FieldMap(encoded = true) Map<String, Object> parameters, final ResponseCallBack<T> callBack) {
        LogWraper.e("novate parames:",parameters.toString());
        Observable<ResponseBody> obser = apiManager.executePost(url, parameters);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new NovateSubscriber<T>(mContext, callBack));
        return  (T) obser;


    }


    /**
     * Novate Post by Form
     *
     * @param url
     * @param subscriber
     */
    public <T> T formKey(String keyid, @FieldMap(encoded = true) Map<String, Object> fields, BaseSubscriber<ResponseBody> subscriber) {
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }

        LogWraper.e("novate parames:",fields.toString());
        return formUrl(url,fields,subscriber);
    }
    /**
     * Novate Post by Form
     *
     * @param url
     * @param subscriber
     */
    public <T> T formUrl(String url, @FieldMap(encoded = true) Map<String, Object> fields, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",fields.toString());
        Observable<ResponseBody> obser = apiManager.postForm(url, fields);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return  (T) obser;
    }


    /**
     * Novate execute Post by Form
     *
     * @return parsed data
     * you don't need to   parse ResponseBody
     */
    public <T> T executeFormKey(final String keyid, final @FieldMap(encoded = true) Map<String, Object> fields, final ResponseCallBack<T> callBack) {
        LogWraper.e("novate parames:",fields.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return executeFormUrl(url,fields,callBack);
    }
 /**
     * Novate execute Post by Form
     *
     * @return parsed data
     * you don't need to   parse ResponseBody
     */
    public <T> T executeFormUrl(final String url, final @FieldMap(encoded = true) Map<String, Object> fields, final ResponseCallBack<T> callBack) {
        LogWraper.e("novate parames:",fields.toString());
        Observable<ResponseBody> obser = apiManager.postForm(url, fields);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new NovateSubscriber<T>(mContext, callBack));
        return  (T) obser;


    }


    /**
     * http Post by Body
     * you  need to parse ResponseBody
     *
     * @param url
     * @param subscriber
     */
    public void PostbodyKey(String keyid, Object body, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",body.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        PostbodyUrl(url,body,subscriber);
    }
    /**
     * http Post by Body
     * you  need to parse ResponseBody
     *
     * @param url
     * @param subscriber
     */
    public void PostbodyUrl(String url, Object body, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",body.toString());
        Observable<ResponseBody> obser = apiManager.executePostBody(url, body);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);


    }

    /**
     * http execute Post by body
     *
     * @return parsed data
     * you don't need to   parse ResponseBody
     */
    public <T> T executeBodyKey(final String keyid, final Object body, final ResponseCallBack<T> callBack) {
        LogWraper.e("novate parames:",body.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        final Type[] types = callBack.getClass().getGenericInterfaces();
            return executeBodyUrl(url,body,callBack);
    }
    /**
     * http execute Post by body
     * @parame url
     * @return parsed data
     * you don't need to   parse ResponseBody
     */
    public <T> T executeBodyUrl(final String url, final Object body, final ResponseCallBack<T> callBack) {
        LogWraper.e("novate parames:",body.toString());
        final Type[] types = callBack.getClass().getGenericInterfaces();
        Observable<ResponseBody> obser = apiManager.executePostBody(url, body);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new NovateSubscriber<T>(mContext, callBack));
        return (T) obser;

    }


    /**
     * http Post by json
     * you  need to parse ResponseBody
     *
     * @param keyid
     * @param jsonStr    Json String
     * @param subscriber
     */
    public<T> T  rxPostjsonKey(String keyid, String jsonStr, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",jsonStr.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return rxPostjsonUrl(url,jsonStr,subscriber);
    }
    /**
     * http Post by json
     * you  need to parse ResponseBody
     *
     * @param url
     * @param jsonStr    Json String
     * @param subscriber
     */
    public<T> T  rxPostjsonUrl(String url, String jsonStr, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",jsonStr.toString());

        Observable<ResponseBody> obser = apiManager.postRequestBody(url, Utils.createJson(jsonStr));
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return (T) obser;


    }





    /**
     * Novate Execute http by Delete
     *
     * @return parsed data
     * you don't need to   parse ResponseBody
     */
    public <T> T executeDeleteKey(final String keyid, final Map<String, T> maps, final ResponseCallBack<T> callBack) {
        LogWraper.e("novate parames:",maps.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
//        final Type[] types = callBack.getClass().getGenericInterfaces();

        return executeDeleteUrl(url,maps,callBack);
    }
    /**
     * Novate Execute http by Delete
     *
     * @return parsed data
     * you don't need to   parse ResponseBody
     */
    public <T> T executeDeleteUrl(final String url, final Map<String, T> maps, final ResponseCallBack<T> callBack) {
        LogWraper.e("novate parames:",maps.toString());

        final Type[] types = callBack.getClass().getGenericInterfaces();

        Observable<ResponseBody> obser = apiManager.executeDelete(url, (Map<String, Object>) maps);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new NovateSubscriber<T>(mContext, callBack));
        return (T) obser;



    }

    /**
     * Novate put
     *
     * @param keyid
     * @param parameters
     * @param subscriber
     * @param <T>
     * @return no parse data
     */
    public <T> T putKey(String keyid, final @FieldMap(encoded = true) Map<String, T> parameters, BaseSubscriber<ResponseBody> subscriber) {
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        LogWraper.e("novate parames:",parameters.toString());
        return putUrl(url,parameters,subscriber);
    }
    /**
     * Novate put
     *
     * @param url
     * @param parameters
     * @param subscriber
     * @param <T>
     * @return no parse data
     */
    public <T> T putUrl(String url, final @FieldMap(encoded = true) Map<String, T> parameters, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",parameters.toString());
        Observable<ResponseBody> obser = apiManager.executePut(url, (Map<String, Object>) parameters);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return (T) obser;



    }

    /**
     * Novate Execute  Http by Put
     *
     * @return parsed data
     * you don't need to parse ResponseBody
     */
    public <T> T executePutKey(final String keyid, final @FieldMap(encoded = true) Map<String, T> parameters, final ResponseCallBack<T> callBack) {
        LogWraper.e("novate parames:",parameters.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return executePutUrl(url,parameters,callBack);
    }
    /**
     * Novate Execute  Http by Put
     *
     * @return parsed data
     * you don't need to parse ResponseBody
     */
    public <T> T executePutUrl(final String url, final @FieldMap(encoded = true) Map<String, T> parameters, final ResponseCallBack<T> callBack) {
        LogWraper.e("novate parames:",parameters.toString());
        Observable<ResponseBody> obser = apiManager.executePut(url, (Map<String, Object>) parameters);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new NovateSubscriber<T>(mContext, callBack));
        return (T) obser;


    }


    /**
     * Novate Test
     *
     * @param url        url
     * @param maps       maps
     * @param subscriber subscriber
     * @param <T>        T
     * @return
     */
    public <T> T test(String url, Map<String, T> maps, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",maps.toString());

        Observable<ResponseBody> obser = apiManager.getTest(url, (Map<String, Object>) maps);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return (T) obser;


    }

    /**
     * Novate upload
     *
     * @param keyid
     * @param requestBody requestBody
     * @param subscriber  subscriber
     * @param <T>         T
     * @return
     */
    public <T> T uploadKey(String keyid, RequestBody requestBody, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",requestBody.toString());

        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return uploadKeyUrl(url,requestBody,subscriber);
    }
    /**
     * Novate upload
     *
     * @param url
     * @param requestBody requestBody
     * @param subscriber  subscriber
     * @param <T>         T
     * @return
     */
    public <T> T uploadKeyUrl(String url, RequestBody requestBody, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",requestBody.toString());
        Observable<ResponseBody> obser = apiManager.postRequestBody(url, requestBody);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return (T) obser;
    }

    /**
     * uploadImage
     *
     * @param keyid        url
     * @param file       file
     * @param subscriber
     * @param <T>
     * @return
     */
    public <T> T uploadImageKey(String keyid, File file, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",file.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return uploadImageUrl(url,file,subscriber);
    }
 /**
     * uploadImage
     *
     * @param url        url
     * @param file       file
     * @param subscriber
     * @param <T>
     * @return
     */
    public <T> T uploadImageUrl(String url, File file, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",file.toString());
        Observable<ResponseBody> obser = apiManager.upLoadImage(url, Utils.createImage(file));
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return (T) obser;
    }

    /**
     * Novate upload Flie
     *
     * @param keyid
     * @param file       file
     * @param subscriber subscriber
     * @param <T>        T
     * @return
     */
    public <T> T uploadFlieKey(String keyid, RequestBody file, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",file.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return uploadFlieUrl(url,file,subscriber);
    }

    /**
     * Novate upload Flie
     *
     * @param url
     * @param file       file
     * @param subscriber subscriber
     * @param <T>        T
     * @return
     */
    public <T> T uploadFlieUrl(String url, RequestBody file, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",file.toString());

        Observable<ResponseBody> obser = apiManager.postRequestBody(url, file);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return (T) obser;
    }



    /**
     * Novate upload Flie
     *
     * @param keyid
     * @param file       file
     * @param subscriber subscriber
     * @param <T>        T
     * @return
     */
    public <T> T uploadFlieKey(String keyid, RequestBody description, MultipartBody.Part file, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",file.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return uploadFlieUrl(url,description,file,subscriber);
    }

     /**
     * Novate upload Flie
     *
     * @param url
     * @param file       file
     * @param subscriber subscriber
     * @param <T>        T
     * @return
     */
    public <T> T uploadFlieUrl(String url, RequestBody description, MultipartBody.Part file, BaseSubscriber<ResponseBody> subscriber) {

        Observable<ResponseBody> obser = apiManager.uploadFlie(url, description, file);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return (T) obser;
    }





    /**
     * Novate upload Flies
     *
     * @param keyid
     * @param files
     * @param <T>        T
     * @return
     */
    public <T> T uploadFlies(String keyid, Map<String, RequestBody> files, BaseSubscriber<ResponseBody> subscriber) {
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        LogWraper.e("novate parames:",files.toString());
        return uploadFliesUrl(url,files,subscriber);
    }

    /**
     * Novate upload Flies
     *
     * @param url
     * @param subscriber subscriber
     * @param <T>        T
     * @return
     */
    public <T> T uploadFliesUrl(String url, Map<String, RequestBody> files, BaseSubscriber<ResponseBody> subscriber) {

        LogWraper.e("novate parames:",files.toString());

        Observable<ResponseBody> obser = apiManager.uploadFiles(url,files );
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return (T) obser;

    }


    /**
     * Novate upload Flies
     *
     * @param keyid
     * @param files
     * @param callBack
     * @param <T>        T
     * @return
     */
    public <T> T uploadFliesKey(String keyid, Map<String, RequestBody> files, final ResponseCallback<T, ResponseBody> callBack) {
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        LogWraper.e("novate parames:",files.toString());
        return uploadFliesUrl(url,files,callBack);


    }
    /**
     * Novate upload Flies
     *
     * @param url
     * @param files subscriber
     * @param <T>        T
     * @return
     */
    public <T> T uploadFliesUrl(String url, Map<String, RequestBody> files, final ResponseCallback<T, ResponseBody> callBack) {
        LogWraper.e("novate parames:",files.toString());
        Observable<ResponseBody> obser = apiManager.uploadFiles(url, files);
        obser.compose(new OndoTransformer(url,callBack))
                .compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(new RxSubscriber<T, ResponseBody>(url, callBack).addContext(mContext));
        return (T) obser;


    }



    /**
     * Novate upload Flies WithPartMap
     * @param keyid
     * @param partMap
     * @param file
     * @param subscriber
     * @param <T>
     * @return
     */
    public <T> T uploadFileWithPartMapKey(String keyid, Map<String, RequestBody> partMap,
                                       @Part("file") MultipartBody.Part file, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",partMap.toString()+"  "+file.toString());
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return uploadFileWithPartMapUrl(url,partMap,file,subscriber);
    }
    /**
     * Novate upload Flies WithPartMap
     * @param url
     * @param partMap
     * @param file
     * @param subscriber
     * @param <T>
     * @return
     */
    public <T> T uploadFileWithPartMapUrl(String url, Map<String, RequestBody> partMap,
                                       @Part("file") MultipartBody.Part file, BaseSubscriber<ResponseBody> subscriber) {
        LogWraper.e("novate parames:",partMap.toString()+"  "+file.toString());

        Observable<ResponseBody> obser = apiManager.uploadFileWithPartMap(url,  partMap, file);
        obser.compose(schedulersTransformer)
                .compose(handleErrTransformer())
                .subscribe(subscriber);
        return (T) obser;
    }


    /**
     * Novate download
     *
     * @param keyid
     * @param callBack
     */
    public <T> T downloadkey(String keyid, DownLoadCallBack callBack) {
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return downloadUrl(keyid, FileUtil.getFileNameWithURL(url), callBack);
    }

    /**
     * @param url
     * @param name
     * @param callBack
     */
    public <T> T downloadUrl(String url, String name, DownLoadCallBack callBack) {
        return download(FileUtil.generateFileKey(url, name), url, null, name, callBack);
    }

    /**
     * downloadMin
     *
     * @param keyid
     * @param callBack
     */
    public <T> T  downloadMinKey(String keyid, DownLoadCallBack callBack) {
        String url = "";
        if (mMapinit.get(keyid) != null){
            url = mMapinit.get(keyid);
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值 " + url);
        }else {
            LogWraper.e("novate map","keyid "+ keyid +" 没有对应的value值");
            url = "";
        }
        return downloadMinUrl(FileUtil.generateFileKey(url, FileUtil.getFileNameWithURL(url)), url, callBack);
    }

    /**
     * downloadMin
     * @param key  key
     * @param url url
     * @param callBack CallBack
     */
    public <T> T downloadMinUrl(String key, String url, DownLoadCallBack callBack) {
        return downloadMinUrl(key, url, FileUtil.getFileNameWithURL(url), callBack);
    }

    /**
     * downloadMin
     * @param key key
     * @param url down url
     * @param name name
     * @param callBack callBack
     */
    public <T> T downloadMinUrl(String key, String url, String name, DownLoadCallBack callBack) {
        return downloadMinUrl(key, url, null, name, callBack);
    }

    /**
     * download small file
     * @param key
     * @param url
     * @param savePath
     * @param name
     * @param callBack
     */
    public <T> T  downloadMinUrl(String key, String url, String savePath, String name, DownLoadCallBack callBack) {

        if(TextUtils.isEmpty(key)) {
            key = FileUtil.generateFileKey(url, FileUtil.getFileNameWithURL(url));
        }

        if (downMaps.get(key) == null) {
            downObservable = apiManager.downloadSmallFile(url);
        } else {
            downObservable = downMaps.get(key);
        }
        downMaps.put(key, downObservable);
        return executeDownload(key, url, savePath, name, callBack);
    }


    /**
     * @param key
     * @param url
     * @param savePath
     * @param name
     * @param callBack
     */
    public <T> T download(String key, String url, String savePath, String name, DownLoadCallBack callBack) {
        if(TextUtils.isEmpty(key)) {
            key = FileUtil.generateFileKey(url, FileUtil.getFileNameWithURL(url));
        }
        return executeDownload(key, url, savePath, name, callBack);
    }

    /**
     * executeDownload
     * @param key
     * @param savePath
     * @param name
     * @param callBack
     */
    private <T> T executeDownload(String key, String url, String savePath, String name, final DownLoadCallBack callBack) {

        if (downMaps.get(key) == null) {
            downObservable = apiManager.downloadFile(url);
        } else {
            downObservable = downMaps.get(key);
        }
        downMaps.put(key, downObservable);
        downObservable.compose(schedulersTransformerDown)
                .compose(handleErrTransformer())
                .subscribe(new DownSubscriber<ResponseBody>(key, savePath, name, callBack, mContext));
        return (T) downObservable;

    }

    @Override
    protected LifecycleProvider getLifecycleProvider() {
      return   super.getLifecycleProvider();
    }



    public Builder newBuilder() {
        return new Builder(this);
    }

    /**
     * Mandatory Builder for the Builder
     */
    public static final class Builder {

        private static final int DEFAULT_TIMEOUT = 15;
        private static final int DEFAULT_MAXIDLE_CONNECTIONS = 5;
        private static final long DEFAULT_KEEP_ALIVEDURATION = 8;
        private static final long caheMaxSize = 10 * 1024 * 1024;
        private int readTimeout = DEFAULT_TIMEOUT;
        private okhttp3.Call.Factory callFactory;
        private String baseUrl;
        private Boolean isLog = false;
        private Object tag;
        private Boolean isCookie = false;
        private Boolean isCache = true;
        private List<InputStream> certificateList;
        private HostnameVerifier hostnameVerifier;
        private CertificatePinner certificatePinner;
        private List<Converter.Factory> converterFactories = new ArrayList<>();
        private List<CallAdapter.Factory> adapterFactories = new ArrayList<>();
        private Executor callbackExecutor;
        private boolean validateEagerly;
        private Context context;
        private NovateCookieManager cookieManager;
        private Cache cache = null;
        private Proxy proxy;
        private File httpCacheDirectory;
        private SSLSocketFactory sslSocketFactory;
        private ConnectionPool connectionPool;
        private Converter.Factory converterFactory;
        private CallAdapter.Factory callAdapterFactory;
        private Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR;
        private Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR_OFFLINE;
        private Map<String,String> mMapbuilt = new HashMap<>();
        private boolean isSkip = false;

        public Builder(Context context) {
            // Add the base url first. This prevents overriding its behavior but also
            // ensures correct behavior when using novate that consume all types.
            okhttpBuilder = new OkHttpClient.Builder();
            retrofitBuilder = new Retrofit.Builder();
            if(context instanceof Activity) {
                this.context  = ((Activity) context).getApplicationContext();
            } else {
                this.context = context;
            }

        }

        public Builder(Novate novate) {

        }

        /**
         * The HTTP client used for requests. default OkHttpClient
         * <p/>
         * This is a convenience method for calling {@link #callFactory}.
         * <p/>
         * Note: This method <b>does not</b> make a defensive copy of {@code client}. Changes to its
         * settings will affect subsequent requests. Pass in a {@linkplain OkHttpClient#clone() cloned}
         * instance to prevent this if desired.
         */
        @NonNull
        public Builder client(OkHttpClient client) {
            retrofitBuilder.client(Utils.checkNotNull(client, "client == null"));
            return this;
        }


        /**
         * Specify a custom call factory for creating {@link } instances.
         * <p/>
         * Note: Calling {@link #client} automatically sets this value.
         */
        public Builder callFactory(okhttp3.Call.Factory factory) {
            this.callFactory = Utils.checkNotNull(factory, "factory == null");
            return this;
        }

        /**
         * Sets the default connect timeout for new connections. A value of 0 means no timeout,
         * otherwise values must be between 1 and {@link Integer#MAX_VALUE} when converted to
         * milliseconds.
         */
        public Builder connectTimeout(int timeout) {
            return connectTimeout(timeout, TimeUnit.SECONDS);
        }

        /**
         * Sets the default connect timeout for new connections. A value of 0 means no timeout,
         * otherwise values must be between 1 and {@link Integer#MAX_VALUE} when converted to
         * milliseconds.
         */
        public Builder writeTimeout(int timeout) {
            return writeTimeout(timeout, TimeUnit.SECONDS);
        }

        /**
         * Sets the default read timeout for new connections. A value of 0 means no timeout, otherwise
         * values must be between 1 and {@link Integer#MAX_VALUE} when converted to milliseconds.
         */
        public Builder readTimeout(int timeout) {
            return readTimeout(timeout, TimeUnit.SECONDS);
        }

        /**
         * Attaches {@code tag} to the request. It can be used later to cancel the request. If the tag
         * is unspecified or null, the request is canceled by using the request itself as the tag.
         */
        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        /**
         * open default logcat
         *
         * @param isLog
         * @return
         */
        public Builder addLog(boolean isLog) {
            this.isLog = isLog;
            return this;
        }

        // 初始化API 列表
        public  Builder addApiMapList(final Map<String,String> map){
            if (map == null){
                LogWraper.e("novate","请初始化mapAPIList");
            }
            LogWraper.e("novate",map.toString());
            mMapbuilt = map;
            return this;
        }


        /**
         * open sync default Cookie
         *
         * @param isCookie
         * @return
         */
        public Builder addCookie(boolean isCookie) {
            this.isCookie = isCookie;
            return this;
        }

        /**
         * open default Cache
         *
         * @param isCache
         * @return
         */
        public Builder addCache(boolean isCache) {
            this.isCache = isCache;
            return this;
        }

        public Builder proxy(Proxy proxy) {
            this.proxy = proxy;
            okhttpBuilder.proxy(Utils.checkNotNull(proxy, "proxy == null"));
            return this;
        }

        /**
         * Sets the default write timeout for new connections. A value of 0 means no timeout,
         * otherwise values must be between 1 and {@link TimeUnit #MAX_VALUE} when converted to
         * milliseconds.
         */
        public Builder writeTimeout(int timeout, TimeUnit unit) {
            if (timeout != -1) {
                okhttpBuilder.writeTimeout(timeout, unit);
            } else {
                okhttpBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            }
            return this;
        }
        /**
         * Sets the default read timeout for new connections. A value of 0 means no timeout, otherwise
         * values must be between 1 and {@link Integer#MAX_VALUE} when converted to milliseconds.
         * TimeUnit {@link TimeUnit}
         */
        public Builder readTimeout(int timeout, TimeUnit unit) {
            if (readTimeout > 0) {
                this.readTimeout = timeout;
                okhttpBuilder.readTimeout(readTimeout, unit);
            }
            return this;
        }

        /**
         * Sets the connection pool used to recycle HTTP and HTTPS connections.
         * <p>
         * <p>If unset, a new connection pool will be used.
         */
        public Builder connectionPool(ConnectionPool connectionPool) {
            if (connectionPool == null) throw new NullPointerException("connectionPool == null");
            this.connectionPool = connectionPool;
            return this;
        }

        /**
         * Sets the default connect timeout for new connections. A value of 0 means no timeout,
         * otherwise values must be between 1 and {@link TimeUnit #MAX_VALUE} when converted to
         * milliseconds.
         */
        public Builder connectTimeout(int timeout, TimeUnit unit) {
            this.readTimeout = Utils.checkDuration("timeout", timeout, unit);
            if (timeout >= 0) {
                this.readTimeout = timeout;
                okhttpBuilder.connectTimeout(readTimeout, unit);
            }
            return this;
        }


        /**
         * Set an API base URL which can change over time.
         *
         * @see (HttpUrl)
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = Utils.checkNotNull(baseUrl, "baseUrl == null");
            return this;
        }

        /**
         * Add converter factory for serialization and deserialization of objects.
         */
        public Builder addConverterFactory(Converter.Factory factory) {
            this.converterFactory = factory;
            return this;
        }

        /**
         * Add a call adapter factory for supporting service method return types other than {@link CallAdapter
         * }.
         */
        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            this.callAdapterFactory = factory;
            return this;
        }

        /**
         * Add Header for serialization and deserialization of objects.
         */
        public <T> Builder addHeader(Map<String, T> headers) {
            okhttpBuilder.addInterceptor(new BaseInterceptor(Utils.checkNotNull(headers, "header == null")));
            return this;
        }

        /**
         * Add parameters for serialization and deserialization of objects.
         */
        public <T> Builder addParameters(Map<String, T> parameters) {
            okhttpBuilder.addInterceptor(new BaseInterceptor(Utils.checkNotNull(parameters, "parameters == null")));
            return this;
        }

        /**
         * Returns a modifiable list of interceptors that observe a single network request and response.
         * These interceptors must call {@link Interceptor.Chain#proceed} exactly once: it is an error
         * for a network interceptor to short-circuit or repeat a network request.
         */
        public Builder addInterceptor(Interceptor interceptor) {
            okhttpBuilder.addInterceptor(Utils.checkNotNull(interceptor, "interceptor == null"));
            return this;
        }

        /**
         * The executor on which {@link Call} methods are invoked when returning {@link Call} from
         * your service method.
         * <p/>
         * Note: {@code executor} is not used for {@linkplain #addCallAdapterFactory custom method
         * return types}.
         */
        public Builder callbackExecutor(Executor executor) {
            this.callbackExecutor = Utils.checkNotNull(executor, "executor == null");
            return this;
        }

        /**
         * When calling {@link #create} on the resulting {@link Retrofit} instance, eagerly validate
         * the configuration of all methods in the supplied interface.
         */
        public Builder validateEagerly(boolean validateEagerly) {
            this.validateEagerly = validateEagerly;
            return this;
        }

        /**
         * Sets the handler that can accept cookies from incoming HTTP responses and provides cookies to
         * outgoing HTTP requests.
         * <p/>
         * <p>If unset, {@linkplain NovateCookieManager#NO_COOKIES no cookies} will be accepted nor provided.
         */
        public Builder cookieManager(NovateCookieManager cookie) {
            if (cookie == null) throw new NullPointerException("cookieManager == null");
            this.cookieManager = cookie;
            return this;
        }


        /**
         * skipSSLSocketFactory
         */
        public Builder skipSSLSocketFactory(boolean isSkip) {
            this.isSkip = isSkip;
            return this;
        }

        /**
         *
         */
        public Builder addSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public Builder addHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        public Builder addCertificatePinner(CertificatePinner certificatePinner) {
            this.certificatePinner = certificatePinner;
            return this;
        }


        /**
         * Sets the handler that can accept cookies from incoming HTTP responses and provides cookies to
         * outgoing HTTP requests.
         * <p/>
         * <p>If unset, {@linkplain NovateCookieManager#NO_COOKIES no cookies} will be accepted nor provided.
         */
        public Builder addSSL(String[] hosts, int[] certificates) {
            if (hosts == null) throw new NullPointerException("hosts == null");
            if (certificates == null) throw new NullPointerException("ids == null");
            addSSLSocketFactory(NovateHttpsFactroy.getSSLSocketFactory(context, certificates));
            addHostnameVerifier(NovateHttpsFactroy.getHostnameVerifier(hosts));
            return this;
        }

        public Builder addNetworkInterceptor(Interceptor interceptor) {
            okhttpBuilder.addNetworkInterceptor(interceptor);
            return this;
        }

        /**
         * setCache
         *
         * @param cache cahe
         * @return Builder
         */
        public Builder addCache(Cache cache) {
            int maxStale = 60 * 60 * 24 * 3;
            return addCache(cache, maxStale);
        }

        /**
         * @param cache
         * @param cacheTime ms
         * @return
         */
        public Builder addCache(Cache cache, final int cacheTime) {
            addCache(cache, String.format("max-age=%d", cacheTime));
            return this;
        }

        /**
         * @param cache
         * @param cacheControlValue Cache-Control
         * @return
         */
        private Builder addCache(Cache cache, final String cacheControlValue) {
            REWRITE_CACHE_CONTROL_INTERCEPTOR = new CacheInterceptor(mContext, cacheControlValue);
            REWRITE_CACHE_CONTROL_INTERCEPTOR_OFFLINE = new CacheInterceptorOffline(mContext, cacheControlValue);
            addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
            addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR_OFFLINE);
            addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR_OFFLINE);
            this.cache = cache;
            return this;
        }

        /**
         * Create the {@link Retrofit} instance using the configured values.
         * <p/>
         * Note: If neither {@link #client} nor {@link #callFactory} is called a default {@link
         * OkHttpClient} will be created and used.
         */
        public Novate build() {

            if (baseUrl == null) {
                throw new IllegalStateException("Base URL required.");
            }

            if (okhttpBuilder == null) {
                throw new IllegalStateException("okhttpBuilder required.");
            }

            if (retrofitBuilder == null) {
                throw new IllegalStateException("retrofitBuilder required.");
            }
            /** set Context. */
            mContext = context;
           /**
            * ConfigLoader.init.
            * */
            ConfigLoader.init(context);
            /**
             * Set a fixed API base URL.
             *
             * @see #baseUrl(HttpUrl)
             */
            retrofitBuilder.baseUrl(baseUrl);

            /** Add converter factory for serialization and deserialization of objects. */
            if (converterFactory == null) {
                converterFactory = GsonConverterFactory.create();
            }

            retrofitBuilder.addConverterFactory(converterFactory);
            /**
             * Add a call adapter factory for supporting service method return types other than {@link
             * Call}.
             */
            if (callAdapterFactory == null) {
                callAdapterFactory = RxJava2CallAdapterFactory.create();
            }
            retrofitBuilder.addCallAdapterFactory(callAdapterFactory);

            LogWraper.setDebug(isLog && !BuildConfig.DEBUG);

            if (tag != null) {
                okhttpBuilder.addInterceptor(new RequestInterceptor<>(tag));
            }

            if (isLog) {
                okhttpBuilder.addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS));

                okhttpBuilder.addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            }

            if (isLog) {
                okhttpBuilder.addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS));

                okhttpBuilder.addNetworkInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            }

            if (isSkip) {
                okhttpBuilder.sslSocketFactory(NovateHttpsFactroy.getSSLSocketFactory(),
                        NovateHttpsFactroy.creatX509TrustManager());

                okhttpBuilder.hostnameVerifier(NovateHttpsFactroy.creatSkipHostnameVerifier());
            }

            if (!isSkip && sslSocketFactory != null) {
                okhttpBuilder.sslSocketFactory(sslSocketFactory);
            }

            if (!isSkip && sslSocketFactory != null) {
                okhttpBuilder.sslSocketFactory(sslSocketFactory);
            }

            if (hostnameVerifier != null) {
                okhttpBuilder.hostnameVerifier(hostnameVerifier);
            }


            if (httpCacheDirectory == null) {
                httpCacheDirectory = new File(mContext.getCacheDir(), "Novate_Http_cache");
            }

            if (isCache) {
                try {
                    if (cache == null) {
                        cache = new Cache(httpCacheDirectory, caheMaxSize);
                    }

                    addCache(cache);

                } catch (Exception e) {
                    Log.e("OKHttp", "Could not create http cache", e);
                }
                if (cache == null) {
                    cache = new Cache(httpCacheDirectory, caheMaxSize);
                }
            }

            if (cache != null) {
                okhttpBuilder.cache(cache);
            }

            /**
             * Sets the connection pool used to recycle HTTP and HTTPS connections.
             *
             * <p>If unset, a new connection pool will be used.
             */
            if (connectionPool == null) {

                connectionPool = new ConnectionPool(DEFAULT_MAXIDLE_CONNECTIONS, DEFAULT_KEEP_ALIVEDURATION, TimeUnit.SECONDS);
            }
            okhttpBuilder.connectionPool(connectionPool);

            /**
             * Sets the HTTP proxy that will be used by connections created by this client. This takes
             * precedence over {@link #proxySelector}, which is only honored when this proxy is null (which
             * it is by default). To disable proxy use completely, call {@code setProxy(Proxy.NO_PROXY)}.
             */
            if (proxy == null) {
                okhttpBuilder.proxy(proxy);
            }

            /**
             * Sets the handler that can accept cookies from incoming HTTP responses and provides cookies to
             * outgoing HTTP requests.
             *
             * <p>If unset, {@link Novate NovateCookieManager#NO_COOKIES no cookies} will be accepted nor provided.
             */
            if (isCookie && cookieManager == null) {
                //okhttpBuilder.cookieJar(new NovateCookieManger(context));
                okhttpBuilder.cookieJar(new NovateCookieManager(new CookieCacheImpl(), new SharedPrefsCookiePersistor(context)));

            }

            if (cookieManager != null) {
                okhttpBuilder.cookieJar(cookieManager);
            }

            /**
             *okhttp3.Call.Factory callFactory = this.callFactory;
             */
            if (callFactory != null) {
                retrofitBuilder.callFactory(callFactory);
            }


            /**
             * create okHttpClient
             */
            okHttpClient = okhttpBuilder.build();


            /**
             * create Retrofit
             */

            retrofitBuilder.client(okHttpClient);

            if (baseUrl != null) {
                retrofitBuilder.baseUrl(baseUrl);
                retrofit = retrofitBuilder.build();
            }else {
                LogWraper.e("navate baseUrl:" , "111111:"+baseUrl);
                retrofitBuilder.baseUrl(baseUrl);
                retrofit = retrofitBuilder.build();
            }


            /**
             *create BaseApiService;
             */
            apiManager = retrofit.create(BaseApiService.class);

            return new Novate(callFactory, baseUrl, headers, parameters, apiManager, converterFactories, adapterFactories,
                    callbackExecutor, validateEagerly,mMapbuilt,context);
        }
    }


    /**
     * Retry
     */
    public class RetryWithDelay implements
            Function<Observable<? extends Throwable>, Observable<?>> {

        private final int maxRetries;
        private final int retryDelayMillis;
        private int retryCount;

        public RetryWithDelay(int maxRetries, int retryDelayMillis) {
            this.maxRetries = maxRetries;
            this.retryDelayMillis = retryDelayMillis;
        }


        @Override
        public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
            return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                @Override
                public ObservableSource<?> apply(Throwable throwable) throws Exception {
                    if (++retryCount <= maxRetries) {
                                // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                                LogWraper.e("novate", "Novate get error, it will try after " + retryDelayMillis
                                        + " millisecond, retry count: " + retryCount);
                                return Observable.timer(retryDelayMillis,
                                        TimeUnit.MILLISECONDS);
                            }
                            // Max retries hit. Just pass the error along.
                            return Observable.error(throwable);
                }
            });
        }
    }





    /**
     * ResponseCallBack <T> Support your custom data model
    * 兼容1.3.3.2以下版本 更高以上版本已过时
     */
   @Deprecated
    public interface ResponseCallBack<T> {

        public void onStart();

        public void onCompleted();

        public abstract void onError(Throwable e);

        @Deprecated
        public abstract void onSuccee(NovateResponse<T> response);

        public void onsuccess(int code, String msg, T response, String originalResponse);

    }
}


