package com.charles.www.testDemo.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.charles.www.testDemo.R;
import com.charles.www.testDemo.camera.bean.ImageItem;
import com.charles.www.testDemo.util.BitmapUtil;
import com.charles.www.testDemo.view.CropImageView;

import java.io.File;
import java.util.ArrayList;

public class ImageCropActivity extends ImageBaseActivity implements View.OnClickListener, CropImageView.OnBitmapSaveCompleteListener{

    private CropImageView mCropImageView;
    private ImagePicker imagePicker;
    private ArrayList<ImageItem> mImageItems;
    private Bitmap mBitmap;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        mCropImageView = findViewById(R.id.cv_crop_image);
        imagePicker = ImagePicker.getInstance();
        mImageItems = imagePicker.getSelectedImages();
        String imagePath = mImageItems.get(0).path;


        mCropImageView.setOnBitmapSaveCompleteListener(this);
        mCropImageView.setFocusStyle(imagePicker.getStyle());
        mCropImageView.setFocusWidth(imagePicker.getFocusWidth());
        mCropImageView.setFocusHeight(imagePicker.getFocusHeight());
        mCropImageView.setMaskColor(R.color.black);

        //缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels);
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(imagePath, options);
//        mCropImageView.setImageBitmap(mBitmap);
        //设置默认旋转角度
        mCropImageView.setImageBitmap(mCropImageView.rotate(mBitmap, BitmapUtil.getBitmapDegree(imagePath)));
    }



    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = width / reqWidth;
            } else {
                inSampleSize = height / reqHeight;
            }
        }
        return inSampleSize;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBitmapSaveSuccess(File file) {
        //裁剪后替换掉返回数据的内容，但是不要改变全局中的选中数据
        mImageItems.remove(0);
        ImageItem imageItem = new ImageItem();
        imageItem.path = file.getAbsolutePath();
        mImageItems.add(imageItem);

        Intent intent = new Intent();
        intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, mImageItems);
        setResult(ImagePicker.RESULT_CODE_ITEMS, intent);   //单选不需要裁剪，返回数据
        finish();
    }

    @Override
    public void onBitmapSaveError(File file) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCropImageView.setOnBitmapSaveCompleteListener(null);
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }

    }
}
