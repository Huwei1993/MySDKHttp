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
package com.charles.httpsdk.novate.exception;

import android.net.ParseException;
import android.text.TextUtils;

import com.charles.httpsdk.novate.Throwable;
import com.charles.httpsdk.novate.config.ConfigLoader;
import com.charles.httpsdk.novate.util.LogWraper;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.net.ssl.SSLPeerUnverifiedException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Tamic on 2016-08-12.
 */
public class NovateException {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int ACCESS_DENIED = 302;
    private static final int HANDEL_ERRROR = 417;

    public static Throwable handleException(java.lang.Throwable e) {

        LogWraper.e("Novate", e.getMessage());
        String detail = "";
        if (e.getCause() != null) {
            detail = e.getCause().getMessage();
        }
        LogWraper.e("Novate", detail);
        Throwable ex;
        if (!(e instanceof ServerException) && e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new Throwable(e, httpException.code());
            ex.setServerError(false);
            switch (ex.getCode()) {

                case UNAUTHORIZED:
                    ex.setMessage("未授权的请求");
                    break;
                case FORBIDDEN:
                    ex.setMessage("禁止访问");
                    break;
                case NOT_FOUND:
                    ex.setMessage("服务器地址未找到");
                    break;
                case REQUEST_TIMEOUT:
                    ex.setMessage("请求超时");
                    break;
                case GATEWAY_TIMEOUT:
                    ex.setMessage("网关响应超时");
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.setMessage("服务器出错");
                case BAD_GATEWAY:
                    ex.setMessage("无效的请求");
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.setMessage("服务器不可用");
                    break;
                case ACCESS_DENIED:
                    ex.setMessage("网络错误");
                    break;
                case HANDEL_ERRROR:
                    ex.setMessage("接口处理失败");
                    break;

                default:
                    if (TextUtils.isEmpty(ex.getMessage())) {
                        ex.setMessage(e.getMessage());
                        break;
                    }

                    if (TextUtils.isEmpty(ex.getMessage()) && e.getLocalizedMessage() != null) {
                        ex.setMessage(e.getLocalizedMessage());
                        break;
                    }
                    if (TextUtils.isEmpty(ex.getMessage()) ) {
                        ex.setMessage("未知错误");
                    }
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new Throwable(resultException, resultException.code);
            ex.setServerError(true);
            HashMap<String, String> errorConfigs = ConfigLoader.getErrorConfig();
            if (errorConfigs != null && errorConfigs.containsKey(String.valueOf(resultException.code))) {
                ex.setMessage(errorConfigs.get(String.valueOf(resultException.code)));
                return ex;
            }
            ex.setMessage(resultException.getMessage());
            ex.setServerError(false);
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new Throwable(e, ERROR.PARSE_ERROR);
            ex.setMessage("解析错误");
            ex.setServerError(false);
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new Throwable(e, ERROR.NETWORD_ERROR);
            ex.setMessage("连接失败");
            ex.setServerError(false);
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new Throwable(e, ERROR.SSL_ERROR);
            ex.setServerError(false);
            ex.setMessage("证书验证失败");
            ex.setServerError(false);
            return ex;
        } else if (e instanceof java.security.cert.CertPathValidatorException) {
            LogWraper.e("Novate", e.getMessage());
            ex = new Throwable(e, ERROR.SSL_NOT_FOUND);
            ex.setMessage("证书路径没找到");
            ex.setServerError(false);
            return ex;
        } else if (e instanceof SSLPeerUnverifiedException) {
            LogWraper.e("Novate", e.getMessage());
            ex = new Throwable(e, ERROR.SSL_NOT_FOUND);
            ex.setMessage("无有效的SSL证书");
            ex.setServerError(false);
            return ex;

        } else if (e instanceof ConnectTimeoutException){
            ex = new Throwable(e, ERROR.TIMEOUT_ERROR);
            ex.setMessage("连接超时");
            ex.setServerError(false);
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new Throwable(e, ERROR.TIMEOUT_ERROR);
            ex.setMessage("连接超时");
            ex.setServerError(false);
            return ex;
        } else if (e instanceof java.lang.ClassCastException) {
            ex = new Throwable(e, ERROR.FORMAT_ERROR);
            ex.setMessage("类型转换出错");
            ex.setServerError(false);
            return ex;
        } else if (e instanceof NullPointerException) {
            ex = new Throwable(e, ERROR.NULL);
            ex.setMessage("返回数据为 null");
            ex.setServerError(false);
            return ex;
        } else if (e instanceof FormatException) {
            FormatException resultException = (FormatException) e;
            ex = new Throwable(e, resultException.code);
            ex.setMessage(resultException.message);
            ex.setServerError(false);
            return ex;
        } else if (e instanceof UnknownHostException){
            LogWraper.e("Novate", e.getMessage());
            ex = new Throwable(e, NOT_FOUND);
            ex.setMessage("服务器地址未找到,请检查网络或Url");
            ex.setServerError(false);
            return ex;
        } else {
            LogWraper.e("Novate", e.getMessage());
            ex = new Throwable(e, ERROR.UNKNOWN);
            ex.setMessage(e.getMessage());
            ex.setServerError(false);
            return ex;
        }
    }


    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;

        /**
         * 证书未找到
         */
        public static final int SSL_NOT_FOUND = 1007;

        /**
         * 出现空值
         */
        public static final int NULL = -100;

        /**
         * 格式错误
         */
        public static final int FORMAT_ERROR = 1008;
    }

    public static class Http {
        /*==========对应HTTP的状态码=================*/
        public static final int UNAUTHORIZED = 401;
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int REQUEST_TIMEOUT = 408;
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int BAD_GATEWAY = 502;
        public static final int SERVICE_UNAVAILABLE = 503;
        public static final int GATEWAY_TIMEOUT = 504;
        /*=======================================*/
    }

    public static class Response {
        /*===========Response响应码================*/
        //HTTP请求成功状态码
        public static final int HTTP_SUCCESS = 200;
        //AccessToken错误或已过期
        public static final int ACCESS_TOKEN_EXPIRED = 10001;
        //RefreshToken错误或已过期
        public static final int REFRESH_TOKEN_EXPIRED = 10002;
        //帐号在其它手机已登录
        public static final int OTHER_PHONE_LOGINED = 10003;
        //时间戳过期
        public static final int TIMESTAMP_ERROR = 10004;
        //缺少授权信息,没有AccessToken
        public static final int NO_ACCESS_TOKEN = 10005;
        //签名错误
        public static final int SIGN_ERROR = 10006;
        //密码错误
        public static final int ERR_PWD = 1020100902;
        /*============================================*/
    }


}

