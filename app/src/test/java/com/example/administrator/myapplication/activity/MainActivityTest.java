package com.charles.www.testDemo.activity;

import android.content.Context;
import android.widget.Button;

import com.charles.www.testDemo.BuildConfig;
import com.charles.www.testDemo.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by xiao on 2018/7/27.
 *
 * @author xiao
 * @date 2018/7/27
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MainActivityTest {
    private MainActivity mActivity;
    private Button mButton;
    private Context mContext;
    @Before
    public void setUp() throws  Exception{
        ShadowLog.stream = System.out;
        mActivity = Robolectric.setupActivity(MainActivity.class);
        RxJavaCallAdapterFactory.createAsync();
        mButton = (Button) mActivity.findViewById(R.id.btn_1);

    }

    // 验证点击第一个按钮数据
    @Test
    public void onclick1test() throws  Exception{
        mButton.performClick();

    }

}
