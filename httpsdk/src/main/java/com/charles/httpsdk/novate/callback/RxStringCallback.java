package com.charles.httpsdk.novate.callback;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.charles.httpsdk.novate.Throwable;
import com.charles.httpsdk.novate.config.ConfigLoader;
import com.charles.httpsdk.novate.util.LogWraper;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.ResponseBody;

/**
 * RxStringCallback  字符串解析器
 * <p>
 * Created by Tamic on 2017-5-30.
 * ink :https://github.com/Tamicer/Novate
 */
public abstract class RxStringCallback extends ResponseCallback<String, ResponseBody> {


    @Override
    public String onHandleResponse(ResponseBody response) throws Exception {
        String responseString = new String(response.bytes());
        Log.d("Novate", responseString);
        return responseString;
    }

    @Override
    public boolean isReponseOk(Context context, Object tag, ResponseBody responseBody) {
        String response = null;
        try {
            response = new String(responseBody.bytes());
            LogWraper.e(TAG, response);
            jsonObject = new JSONObject(response);
            code = jsonObject.optInt("code");
            msg = jsonObject.optString("msg");
            message = jsonObject.optString("message");
            dataStr = jsonObject.optString("data");
            if (TextUtils.isEmpty(dataStr)) {
                dataStr = jsonObject.optString("result");
            }
            if (TextUtils.isEmpty(dataStr) && jsonObject.optJSONArray("data") != null) {
                dataStr = jsonObject.optJSONArray("data").toString();
            }

            if (TextUtils.isEmpty(dataStr) && jsonObject.optJSONArray("result") != null) {
                dataStr = jsonObject.optJSONArray("result").toString();
            }

            if (TextUtils.isEmpty(msg)) {
                msg = jsonObject.optString("message");
            } else if (TextUtils.isEmpty(message)) {
                msg = jsonObject.optString("msg");
            }

            if (TextUtils.isEmpty(msg)) {
                msg = jsonObject.optString("error");
            }
            if (code == 200 || ConfigLoader.checkSucess(ConfigLoader.getContext(), code)) {
                onNext(tag, response);
                return true;
            } else {
                Throwable throwable = new Throwable(null, code, message, dataStr);
                throwable.setServerError(true);
                onError(tag, throwable);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Throwable throwable = new Throwable(e, code, message, dataStr);
            throwable.setServerError(true);
            onError(tag, throwable);
        }
        return false;
    }

    @Override
    public void onNext(Object tag, Call call, String response) {

    }

    public abstract void onNext(Object tag, String response);
}
