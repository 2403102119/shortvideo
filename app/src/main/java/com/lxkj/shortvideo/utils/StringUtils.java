package com.lxkj.shortvideo.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 小火
 * Create time on  2017/3/22
 * My mailbox is 1403241630@qq.com
 */

public class StringUtils {
    /**
     * 判断字符串是否有内容
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !TextUtils.isEmpty(str);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
        String num = "[1][3456789]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            return number.matches(num);
        }
    }
    /**
     * 判断密码格式是否正确
     * @param pwd
     * @return
     */
    public static boolean isPwd(String pwd) {
        String check = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{6,16})$";//
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(pwd);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 判断字符串是否符合密码的正则表达式规则
     */
    public static boolean isMatchesPwd(String pwd) {
        return pwd.matches("[\\da-zA-Z_]{6,20}");
    }


    /**
     * 判断字符串是否符合验证码的正则表达式规则
     */
    private static boolean isMatchesVerification(String text) {
        return text.matches("[\\d]{4}");
    }



    /**
     * @param oldString
     * @param unit
     * @return
     */
    public static String parseStringToNumber(String oldString, String unit) {
        if (TextUtils.isEmpty(oldString)) {
            return null;
        }
        if (!TextUtils.isEmpty(unit)) {
            int index = oldString.indexOf(unit);
            if (index != -1) {
                return oldString.substring(0, index);
            }
        }
        return oldString;
    }

    public static String modifyDataFormat(String str){
        String result;
        result = str.substring(0,4) + "."+ str.substring(5,7) + "."+ str.substring(8,10);
        return result;
    }
}
