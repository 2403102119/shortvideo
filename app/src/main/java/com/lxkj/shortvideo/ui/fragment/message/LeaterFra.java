package com.lxkj.shortvideo.ui.fragment.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.biz.ActivitySwitcher;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.login.LoginFra;
import com.lxkj.shortvideo.utils.SharePrefUtil;
import com.lxkj.shortvideo.utils.StringUtil;
import com.lxkj.shortvideo.utils.ToastUtil;
import com.lxkj.shortvideo.view.NormalDialog;
import com.tencent.imsdk.TIMManager;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Time:2020/12/31
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class LeaterFra extends TitleFragment implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.tvSelectFriend)
    TextView tvSelectFriend;
    @BindView(R.id.conversation_layout)
    ConversationLayout conversationLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.layout_leater, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    public void initView() {
        tvSelectFriend.setOnClickListener(this);
        // 初始化聊天面板
        conversationLayout.initDefault();
        // 获取 TitleBarLayout
        TitleBarLayout titleBarLayout = conversationLayout.findViewById(R.id.conversation_title);
        titleBarLayout.setVisibility(View.GONE);

        customizeConversation(conversationLayout);
    }

    public void customizeConversation(final ConversationLayout layout) {
        // 从 ConversationLayout 获取会话列表
        ConversationListLayout listLayout = layout.getConversationList();
        listLayout.disableItemUnreadDot(false);// 设置 item 是否不显示未读红点，默认显示
        listLayout.setItemAvatarRadius(90); // 设置 adapter item 头像圆角大小
        // 长按弹出菜单
        listLayout.setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConversationInfo messageInfo) {
                Bundle bundle = new Bundle();
                bundle.putString("id",messageInfo.getId());
                bundle.putString("title",messageInfo.getTitle());
                ActivitySwitcher.startFragment(getActivity(), ChatFra.class,bundle);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSelectFriend://选择好友
                if (StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
                    ToastUtil.show("请先登录");
                    ActivitySwitcher.startFragment(getActivity(), LoginFra.class);
                    return;
                }
                ActivitySwitcher.startFragment(getActivity(), SelectFriendFra.class);
                break;
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!StringUtil.isEmpty(SharePrefUtil.getString(getContext(), AppConsts.UID, ""))){
            TIMManager.getInstance().getConversationList();
            conversationLayout.setVisibility(View.VISIBLE);
        }else {
            conversationLayout.setVisibility(View.GONE);
        }

    }
}
