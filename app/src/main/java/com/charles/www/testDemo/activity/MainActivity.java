package com.charles.www.testDemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.charles.www.testDemo.API;
import com.charles.www.testDemo.PATH;
import com.charles.www.testDemo.R;
import com.charles.www.testDemo.bean.bean;
import com.charles.www.testDemo.camera.ImageGridActivity;
import com.charles.www.testDemo.camera.ImagePicker;
import com.charles.www.testDemo.camera.loader.GlideImageLoader;
import com.charles.www.testDemo.util.AppUtil;
import com.charles.www.testDemo.util.RxBus;
import com.charles.www.testDemo.util.RxBusEventBean;
import com.charles.www.testDemo.util.Utils;
import com.charles.www.testDemo.view.CropImageView;
import com.charles.www.testDemo.view.WidthTextViewActivity;
import com.charles.httpsdk.novate.Throwable;
import com.charles.httpsdk.novate.callback.RxGenericsCallback;
import com.charles.httpsdk.novate.callback.RxStringCallback;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.functions.Func0;


public class MainActivity extends AppCompatActivity {
    private Map<String, Object> initParames = new HashMap<>();
    private Context mContext;
    private TextView mTVtextTime;
    int time = 2000;
    private static final String mTAG = MainActivity.class.getSimpleName();
    private   String[] strings1 = {"Hello", "World"};
    private String[] strings2 = {"Hello", "RxJava"};
    @BindView(R.id.tv_textView) TextView tvTextView;
    private Disposable rxSbscription;

