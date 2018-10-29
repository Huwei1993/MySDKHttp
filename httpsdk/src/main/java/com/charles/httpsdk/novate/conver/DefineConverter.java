package com.charles.httpsdk.novate.conver;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


/**
 * Created by xiao on 2018/5/16.
 *
 * @author xiao
 * @date 2018/5/16
 */

public class DefineConverter extends Converter.Factory {
    public static DefineConverter create() {
        return create(new Gson());
    }

    public static DefineConverter create(Gson gson) {
        return new DefineConverter(gson);
    }

    private final Gson gson;

    private DefineConverter(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DecodeRequestBodyConverter<>(gson, adapter);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new DecodeResponseBodyConverter<>(adapter);
    }

}
