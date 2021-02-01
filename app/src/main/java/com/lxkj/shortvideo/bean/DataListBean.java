package com.lxkj.shortvideo.bean;

import java.util.List;

/**
 * Time:2020/8/21
 * <p>
 * Author:李迪迦
 * <p>
 * Interface:
 */
public class DataListBean {
    public String id;
    public String name;
    public String image;
    public String enterEndDate;
    public String enterCount;
    public String competitionState;
    public String content;
    public String title;
    public String url;
    public String competitionCategoryId;
    public String video;
    public String coverImage;
    public String passed;
    public String nged;
    public String collected;
    public String focused;
    public String collectCount;
    public String shareCount;
    public String commentCount;
    public String likedCount;
    public String liked;
    public String createDate;
    public String passNgCount;
    public String passRatio;
    public String ngRatio;
    public String uploadDate;
    public String avatar;
    public String duration;
    public String nickname;
    public String updateDate;
    public String beFocused;
    public String subhead;
    public String messageType;
    public String correlation;
    public String unread;
    public Object msgContent;
    public String senderId;
    public String sendDate;
    public String competitionNumber;
    public String itemId;
    public String score;
    public int position;
    public Member member;
    public Competition competition;
    public List<SubVideos> subVideos;
    public List<String> images;
    public List<String> friendNicknameList;
    public List<SubCommentList> subCommentList;


    public String value;
    public String pm;
    public String code;


    public class Member{
        public String id;
        public String nickname;
        public String avatar;
    }
    public class Competition{
        public String id;
        public String name;
    }
    public class SubVideos{
        public String title;
        public String video;
    }
    public class  SubCommentList{
        public String id;
        public String content;
        public String likedCount;
        public String liked;
        public String createDate;
        public Member member;
    }
}
