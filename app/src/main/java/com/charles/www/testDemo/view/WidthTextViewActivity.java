package com.charles.www.testDemo.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.charles.www.testDemo.R;

public class WidthTextViewActivity extends AppCompatActivity {

    private AutoResizeTextView tv_center;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_width_text_view);
        tv_center = findViewById(R.id.tv_center);
        tv_center.setText("1basdknaskd");

    }
}
