package com.charles.www.testDemo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/13.
 */

public class PATH {
//    public static final String BASE_URL = "http://www.baidu.com";
    public static final String BASE_URL = "https://ocj-test4.ocj.com.cn/";
//    public static final String BASE_URL = "https://m1.ocj.com.cn";
    public static final String LoginWithPassword = "/api/members/members/password_login";
    public static final String sendsms = "api/members/smscode/send_verify_code_mobile";
    public static final String CheckID = "/api/members/members/check_loginid";
    public static final String GetCartNum = "/api/orders/carts/getCartsCount";
    public static final String PayDrawPrize = "/api/events/restapi/draw_prize_for_order";


    public static class initAPIMapList{
        public static Map<String,String> getMap(){
            Map<String,String> map = new HashMap<>();
            map.put("LoginWithPassword",PATH.LoginWithPassword);
            map.put("sendsms",PATH.sendsms);
            map.put(PATHAPIID.CheckID,PATH.CheckID);
            map.put(PATHAPIID.GetCartNum,PATH.GetCartNum);
            return map;
        }
    }
}
