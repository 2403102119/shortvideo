package com.lxkj.shortvideo.ui.fragment.message;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.actlink.NaviRightListener;
import com.lxkj.shortvideo.adapter.ChatAdapter;
import com.lxkj.shortvideo.bean.DataListBean;
import com.lxkj.shortvideo.bean.ResultBean;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.http.BaseCallback;
import com.lxkj.shortvideo.http.OkHttpHelper;
import com.lxkj.shortvideo.http.Url;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lxkj.shortvideo.view.NormalDialog;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Time:2020/11/20
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:会话列表
 */
public class ChatFra extends TitleFragment implements NaviRightListener, View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.chat_layout)
    ChatLayout chatLayout;
    @BindView(R.id.llhuati)
    LinearLayout llhuati;
    @BindView(R.id.etHuati)
    EditText etHuati;
    @BindView(R.id.tvWancheng)
    TextView tvWancheng;
    @BindView(R.id.tagFlow)
    TagFlowLayout tagFlow;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llHuanti)
    LinearLayout llHuanti;
    @BindView(R.id.imGuanbi)
    ImageView imGuanbi;
    private String id, title;
    private List<DataListBean> listBeans = new ArrayList<>();
    TagAdapter<String> adapter;
    List<String> hot_list = new ArrayList<>();
    List<String> id_list = new ArrayList<>();
    private String huatiID;
    private MessageLayout messageLayout;
    private boolean listVisibity = false;
    private ArrayList<DataListBean> listBeans_recycle;
    private int page = 1, totalPage = 1;
    private ChatAdapter chatAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fra_chat, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        id = getArguments().getString("id");
        title = getArguments().getString("title");
        act.titleTv.setText(title);
        chatLayout.initDefault();
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(id);
        chatLayout.setChatInfo(chatInfo);
        chatLayout.getTitleBar().setVisibility(View.GONE);




        messageLayout = chatLayout.getMessageLayout();
        messageLayout.setAvatar(R.mipmap.touxiang);

        messageLayout.setOnItemClickListener(new MessageLayout.OnItemClickListener() {
            @Override
            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
                messageLayout.showItemPopMenu(position, messageInfo, view);
            }

            @Override
            public void onConcelClick(View view, int position, MessageInfo messageInfo) {//选中
                Log.i("TAG", "onConcelClick: " + position);
                for (int i = 0; i < listBeans.size(); i++) {
                    if (position == listBeans.get(i).position) {
                        listBeans.remove(i);
                    }
                }
                if (listBeans.size() == 0) {
                    messageLayout.gone();
                    llhuati.setVisibility(View.GONE);
                }

            }

            @Override
            public void onEnterClick(View view, int position, MessageInfo messageInfo) {//取消
                Log.i("TAG", "onConcelClick: " + position);
                DataListBean dataListBean = new DataListBean();
                dataListBean.senderId = messageInfo.getTimMessage().getSender();
                dataListBean.msgContent = messageInfo.getExtra();
                dataListBean.sendDate = StringUtil.stampToDate(messageInfo.getTimMessage().getTimestamp() + "");
                dataListBean.position = position;
                listBeans.add(dataListBean);

                llhuati.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {

            }
        });

        InputLayout inputLayout = chatLayout.getInputLayout();
        inputLayout.enableVideoCall();
        inputLayout.enableAudioCall();
        inputLayout.disableSendFileAction(true);
        inputLayout.disableVideoRecordAction(true);

        tvWancheng.setOnClickListener(this);
        llHuanti.setOnClickListener(this);
        imGuanbi.setOnClickListener(this);


        adapter = new TagAdapter<String>(hot_list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_choose, parent, false);
                view.setText(s);
                return view;
            }
        };
        tagFlow.setAdapter(adapter);

        tagFlow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                etHuati.setText(hot_list.get(position));
                huatiID = id_list.get(position);
                return true;
            }
        });

        topicList();
        topicList_ry();

        listBeans_recycle = new ArrayList<DataListBean>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(getContext(), listBeans_recycle);
        recyclerView.setAdapter(chatAdapter);
        chatAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int firstPosition) {//详情
                Bundle bundle = new Bundle();
                bundle.putString("toMid", id);
                bundle.putString("tid", listBeans_recycle.get(firstPosition).id);
                bundle.putString("title", listBeans_recycle.get(firstPosition).name);
                ActivitySwitcher.startFragment(getActivity(), ChatDetailFra.class, bundle);
            }

        });


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvWancheng://完成
                for (int i = 0; i < hot_list.size(); i++) {
                    if (hot_list.get(i).equals(etHuati.getText().toString())) {
                        huatiID = id_list.get(i);
                    } else {
                        huatiID = "";
                    }
                }
                saveTopic(etHuati.getText().toString());
                break;
            case R.id.llHuanti:
                listVisibity = false;
                llHuanti.setVisibility(View.GONE);
                break;
            case R.id.imGuanbi:
                llhuati.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 获取话题列表
     */
    private void topicList() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        OkHttpHelper.getInstance().post_json(getContext(), Url.topicList, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {
            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                hot_list.clear();
                id_list.clear();
                for (int i = 0; i < resultBean.dataList.size(); i++) {
                    hot_list.add(resultBean.dataList.get(i).name);
                    id_list.add(resultBean.dataList.get(i).id);
                }
                adapter.notifyDataChanged();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 保存聊天记录至话题
     */
    private void saveTopicMsg(String tid) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        params.put("tid", tid);
        params.put("toMid", id);
        params.put("msgData", listBeans);
        OkHttpHelper.getInstance().post_json(getContext(), Url.saveTopicMsg, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {
            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                ToastUtil.show("保存成功");
                messageLayout.gone();
                etHuati.setText("");
                huatiID = "";
                topicList_ry();
                topicList();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }


    /**
     * 保存话题
     */
    private void saveTopic(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        params.put("id", huatiID);
        params.put("name", name);
        OkHttpHelper.getInstance().post_json(getContext(), Url.saveTopic, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {
            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                messageLayout.gone();
                huatiID = resultBean.id;
                saveTopicMsg(huatiID);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    /**
     * 获取话题列表
     */
    private void topicList_ry() {
        Map<String, Object> params = new HashMap<>();
        params.put("mid", SharePrefUtil.getString(getContext(), AppConsts.UID, ""));
        OkHttpHelper.getInstance().post_json(getContext(), Url.topicList, params, new BaseCallback<ResultBean>() {
            @Override
            public void onBeforeRequest(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onResponse(Response response) {
            }

            @Override
            public void onSuccess(Response response, ResultBean resultBean) {
                listBeans_recycle.clear();
                listBeans_recycle.addAll(resultBean.dataList);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public String rightText() {
        return "话题列表";
    }

    @Override
    public void onRightClicked(View v) {
        if (listVisibity) {
            listVisibity = false;
            llHuanti.setVisibility(View.GONE);
        } else {
            listVisibity = true;
            llHuanti.setVisibility(View.VISIBLE);
        }
    }

}
