/*
 *    Copyright (C) 2017 Tamic
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
import android.text.TextUtils;

import com.charles.httpsdk.novate.NovateResponse;
import com.charles.httpsdk.novate.Throwable;
import com.charles.httpsdk.novate.config.ConfigLoader;
import com.charles.httpsdk.novate.util.LogWraper;
import com.charles.httpsdk.novate.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * RxGenericsCallback<T> 泛型回调
 * Created by Tamic on 2016/6/23.
 */
public abstract class RxGenericsCallback<T, E> extends ResponseCallback<T, E> {
    private static final String TAG = RxGenericsCallback.class.getSimpleName();
    private Context mContext;

    public RxGenericsCallback(Context context) {
        mContext = context;
    }

    public RxGenericsCallback() {
    }

    @Override
    public T onHandleResponse(ResponseBody response) throws Exception {
//        Type entityClass =  ((ParameterizedType)foo.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String strResponse = new String(response.bytes());
        jsonObject = new JSONObject(strResponse);
        this.code = jsonObject.optInt("code");
        this.msg = jsonObject.optString("msg");
        this.message = jsonObject.optString("message");
        if(TextUtils.isEmpty(this.msg)) {
            this.msg = jsonObject.optString("message");
        } else if(TextUtils.isEmpty(this.message)) {
            this.msg = jsonObject.optString("msg");
        }

        if(TextUtils.isEmpty(this.msg)) {
            this.msg = jsonObject.optString("error");
        }
        dataStr = jsonObject.optString("data");
        if (TextUtils.isEmpty(dataStr)) {
            dataStr = jsonObject.optString("result");
        }

        if (entityClass == String.class) {
            return (T)dataStr;
        }else {
            return transform(strResponse, entityClass);
        }
    }

    @Override
    public boolean isReponseOk(Context context, Object tag, ResponseBody responseBody) {

        return super.isReponseOk(context, tag, responseBody);
    }

    @Override
    public void onNext(final Object tag, Call call, final T response) {
        if (response != null) {
            if (Utils.checkMain()) {
                if (code == 200 || ConfigLoader.checkSucess(ConfigLoader.getContext(),code)) {
                    onNext(tag, code, msg, response);
                }else {
                    onError(tag, new Throwable(null, code, message,dataStr));
                }
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (code == 200 || ConfigLoader.checkSucess(ConfigLoader.getContext(),code)) {
                            onNext(tag, code, msg, response);
                        }else {
                            onError(tag, new Throwable(null, code, message,dataStr));
                        }
                    }
                });
            }
        }
    }

    public abstract void onNext(Object tag, int code, String message, T response);

    public T transform(String response, final Type classOfT) throws ClassCastException {

        if (classOfT == NovateResponse.class) {
            return (T) new Gson().fromJson(response, classOfT);
        }

        JSONObject jsonObject = null;
        LogWraper.e(TAG, response);
        try {
            jsonObject = new JSONObject(response);
            code = jsonObject.optInt("code");
            msg = jsonObject.optString("msg");
            message = jsonObject.optString("message");
            if (TextUtils.isEmpty(msg)) {
                msg = jsonObject.optString("message");
            } else if (TextUtils.isEmpty(message)) {
                msg = jsonObject.optString("msg");
            }
            if (TextUtils.isEmpty(msg)) {
                msg = jsonObject.optString("error");
            }
            dataStr = jsonObject.optString("data").toString();
            if (TextUtils.isEmpty(dataStr)) {
                dataStr = jsonObject.optString("result").toString();
            }
            if ((code == 200 || ConfigLoader.checkSucess(ConfigLoader.getContext(),code)) && (!jsonObject.optString("data").isEmpty() || !jsonObject.optString("result").isEmpty()) ) {
                    if (dataStr.charAt(0) == '{') {
                        data = (T) new Gson().fromJson(dataStr, classOfT);
                    } else if (dataStr.charAt(0) == '[') {
                        dataStr = jsonObject.optJSONArray("data").toString();
                        if (TextUtils.isEmpty(dataStr)) {
                            dataStr = jsonObject.optJSONArray("result").toString();
                        }
                        Type collectionType = new TypeToken<List<T>>() {
                        }.getType();
                        data = (T) new Gson().fromJson(dataStr, collectionType);
                    }
                } else {
                LogWraper.e(TAG, "code"+ code);
                onError(tag, new  Throwable(null, code, message,dataStr));
            }
        } catch (Exception e){
            e.printStackTrace();
            LogWraper.e(TAG, "解析错误" +e.getMessage());
            onError(tag, new  Throwable(e, code, message,dataStr));
        }
        return data;
    }
}

