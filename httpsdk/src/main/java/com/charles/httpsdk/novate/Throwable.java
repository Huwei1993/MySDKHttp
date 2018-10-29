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

/**
 * Created by Tamic on 2016-11-03.
 */

public class Throwable extends Exception {

    private int code;
    private String message;
    private String data;

    // 是否是服务器异常 true 是服务器返回到数据异常   false为 http 访问网络异常（不经过服务器返回code）
    private boolean isServerError = true;

    public Throwable(java.lang.Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public Throwable(java.lang.Throwable throwable, int code, String message) {
        super(throwable);
        this.code = code;
        this.message = message;
    }

    public Throwable(java.lang.Throwable throwable, int code, String message,String data) {
        super(throwable);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isServerError() {
        return isServerError;
    }

    public void setServerError(boolean isServerError) {
        this.isServerError = isServerError;
    }

}
