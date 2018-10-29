package com.charles.www.testDemo.bean;

/**
 * Created by huwei on 2018/8/8.
 *
 * @author : huwei
 * @date : 2018/8/8
 * @Sub :
 */

public class SignBean {
    // 头像的图片url
    private String imageUrl ;
    // 主持人 姓名
    private String host_name;

    // 获得的票数
    private String host_num;

    // 是否选中
    private boolean isSelect;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String getHost_num() {
        return host_num;
    }

    public void setHost_num(String host_num) {
        this.host_num = host_num;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