    //    Novate novate = new Novate.Builder(this)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mTVtextTime = findViewById(R.id.tv_text_time);
        mTVtextTime.setText(String.valueOf(time));
        ButterKnife.bind(this);
        getRxBusEvent();
        //        Novate novate = new Novate.Builder(getApplicationContext())
//                .addSSLSocketFactory()
//                .build();

//        API.getmNovate().rxGetUrl("v2/movie/top250", new RxGenericsCallback<MovieModel, ResponseBody>() {
//
//            @Override
//            public void onError(Object tag, Throwable e) {
//                Log.v("MainActivity： error", e.getMessage());
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object tag, int code, String message, MovieModel response) {
//                Log.v("MainActivity：", response.getTitle());
//            }
//        });
    }


    public void btnOnclick1(View view) {
//        API.getmNovate("http://10.22.218.162/").rxGetUrl("cms/pages/relation/pageV1?id=AP1706A047&playDate=2018-2-26",initParames, new RxResultCallback<bean>() {
//// https://m1.ocj.com.cn/analysis/check_period,
//            @Override
//            public void onError(Object tag, Throwable e) {
//                Log.v("MainActivity： error", e.toString());
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object tag, int code, String message, bean response) {
//                Log.v("bean  MainActivity：", response.toString());
//            }
//        });
//        Map<String, Object> parames = new HashMap<>();
//        parames.put("id", "AP1711A002");
        API.getmNovate().rxGetUrl("/api/interactions/messages/count", new RxStringCallback() {
            @Override
            public void onNext(Object tag, String response) {
                Toast.makeText(mContext, response + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Object tag, Throwable e) {
                if (e.isServerError()){
                    Toast.makeText(mContext, e.getCode() + " 服务器异常  " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext, e.getCode() + " Http 异常 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancel(Object tag, Throwable e) {

            }

            @Override
            public void onCompleted(Object tag) {

                Log.v(mTAG,"onComplete");
            }
        });


        Map<String,Object> params1 = new HashMap<>();
        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        map.put("orderno","123145646");
        map.put("itemCode","");
        list.add(map);
        params1.put("ordernew", list.toString());
        Log.v(mTAG,new Gson().toJson(list));
    }

    public void btnOnclick2(View view) {
        Map<String, Object> parames = new HashMap<>();
//        parames.put("mobile","18779117795");
//        parames.put("purpose","quick_register_context");
        parames.put("otherInfo", "18779117795");
//        Log.v("mobile",parames.get("mobile")+"");
//        Log.v("mobile",parames.getOrDefault("mobile","110")+"");

//        API.getmNovate("https://m1.ocj.com.cn/").rxGet("analysis/check_period",initParames, new RxResultCallback<CheckPeriodBean>() {
//            // https://m1.ocj.com.cn/analysis/check_period,
//            @Override
//            public void onError(Object tag, Throwable e) {
//                Log.v("MainActivity： error", e.toString());
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object tag, int code, String message, CheckPeriodBean response) {
//                Log.v("MainActivity：", response.getCheck_period());
//            }
//        });

//        Map<String,String> mapstr = new HashMap<>();
        Gson gson = new Gson();
        String gsonstr = gson.toJson(parames);
        API.getmNovate().rxGetUrl("/api/members/members/check_other_info", parames, new RxGenericsCallback<String, ResponseBody>() {
            @Override
            public void onNext(Object o, int code, String message, String s) {
                Toast.makeText(mContext, "message:" + message + "data " + s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Object o, Throwable e) {
                Toast.makeText(mContext, "message:" + e.getMessage() + "code " + e.getCode(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Object o, Throwable throwable) {

            }
        });

        List<Map<String, Object>> parames1 = new ArrayList<>();
        Map<String, Object> maps = new HashMap<>();

        parames1.add(maps);
//        API.getmNovate().rxBodyGet(urlAPi, parames1, new RxStringCallback() {
//            @Override
//            public void onNext(Object tag, String response) {
//                Toast.makeText(MainActivity.this,response,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Object tag, Throwable e) {
//                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//        });
        Map<String, Object> parames2 = new HashMap<>();
        parames2.put("id", "AP1711A002");
//        API.getmNovate("http://10.22.218.162").rxGetUrl("/cms/pages/relation/pageV1", parames2,new RxGenericsCallback<Object, ResponseBody>() {
//            @Override
//            public void onNext(Object tag, int code, String message, Object response) {
//                Toast.makeText(MainActivity.this,
//                        response.toString(),Toast.LENGTH_SHORT).show();
//
//
//            }
//
//
//            @Override
//            public void onError(Object tag, Throwable e) {
//                Toast.makeText(MainActivity.this,
//                        e.getMessage(),Toast.LENGTH_SHORT).show();
//
//
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//        });


//        API.getmNovate().rxGet("https://m1.ocj.com.cn/", new RxResultCallback<Object>() {
//            @Override
//            public void onError(Object tag, Throwable e) {
//
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object tag, int code, String message, Object response) {
//
//            }
//        });

        Map<String, RequestBody> pa = new HashMap<>();
//        API.getmNovate().uploadFlies("asdasasd",pa, new RxResultCallback<bean>() {
//            @Override
//            public void onError(Object tag, Throwable e) {
//
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object tag, int code, String message, bean response) {
//
//            }
//        });


//        API.getmNovate("https://m1.ocj.com.cn/").rxPostKeySp(PATHAPIID.GetCartNum,"url", new RxResultCallback<List<WhiteBean>>() {
//            @Override
//            public void onError(Object tag, Throwable e) {
//                Log.e(TAG,"e:" + e.getMessage());
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object tag, int code, String message, List<WhiteBean> response) {
//
//            }
//        });

//        API.getmNovate("http://10.22.218.162").rxGetUrl("/api/members/members/check_address", new RxListCallback<List<ElectronBean>>() {
//            @Override
//            public void onError(Object tag, Throwable e) {
//                Toast.makeText(mContext,e.getMessage()  +e.getCode(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object tag, int code, String message, List<ElectronBean> response) {
//                Toast.makeText(mContext,response.size()+"",Toast.LENGTH_SHORT).show();
//            }
//        });


//        parames.put("id", "AP1711A001");
//        API.getmNovate().rxGet("/cms/pages/relation/pageV1", new RxStringCallback() {
//            @Override
//            public void onNext(Object tag, String response) {
//                Toast.makeText(mContext, response + "", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Object tag, Throwable e) {
//                Toast.makeText(mContext, e.getCode() + "  " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//        });
//        Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> observer) {
//                try {
//                    if (!observer.isUnsubscribed()) {
//                        for (int i = 1; i < 5; i++) {
//                            observer.onNext(i);
//                        }
//                        observer.onCompleted();
//                    }
//                } catch (Exception e) {
//                    observer.onError(e);
//
//                }
//            }
//        }).subscribe(new Subscriber<Integer>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(java.lang.Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Toast.makeText(mContext, "onNext" + integer , Toast.LENGTH_SHORT).show();
//            }
//        });


        File mLeftFile = null;
        File mRightFile = null;
        Map<String, Object> map = new HashMap<>();
        map.put("cardJustImg", "file:///storage/emulated/0/Android/data/com.ocj.oms.mobile/cache/cardJustImg.png");
        map.put("cardBackImg", "file:///storage/emulated/0/Android/data/com.ocj.oms.mobile/cache/cardBackImg.png");
        map.put("cardNo", "123456789");
        map.put("cardName", "ocj123456");
        map.put("orderNo", "ocj123456789");
        if (map.get("cardJustImg") != null && !TextUtils.isEmpty(map.get("cardJustImg").toString())) {
//                mLeftFile = creatFile(map.get("cardJustImg").toString(),"cardJustImg");
            mLeftFile = new File(map.get("cardJustImg").toString());

        }
        if (!TextUtils.isEmpty(map.get("cardBackImg").toString())) {
//                mRightFile =  mLeftFile = creatFile(map.get("cardBackImg").toString(),"cardBackImg");
            mRightFile = mLeftFile = new File(map.get("cardBackImg").toString());
        }
        // 判断逻辑
        if (mLeftFile == null || mRightFile == null) {
            Toast.makeText(this, "图片不存在", Toast.LENGTH_SHORT).show();
//                    callback.invoke("图片不存在");
            return;
        }
        Map<String, RequestBody> params = new HashMap<>();
//        params.put("access_token", createStringBody(OCJPreferencesUtils.getAccessToken()));
        params.put("cardNo", createStringBody(map.get("cardNo").toString()));
        params.put("cardName", createStringBody("h123456"));
        params.put("cardJustImg\"; filename=\"" + mLeftFile.getName() + "", createFileBody(mLeftFile));
        params.put("cardBackImg\"; filename=\"" + mRightFile.getName() + "", createFileBody(mRightFile));
//        params.put("orderNo", createStringBody(""));
//            new ItemsMode(mContext).uploadCardPicture(params, new ApiResultObserver<ResultStr>(mContext) {
//                @Override
//                public void onSuccess(ResultStr apiResult) {
//                    ToastUtils.showShortToast(apiResult.getResult());
//                }
//
//                @Override
//                public void onError(ApiException e) {
//                    ToastUtils.showShortToast("code:"+e.getCode()+"    message:"+e.getMessage());
//                }
//            });
//        API.getmNovate().uploadFliesUrl("/api/orders/orders/resident_upload", params, new RxStringCallback() {
//            @Override
//            public void onNext(Object tag, String response) {
//                Toast.makeText(mContext,response,Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Object tag, Throwable e) {
//                Toast.makeText(mContext,"message:"+e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancel(Object tag, Throwable e) {
//
//            }
//        });


//        Subscription subscription =   API.getmNovate().rxGetKey("",null, new RxResultCallback<ResponseBody>(){
//
//           @Override
//           public void onError(Object tag, Throwable e) {
//
//           }
//
//           @Override
//           public void onCancel(Object tag, Throwable e) {
//
//           }
//
//           @Override
//           public void onNext(Object tag, int code, String message, ResponseBody response) {
//
//           }
//       });


    }

    public void btnOnclick3(View view) {
        //打开选择,本次允许选择的数量
        initImagePicker();
        ImagePicker.getInstance().setSelectLimit(1);
        Intent intent1 = new Intent(mContext, ImageGridActivity.class);
        intent1.putExtra("profile_setting", "");
        mContext.startActivity(intent1);
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setMultiMode(false);
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(AppUtil.getDisplayWidth(this));                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight((int) (AppUtil.getDisplayHight(this) * 0.357));                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(AppUtil.getDisplayWidth(this));                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY((int) (AppUtil.getDisplayHight(this) * 0.357));                         //保存文件的高度。单位像素
    }


    public void btnOnclick4(final View view) {
        Utils.countdown(time)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mTVtextTime.setText("开始倒计时");
                        mTVtextTime.setVisibility(View.VISIBLE);
                        view.setClickable(false);
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        mTVtextTime.setText(String.valueOf(integer));
                        rxPostNovateMonth(integer+"");

                    }

                    @Override
                    public void onError(java.lang.Throwable e) {
                        Toast.makeText(mContext, "message:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        mTVtextTime.setText("倒计时结束");
                        mTVtextTime.setVisibility(View.GONE);
                        view.setClickable(true);
                    }
                });

    }


    public void btnOnclick5(View view) {
        RxBus.getInstance().post(new RxBusEventBean("132456","rxBus发送数据"));
    }


    private static RequestBody createStringBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }

    private static RequestBody createFileBody(File value) {


        return RequestBody.create(MediaType.parse("multipart/form-data"), value);
    }

    public void btnOnclick6(View view) {
        rxPostNovateMonth("");

    }

    private void rxPostNovateMonth(String str) {
        String json = "[{\"item_code\":\"15057177\",\"unit_code\":\"001\",\"order_qty\":1,\"cart_seq\":22},{\"item_code\":\"15176976\",\"unit_code\":\"001\",\"order_qty\":3,\"cart_seq\":1},{\"item_code\":\"15223694\",\"unit_code\":\"001\",\"order_qty\":1,\"cart_seq\":19},{\"item_code\":\"15225528\",\"unit_code\":\"001\",\"order_qty\":1,\"cart_seq\":16},{\"item_code\":\"15225635\",\"unit_code\":\"001\",\"order_qty\":1,\"cart_seq\":15},{\"item_code\":\"15192520\",\"unit_code\":\"001\",\"order_qty\":1,\"cart_seq\":2},{\"item_code\":\"15233647\",\"unit_code\":\"001\",\"order_qty\":1,\"cart_seq\":12},{\"item_code\":\"15189528\",\"unit_code\":\"001\",\"order_qty\":14,\"cart_seq\":10},{\"item_code\":\"15216961\",\"unit_code\":\"001\",\"order_qty\":1,\"cart_seq\":9}]";

        try {
//        JSONObject jsonObject = new JSONObject(json);
//        List<String> mOrderNoList = new ArrayList<>();
//        Map<String,String> map = new HashMap<>();
////        map.put("orderNo","0002");
//        mOrderNoList.add(json);



//        try {
//            JSONObject jsonArray = new JSONObject(json);

            API.getmNovate().rxJsonPostUrl("/api/orders/check/cart_to_order_stock" + str, json, new RxStringCallback() {
//            API.getmNovate().rxJsonPostUrl("/api/events/restapi/draw_prize_for_order", mOrderNoList.toString(), new RxResultCallback<Throwable>() {
                @Override
                public void onNext(Object tag, String  response) {
                    Toast.makeText(mContext, "response:" + response, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(Object tag, Throwable e) {
                    Toast.makeText(mContext, "message:" + e.getMessage() + "\n" + "code" + e.getCode() + "\n" + "data:" + e.getData(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(Object tag, Throwable e) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.rxCreate,R.id.rxJust,R.id.rxFrom,R.id.rxDefer,R.id.rxEmpty,R.id.rxNever,R.id.rxError,R.id.rxRange,R.id.rxInterval
            ,R.id.rxBuffer,R.id.rxMap,R.id.rxFlatMap,R.id.rxWindow,R.id.rxFilter,R.id.rxTake,R.id.rxSkip,R.id.rxElementAt,R.id.rxDistinct
            ,R.id.rxStartWith,R.id.rxMerge})
    public void onClick(View view){
        final String phone = "12345678911";
        switch (view.getId()){
            case R.id.rxCreate:
                Observable.unsafeCreate(new ObservableSource<String>() {
                    @Override
                    public void subscribe(Observer<? super String> observer) {
                        observer.onNext("Hello");
                        observer.onNext("RxJava");
                        observer.onNext("Nice to meet you");
                    }


                }).map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s;
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.v(mTAG,s);
                    }
                });
                break;
            case R.id.rxJust:
                Observable.just("Hello", "RxJava", "Nice to meet you").subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.v(mTAG +"rxJust" + "onNext-->",s);
                        if (s.equals("RxJava")){
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, WidthTextViewActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                break;
            case R.id.rxFrom:
                String[] strings = {"Hello", "RxJava", "Nice to meet you"};
                Observable.fromArray(strings)
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Log.v(mTAG + "rxFrom" + "onNext-->", s);
                                towebview();
                                Toast.makeText(MainActivity.this, "h1：" + phone.substring(0, 3) + "h1：" + phone.substring(3, 7) + "h1：" + phone.substring(7, 11), Toast.LENGTH_SHORT).show();
                            }

                        }, new Consumer<java.lang.Throwable>() {
                            @Override
                            public void accept(java.lang.Throwable throwable) throws Exception {
                                Log.v(mTAG + "rxFrom" + "onNext-->", throwable.toString());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {

                                Toast.makeText(MainActivity.this, "onComplete()", Toast.LENGTH_SHORT).show();

                            }
                        });
                break;
            case R.id.rxDefer:
                 rxDefertest();
                break;
            case R.id.rxEmpty:
                Observable.empty()
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) throws Exception {
                                System.out.println("onNext--> " + o);
                            }

                        }, new Consumer<java.lang.Throwable>() {
                            @Override
                            public void accept(java.lang.Throwable throwable) {
                                System.out.println("onError--> " + throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() {
                                System.out.println("onComplete");
                            }
                        });
                break;
            case R.id.rxNever:
                Observable.never()
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) {
                                System.out.println("onNext--> " + o);
                            }
                        }, new Consumer<java.lang.Throwable>() {
                            @Override
                            public void accept(java.lang.Throwable throwable) {
                                System.out.println("onError--> " + throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() {
                                System.out.println("onComplete");
                            }
                        });

                break;
            case R.id.rxError:
                Observable.error(new RuntimeException("fuck!"))
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) {
                                System.out.println("onNext--> " + o);
                            }
                        }, new Consumer<java.lang.Throwable>() {
                            @Override
                            public void accept(java.lang.Throwable throwable) {
                                System.out.println("onError--> " + throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() {
                                System.out.println("onComplete");
                            }
                        });
                break;
            case R.id.rxRange:
                Observable.range(3, 8)
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer o) {
                                System.out.println("onNext--> " + o);
                            }
                        }, new Consumer<java.lang.Throwable>() {
                            @Override
                            public void accept(java.lang.Throwable throwable) {
                                System.out.println("onError--> " + throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() {
                                System.out.println("onComplete");
                            }
                        });
                break;
            case R.id.rxInterval:
                Observable.interval(1, TimeUnit.SECONDS)
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long o) {
                                System.out.println("onNext--> " + o);
                            }
                        }, new Consumer<java.lang.Throwable>() {
                            @Override
                            public void accept(java.lang.Throwable throwable) {
                                System.out.println("onError--> " + throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() {
                                System.out.println("onComplete");
                            }
                        });
                try {
                    System.in.read();//阻塞当前线程，防止JVM结束程序
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rxBuffer:
                Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                        .buffer(3)
                        .subscribe(new Consumer<List<Integer>>() {
                            @Override
                            public void accept(List<Integer> o) {
                                System.out.println("onNext--> " + o);
                            }
                        }, new Consumer<java.lang.Throwable>() {
                            @Override
                            public void accept(java.lang.Throwable throwable) {
                                System.out.println("onError--> " + throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() {
                                System.out.println("onComplete");
                            }
                        });
                break;
            case R.id.rxMap:
                Observable.just("Hello", "RxJava", "Nice to meet you")
                        .map(new Function<String, Integer>() { //泛型第一个类型是原数据类型，第二个类型是想要变换的数据类型
                            @Override
                            public Integer apply(String s) {
                                // 这是转换成了Student类型
                                // Student student = new Student();
                                // student.setName(s);
                                // return student;
                                return s.hashCode();        //将数据转换为了int（取得其hashCode值）
                            }
                        })
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer o) {
                                System.out.println("onNext--> " + o);
                            }
                        }, new Consumer<java.lang.Throwable>() {
                            @Override
                            public void accept(java.lang.Throwable throwable) {
                                System.out.println("onError--> " + throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() {
                                System.out.println("onComplete");
                            }
                        });
                break;
            case R.id.rxFlatMap:
                Observable.just("Hello", "RxJava", "Nice to meet you")
                        .flatMap(new Function<String, ObservableSource<Integer>>() {
                            @Override
                            public ObservableSource<Integer> apply(String s) {
                                return Observable.just(s.hashCode());
                            }
                        })
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                System.out.println("onNext--> " + integer);
                            }
                        }, new Consumer<java.lang.Throwable>() {
                            @Override
                            public void accept(java.lang.Throwable throwable) throws Exception {
                                System.out.println("onError--> " + throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                System.out.println("onComplete");
                            }
                        });
                break;
            case R.id.rxWindow:
                break;
            case R.id.rxFilter:
        Observable.just("Hello", "RxJava", "Nice to meet you")
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String aBoolean) throws Exception {
                        //这里的显示条件是s的长度大于5，而Hello的长度刚好是5
                        //所以不能满足条件
                        return aBoolean.length() > 5;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println("onNext--> " + s);
                    }
                }, new Consumer<java.lang.Throwable>() {
                    @Override
                    public void accept(java.lang.Throwable throwable) {
                        System.out.println("onError--> " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        System.out.println("onComplete");
                    }
                });
                break;
            case R.id.rxTake:
        Observable.just("Hello", "RxJava", "Nice to meet you")
                .take(2)
                //.taktLast(2)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println("onNext--> " + s);
                    }
                }, new Consumer<java.lang.Throwable>() {
                    @Override
                    public void accept(java.lang.Throwable throwable) {
                        System.out.println("onError--> " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        System.out.println("onComplete");
                    }
                });
                break;
            case R.id.rxSkip:
        Observable.just("Hello", "RxJava", "Nice to meet you")
                .skip(2)
                //.skipLast(2)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println("onNext--> " + s);
                    }
                }, new Consumer<java.lang.Throwable>() {
                    @Override
                    public void accept(java.lang.Throwable throwable) {
                        System.out.println("onError--> " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        System.out.println("onComplete");
                    }
                });
                break;
            case R.id.rxElementAt:
        Observable.just("Hello", "RxJava", "Nice to meet you")
