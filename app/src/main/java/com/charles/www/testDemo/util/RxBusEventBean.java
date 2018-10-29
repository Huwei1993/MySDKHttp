package com.charles.www.testDemo.util;

/**
 * Created by xiao on 2018/7/23.
 *
 * @author xiao
 * @date 2018/7/23
 */

public class RxBusEventBean {
    private String id;
    private String name;

    public RxBusEventBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
