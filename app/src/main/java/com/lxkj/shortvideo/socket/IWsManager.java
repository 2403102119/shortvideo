package com.lxkj.shortvideo.socket;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Time:2020/9/28
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
interface IWsManager {

    WebSocket getWebSocket();

    void startConnect();

    void stopConnect();

    boolean isWsConnected();

    int getCurrentStatus();

    void setCurrentStatus(int currentStatus);

    boolean sendMessage(String msg);

    boolean sendMessage(ByteString byteString);
}