//                .elementAt(1, "Great")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println("onNext--> " + s);
                    }
                }, new Consumer<java.lang.Throwable>() {
                    @Override
                    public void accept(java.lang.Throwable throwable) {
                        System.out.println("onError--> " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
                break;
            case R.id.rxDistinct:
        Observable.just("Hello", "Hello", "Hello", "RxJava", "Nice to meet you")
                .distinct()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println("onNext--> " + s);
                    }
                }, new Consumer<java.lang.Throwable>() {
                    @Override
                    public void accept(java.lang.Throwable throwable) {
                        System.out.println("onError--> " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        System.out.println("onComplete");
                    }
                });
                break;
            case R.id.rxStartWith:
        Observable.just("Hello", "RxJava", "Nice to meet you")
                .startWith("One")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        System.out.println("onNext--> " + s);
                    }
                }, new Consumer<java.lang.Throwable>() {
                    @Override
                    public void accept(java.lang.Throwable throwable) {
                        System.out.println("onError--> " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() {
                        System.out.println("onComplete");
                    }
                });
                break;
            case R.id.rxMerge:
                Observable.merge(Observable.just(1,2,3),Observable.just(4,5),Observable.just(6, 7), Observable.just(8, 9, 10))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                }, new Consumer<java.lang.Throwable>() {
                    @Override
                    public void accept(java.lang.Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
            default:
                break;
        }
    }

    private void towebview() {
        Intent intent = new Intent();
        intent.setClass(this,WebVeiwActivity.class);
        startActivity(intent);

    }

    private void rxDefertest() {
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.fromArray(strings1);
            }
        });

        strings1 = strings2; //订阅前把strings给改了
        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("onNext--> " + s);
            }

        }, new Consumer<java.lang.Throwable>() {
            @Override
            public void accept(java.lang.Throwable throwable) throws Exception {
                System.out.println("onError--> " + throwable.getMessage());
            }

        }, new Action() {
            @Override
            public void run() throws Exception {

            }
        });

    }


    // RxBus接收数据
    public void getRxBusEvent(){
        rxSbscription = RxBus.getInstance().toFlowable(RxBusEventBean.class).subscribe(new Consumer<RxBusEventBean>() {
            @Override
            public void accept(RxBusEventBean rxBusEventBean) throws Exception {
                tvTextView.setText("id :" + rxBusEventBean.getId() + "那么： " + rxBusEventBean.getName());
                Toast.makeText(MainActivity.this, "id :" + rxBusEventBean.getId() + "那么： " + rxBusEventBean.getName(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    // 最原生是的Rxjava2 使用请求网络  单个请求

//  1.  通过Obsrvable.create方法，调用OkHttp网络请求
//  2.  通过map方法结合gson，将response转换为bean类
//  3.  通过onNext，解析bean中数据，并进行数据库存储
//  4.  调度线程
//  5.  通过subscribe，根据请求成功或异常来更新UI
    public void Rxjava2Net(){
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> e) throws Exception {
                Request.Builder builder = new Request.Builder()
                        .url(PATH.BASE_URL)
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                e.onNext(response);
            }


        })
        // 转化为 bean 类
        .map(new Function<Response, bean>() {
                    @Override
                    public bean apply(Response response) throws Exception {
                        // 使用Gson

                        return null;
                    }
                }).doOnNext(new Consumer<bean>() {
            @Override
            public void accept(bean bean) throws Exception {
                // save Data
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<bean>() {
            @Override
            public void accept(bean bean) throws Exception {
                // get error
            }
        });
    }

    /**
     *   多个网络请求合并
     *
     * 这里主要是依赖于flatMap关键字，FlatMap可以将一个发射数据的Observable变换为多个Observables，然后将它们发射的数据合并后放进一个单独的Observable。
     利用这个特性，我们可以将Observable转成另一个Observable
     */

    public void muchNetWork(){
        Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> e) throws Exception {
                Request.Builder builder = new Request.Builder()
                        .url(PATH.BASE_URL)
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                e.onNext(response);
            }
        })

                // 转化为 bean 类
        .map(new Function<Response, bean>() {

            @Override
            public bean apply(Response response) throws Exception {
                // 使用 GSON 进行类型转换

                return null;
            }
        })
        //  第二个请求
        .flatMap(new Function<bean, ObservableSource<Response>>() {
            @Override
            public ObservableSource<Response> apply(bean bean) throws Exception {

                return null;
            }
        });
    }











}

