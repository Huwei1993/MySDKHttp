package com.charles.www.testDemo.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charles.www.testDemo.R;
import com.charles.www.testDemo.activity.GoodsDetailsActivity;

/**
 * Created by huwei on 2018/8/9.
 *
 * @author : huwei
 * @date : 2018/8/9
 * @Sub : 弹出 DialogFragment 并且测试在此dialog之上在弹出一个dialog覆盖当前的dialog
 */

public class TestFragmentDoalog extends DialogFragment {
    private Activity mActivity;
    private TextView tv1,tv2;
    private Dialog mLoadingDialog;
    private View mLoadingView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE,R.style.MyDialog);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_dialog_fragment,container,false);
        tv1 = view.findViewById(R.id.test_dialog);
        tv2 = view.findViewById(R.id.test_close_dialog);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignActivityRulesDialog();
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }


    private void showSignActivityRulesDialog() {
        String mOtteryRuleUrl = "http://m.ocj.com.cn/other/lotteryrule.jsp";
        // 设置透明度
        WindowManager.LayoutParams layoutParams;
        if (mLoadingDialog == null) {
            mLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_birthday_question_layout, null);
            Display display = GoodsDetailsActivity.getActivity().getWindowManager().getDefaultDisplay();
            TextView loadingText = (TextView) mLoadingView.findViewById(R.id.tv_dialog_birthday_question_text);
            ImageView imageView = (ImageView) mLoadingView.findViewById(R.id.img_dialog_birthday_question_close);
            WebView webView = (WebView) mLoadingView.findViewById(R.id.webview_dialog_birthday_question_layout);
            if (!TextUtils.isEmpty(mOtteryRuleUrl)) {
                webView.loadUrl(mOtteryRuleUrl);
                webView.setVisibility(View.VISIBLE);
                webView.setBackgroundColor(0xb2000000);
                loadingText.setVisibility(View.GONE);
            }else {
                loadingText.setText("数据加载失败");
                webView.setVisibility(View.GONE);
                loadingText.setVisibility(View.VISIBLE);
            }
            mLoadingDialog = new Dialog(getContext(),R.style.BirthDayDioalog);
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setContentView(mLoadingView, new LinearLayout.LayoutParams(display.getWidth(), display.getHeight()));
            // 设置透明度
            if (mLoadingDialog.getWindow() != null) {
                layoutParams = mLoadingDialog.getWindow().getAttributes();
                layoutParams.alpha = 0.8f;
                mLoadingDialog.getWindow().setAttributes(layoutParams);
            }
            // 覆盖状态栏
            Window window = mLoadingDialog.getWindow();
//            window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLoadingDialog.dismiss();
                }
            });

        }
        if (!mLoadingDialog.isShowing() && !GoodsDetailsActivity.getActivity().isFinishing()) {
            mLoadingDialog.setCancelable(true);
            mLoadingDialog.show();
        }
    }



}
