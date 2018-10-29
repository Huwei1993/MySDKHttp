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
package com.charles.httpsdk.novate.callback;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.charles.httpsdk.novate.Throwable;
import com.charles.httpsdk.novate.config.ConfigLoader;
import com.charles.httpsdk.novate.util.LogWraper;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.ResponseBody;


/**
 * ResponseCallback
 * Created by Tamic on 2017-05-02.
 * @param <T>  result T
 * @param <E>  E response
 */
public abstract class ResponseCallback<T, E> implements IGenericsConvert<E> {
    protected int code = -1;
    protected T data = null;
    protected  String msg = "";
    protected String message = "";
    protected String dataStr = "";
    protected JSONObject jsonObject = null;
    protected Object tag;
    protected Handler handler;
    protected String TAG ="novate  ResponseCallback";
    private Context context;

    public Handler getHandler() {
        return handler == null ? handler = new Handler(Looper.getMainLooper()) : null;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ResponseCallback(Object tag) {
        this.tag = tag;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public ResponseCallback() {
        if (handler == null) handler = new Handler(Looper.getMainLooper());
    }

    /**
     * UI Thread
     *
     */
    public void onStart(Object tag) {
    }

    /**
     * UI Thread
     *
     * @param
     */
    public void onCompleted(Object tag) {
    }

    /**
     * UI Thread
     *
     * @param progress
     */
    public void onProgress(Object tag, float progress, long transfered, long total) {
    }

    /**
     * UI Thread
     * @param tag
     * @param progress
     * @param speed
     * @param transfered
     * @param total
     */
    public void onProgress(Object tag, int progress, long speed, long transfered, long total) {
    }

    /**
     * 子类可以复写 默认不做后端数据校验 子类指定规则
     * if you parse reponse code in onHandleResponse, you should make this method return true.
     *
     * @param responseBody
     * @return
     */
    public boolean isReponseOk(Context context,Object tag, ResponseBody responseBody) {




        return true;
    }


    /**
     * Thread Pool Thread
     *
     * @param response
     */
    public abstract T onHandleResponse(ResponseBody response) throws Exception;

    @Override
    public <T> T transform(E response, Class<T> classOfT)  throws Exception {
        return (T) response;
    }

    /**
     * Thread Pool Thread
     * @param tag
     * @param e
     */
    public abstract void onError(Object tag, Throwable e);

    public abstract void onCancel(Object tag, Throwable e);
    /**
     * Thread Pool Thread
     *
     * @param response
     */
    public abstract void onNext(Object tag, Call call, T response);


    /**
     * OnRelease 子类可以复写
     */
    public void onRelease() {
    }

    public static ResponseCallback CALLBACK_DEFAULT = new ResponseCallback() {

        @Override
        public Object transform(Object response, Class classOfT) {
            return response;
        }


        @Override
        public Object onHandleResponse(ResponseBody response) throws Exception {
            return  response;
        }

        @Override
        public void onError(Object tag, Throwable e) {
            onRelease();
        }

        @Override
        public void onCancel(Object tag, Throwable e) {

        }

        @Override
        public void onNext(Object tag, Call call, Object response) {
            LogWraper.e(TAG, response.toString());
            try {
                jsonObject = new JSONObject(response.toString());
                code = jsonObject.optInt("code");
                message = jsonObject.optString("message");
                if (TextUtils.isEmpty(message)) {
                    message = jsonObject.optString("msg");
                }

                dataStr = jsonObject.optString("data");
                if (TextUtils.isEmpty(msg)) {
                    dataStr = jsonObject.optString("result");
                }

                if (TextUtils.isEmpty(message)) {
                    message = jsonObject.optString("error");
                }
                if (code != 200 && !ConfigLoader.checkSucess(ConfigLoader.getContext(),code)) {
                    Throwable throwable = new Throwable(null, code, message,dataStr);
                    throwable.setMessage(message);
                    throwable.setCode(code);
                    throwable.setData(dataStr);
                    onError(tag, throwable);
                }
            }catch (Exception e){
                e.printStackTrace();
                Throwable throwable = new Throwable(e, code, message,dataStr);
                throwable.setMessage(message);
                throwable.setCode(code);
                throwable.setData(dataStr);
                onError(tag, throwable);
            }
        }

        @Override
        public void onCompleted(Object tag) {
            super.onCompleted(tag);
            onRelease();
        }
    };

}