package com.lxkj.shortvideo.biz;

import android.os.Handler;

import com.lxkj.shortvideo.utils.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventCenter {


    public interface EventListener {

        public void onEvent(HcbEvent e);
    }

    private void sendEvtWithKey(final EventType evt,
                                final String key, final String value) {
        final Map<String, Object> param = new HashMap<String, Object>(1);
        param.put(key, value);
        send(new HcbEvent(evt, param));
    }

    public void sendType(final EventType evt) {
        send(new HcbEvent(evt));
    }

    public void evtLogin() {
        send(new HcbEvent(EventType.EVT_LOGIN));
    }

    public void evtLogout() {
        send(new HcbEvent(EventType.EVT_LOGOUT));
    }

    public static class HcbEvent {

        public EventType type;
        public Map<String, Object> params;

        public HcbEvent(EventType type) {
            this.type = type;
        }

        public HcbEvent(EventType type, Map<String, Object> params) {
            this.type = type;
            this.params = params;
        }

    }

    public enum EventType {
        EVT_PaySuccess,//支付成功
        EVT_REGISTER,//注册
        EVT_LOGIN, // 登录
        EVT_LOGOUT, // 注销
        EVT_EDITINFO,//修改个人信息
        EVT_DOORDER,//订单操作
        EVT_CHANGEORDERTYPE,//订单操作
        EVT_WxPay,//微信支付成功
        EVT_CARTSUBMIT,//购物车下单



        EVT_BINDPHONE,//绑定手机号成功
        EVT_TOHOME,//回到主页
        EVT_RZSUCCESS,//招商加盟提交成功



        EVT_UpdateRemark, // 修改备注
    }

    private final Handler uiHandler;
    private final Map<EventType, List<EventListener>> center;

    public EventCenter(final Handler handler) {
        uiHandler = handler;
        center = new HashMap<EventType, List<EventListener>>();
    }

    public void registEvent(final EventListener l, final EventType e) {
        List<EventListener> list = center.get(e);
        if (null == list) {
            list = new ArrayList<EventListener>();
            list.add(l);
            center.put(e, list);
        } else if (!list.contains(l)) {
            list.add(l);
        }
    }

    public void unregistEvent(final EventListener l, final EventType e) {
        final List<EventListener> list = center.get(e);
        if (null != list) {
            list.remove(l);
        }
    }

    public void send(final HcbEvent e) {
        final List<EventListener> list = getCopy(e.type);
        if (ListUtil.isEmpty(list)) {
            return;
        }
        uiHandler.post(new Runnable() {

            @Override
            public void run() {
                for (int i = list.size() - 1; i >= 0; i--) {
                    try {
                        list.get(i).onEvent(e);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    private List<EventListener> getCopy(final EventType et) {
        final List<EventListener> origin = center.get(et);
        if (null == origin || origin.isEmpty()) {
            return null;
        }
        return new ArrayList<EventListener>() {

            private static final long serialVersionUID = 1L;

            {
                addAll(center.get(et));
            }
        };
    }

}
