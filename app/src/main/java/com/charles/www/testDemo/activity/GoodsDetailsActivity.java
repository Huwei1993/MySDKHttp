package com.charles.www.testDemo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.charles.www.testDemo.R;
import com.charles.www.testDemo.bean.SignBean;
import com.charles.www.testDemo.dialog.TestFragmentDoalog;
import com.charles.www.testDemo.view.CircleImageView;
import com.charles.www.testDemo.view.CircleImageViewAdapter;
import com.charles.www.testDemo.view.FixedGridView;

import java.util.ArrayList;

public class GoodsDetailsActivity extends AppCompatActivity {

    private ArrayList<SignBean> signBeanList = new ArrayList<>();
    private ArrayList<String>  imageUrlList = new ArrayList<>();
    private FixedGridView mContantView ;
    private CircleImageViewAdapter circleImageViewAdapter;
    private Context context;
    private static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        mContantView = findViewById(R.id.rg_contant);
        mActivity = this;
        initImageList();
    }

    private void initImageList() {
        imageUrlList.add("http://pic9.photophoto.cn/20081128/0033033999061521_b.jpg");
        imageUrlList.add("http://pic23.photophoto.cn/20120503/0034034456597026_b.jpg");
        imageUrlList.add("http://pic22.photophoto.cn/20120225/0034034432152602_b.jpg");
        imageUrlList.add("http://pic21.photophoto.cn/20111106/0020032891433708_b.jpg");
        imageUrlList.add("http://pic.qiantucdn.com/58pic/13/71/35/24k58PICSiB_1024.jpg");
        imageUrlList.add("http://pic38.nipic.com/20140226/18064511_101736603151_2.jpg");
        imageUrlList.add("http://pic30.photophoto.cn/20140114/0034034810150817_b.jpg");
        imageUrlList.add("http://pic10.photophoto.cn/20090207/0034034809650460_b.jpg");
        SignBean signBean =null;
           for (int i = 0; i < imageUrlList.size(); i ++) {
               signBean = new SignBean();
               signBean.setImageUrl(imageUrlList.get(i));
               signBean.setHost_name("杨洋");
               signBean.setHost_num("10");
               signBeanList.add(signBean);
           }
        Glide.with(this).load(imageUrlList.get(1)).into((CircleImageView) findViewById(R.id.iv_head));
        circleImageViewAdapter = new CircleImageViewAdapter(this,signBeanList,mContantView) {
            @Override
            protected void updateView() {

            }
        };
        mContantView.setAdapter(circleImageViewAdapter);
        mContantView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0;i<signBeanList.size();i++){
                    if(position == i){
                        signBeanList.get(i).setSelect(true);
                    }else{
                        signBeanList.get(i).setSelect(false);
                    }
                }
                circleImageViewAdapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragmentDialog();
            }
        });

    }

    public static Activity getActivity(){
        return mActivity;
    }


    // fragmentDialog弹窗

    public void initFragmentDialog(){
        TestFragmentDoalog testFragmentDoalog = new TestFragmentDoalog();
        testFragmentDoalog.show(getFragmentManager(),"GoodsDetail");
    }

}
