package com.charles.www.testDemo.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.charles.www.testDemo.R;
import com.charles.www.testDemo.base.view.BaseView;
import com.charles.www.testDemo.util.AppUtil;
import com.noober.background.BackgroundLibrary;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;


/**
 * Created by liuz on 16/9/30.
 */
public abstract class  BaseActivity<M, V extends BaseContract.View<M>, P extends BaseContract.Presenter<V>> extends RxFragmentActivity implements BaseView<M> {

    @Inject
    protected P mPresenter;
    protected Activity mContext;

    private Dialog mLoadingDialog;
    private View mLoadingView;



    @Override
    public Resources getResources() {
        return super.getResources();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        mContext = this;
        setBaseConfig();
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            ButterKnife.bind(this);
        }
        initEventAndData();
        if (AppUtil.isDebug()) {
            showLongToast(getClass().getSimpleName());
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideLoading();
    }

    private void setBaseConfig() {
        initTheme();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected abstract int getLayoutId();


    protected abstract void initEventAndData();

    private void initTheme() {
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtils.d("   现在是横屏");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LogUtils.d("   现在是竖屏");
        }
    }


    public void showLoading(String msg, boolean cancelable) {
        if (mLoadingDialog == null) {
            mLoadingView = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
            TextView loadingText = (TextView) mLoadingView.findViewById(R.id.id_tv_loading_dialog_text);
            loadingText.setText("加载中...");
            mLoadingDialog = new Dialog(this, R.style.CustomProgressDialog);
            // 将遮罩层设置为透明色
            mLoadingDialog.getWindow().setDimAmount(0f);
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setContentView(mLoadingView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (!mLoadingDialog.isShowing() && !isFinishing() && !isDestroyed() ) {
                mLoadingDialog.setCancelable(cancelable);
                mLoadingDialog.getWindow().setDimAmount(0f);
                mLoadingDialog.show();
            }
        }else {
            if (!mLoadingDialog.isShowing() && !isFinishing()) {
                mLoadingDialog.setCancelable(cancelable);
                mLoadingDialog.getWindow().setDimAmount(0f);
                mLoadingDialog.show();
            }
        }
    }

    public void showLoading(String msg) {
        showLoading(msg, true);
    }

    @Override
    public void showLoading() {
        showLoading(null, true);
    }

    @Override
    public void showShortToast(String msg) {
        ToastUtils.showShortSafe(msg);
    }

    public void showShortToast(int msg_id) {
        ToastUtils.showShortSafe(getString(msg_id));
    }

    @Override
    public void showLongToast(String msg) {
        ToastUtils.showShortSafe(msg);
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing() && !isFinishing()) {
            mLoadingDialog.dismiss();
        }
    }

    public boolean equalsTwo(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return a.equalsIgnoreCase(b);
    }

    @Override
    public void showException(Throwable e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null ){
            mLoadingDialog.cancel();
            mLoadingDialog = null;
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

}
