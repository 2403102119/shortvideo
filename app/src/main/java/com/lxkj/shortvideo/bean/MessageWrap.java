package com.lxkj.shortvideo.bean;

/**
 * Time:2020/9/30
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class MessageWrap {

    public final String message;

    public static MessageWrap getInstance(String message) {
        return new MessageWrap(message);
    }

    private MessageWrap(String message) {
        this.message = message;
    }
}