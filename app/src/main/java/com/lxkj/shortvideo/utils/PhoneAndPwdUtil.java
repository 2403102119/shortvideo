package com.lxkj.shortvideo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/26 0026.
 * 手机号码   密码  正则验证
 */
public class PhoneAndPwdUtil {
    /**
     * 验证手机号
     */
    public static boolean isPhone(String number) {
        /*
         * 移动：134、135、136、137、138、139、147、150、151、152、157(TD)、158、159、178、182、183、184、187、188
		 * 联通：130、131、132、145、155、156、171、175、176、185、186 电信：133、149、153、173、177、180、181、189（1349卫通）虚拟运营商：170
		 * 总结起来就是第一位必定为1，第二位必定为3、4、5、7或8，其他位置的可以为0-9
		 */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(18[0-9])|(17[0,1,3,5-8]))\\d{8}$";// "[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。

            return number.matches(telRegex);

    }

    /**
     * 验证密码
     */
    public static boolean isPwd(String pwd) {
        String check = "^[0-9a-zA-Z]{6,16}$";//'/^[a-zA-Z0-9]{6,10}$/'
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(pwd);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 验证身份证号码
     */
    private static final Object[] CHECK_NUMBERS = new Object[]{1, 0, "X", 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isIdCard(String card) {
        if ((!card.matches("[0-9]{17}x") && !card.matches("[0-9]{15}") && !card.matches("[0-9]{18}"))) {
            return false;
        }

        if (card.length() == 18) {// 校验校验码
            int sum = 0;
            for (int i = 0; i < card.length() - 1; i++) {
                sum += Integer.parseInt(card.substring(i, i + 1)) * Math.pow(2, 17 - i);
            }
            if (card.substring(17, 18).equalsIgnoreCase(String.valueOf(CHECK_NUMBERS[sum % 11]))) {
                return true;
            }
        }
        return false;

    }
    public static boolean validateIdcard(String no)
    {
        // 对身份证号进行长度等简单判断
        if (no == null || no.length() != 18 || !no.matches("\\d{17}[0-9X]"))
        {
            return false;
        }
        // 1-17位相乘因子数组
        int[] factor = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        // 18位随机码数组
        char[] random = "10X98765432".toCharArray();
        // 计算1-17位与相应因子乘积之和
        int total = 0;
        for (int i = 0; i < 17; i++)
        {
            total += Character.getNumericValue(no.charAt(i)) * factor[i];
        }
        // 判断随机码是否相等
        return random[total % 11] == no.charAt(17);
    }



    /**
     * 验证邮箱
     */
    public static Boolean isEmail(String str) {
        Boolean isEmail = false;
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        if (str.matches(expr)) {
            isEmail = true;
        }
        return isEmail;
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

}
