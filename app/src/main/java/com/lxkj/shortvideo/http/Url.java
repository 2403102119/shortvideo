package com.lxkj.shortvideo.http;


/**
 * Created by kxn on 2018/5/17 0017.
 */

public class Url {
    public static String IP = "http://122.114.49.242:8081/apiService/member/";
    public static String WebIP = "http://62.234.20.192";


    public static String WDQY = IP + "/sideline/a/taboutus/tAboutus/disPlayDetail?id=e65d0de3a8184df8b5ea6ad0f3147555";

    //登录
    public static String login = IP + "login";
    //手机号是否已注册
    public static String mobileExist = IP + "mobileExist";
    //注册
    public static String register = IP + "register";
    //获取验证码
    public static String getAuthCode = IP + "getAuthCode";
    //通过验证码修改密码
    public static String changePasswordByCode = IP + "changePasswordByCode";
    //退出登录
    public static String logout = IP + "logout";
    //赛事分类
    public static String competitionCategoryList = IP + "competitionCategoryList";
    //赛事列表
    public static String competitionList = IP + "competitionList";


}
