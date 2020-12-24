package com.lxkj.shortvideo.utils;

import android.graphics.Color;
import androidx.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kxn on 2019/12/10 0010.
 */
public class LocalUtil {


    /**
     * 设置字符串中多个不同关键字的颜色（颜色统一, 无点击事件）
     *
     * @param content 目标字符串
     * @param keyStrs 关键字集合
     * @param color   单一的颜色值
     * @return
     */
    public static SpannableString setSpanColorStr(String content, List<String> keyStrs, final String color) {
        SpannableString spannableString = new SpannableString(content);
        for (int i = 0; i < keyStrs.size(); i++) {
            String keyStr = keyStrs.get(i);
            if (content.contains(keyStr)) {
                int startNew = 0;
                int startOld = 0;
                String temp = content;
                while (temp.contains(keyStr)) {
                    spannableString.setSpan(
                            new ClickableSpan() {
                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setColor(Color.parseColor(color));
                                    ds.setUnderlineText(false);
                                }

                                @Override
                                public void onClick(View widget) {
                                }
                            }, startOld + temp.indexOf(keyStr),
                            startOld + temp.indexOf(keyStr) + keyStr.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    startNew = temp.indexOf(keyStr) + keyStr.length();
                    startOld += startNew;
                    temp = temp.substring(startNew);
                }
            }
        }
        return spannableString;
    }

    public static SpannableString matcherSearchText(String text, String keyword, String color) {
        SpannableString ss = new SpannableString(text);

        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(ss);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
//            ss.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 单独设置点击事件
            ClickableSpan clickableSpanOne = new ClickableSpan() {
                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.parseColor(color));
                    ds.setUnderlineText(false);
                }
                @Override
                public void onClick(View view) {
//                    ToastUtil.show("onclick");
                }
            };
            ss.setSpan(clickableSpanOne, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }


    public static String getStartTime() {
        String startDate = DateUtil.getCurrentDate("yyyy-MM-dd");
        String hour = "9";
        List<String> hours = getHours();
        for (int i = 0; i < getHours().size(); i++) {
            if (Integer.parseInt(hours.get(i)) > Integer.parseInt(DateUtil.getCurrentDate("HH"))) {
                hour = DateUtil.getCurrentDate(hours.get(i));
                break;
            }
        }
        return startDate + " " + hour + ":" + "00";
    }

    public static String getEndTime() {
        String endDate = DateUtil.getFetureDate(7, "yyyy-MM-dd");
        String hour = "9";
        List<String> hours = getHours();
        for (int i = 0; i < getHours().size(); i++) {
            if (Integer.parseInt(hours.get(i)) > Integer.parseInt(DateUtil.getCurrentDate("HH"))) {
                hour = DateUtil.getCurrentDate(hours.get(i));
                break;
            }
        }
        return endDate + " " + hour + ":" + "00";
    }

    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static List<String> getHours() {
        List<String> hours = new ArrayList<>();
        for (int i = 9; i < 20; i++) {
            hours.add(i + "");
        }
        return hours;
    }


}
