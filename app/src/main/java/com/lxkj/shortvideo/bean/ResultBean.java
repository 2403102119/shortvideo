package com.lxkj.shortvideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kxn on 2019/1/21 0021.
 */
public class ResultBean extends BaseBean implements Serializable {
    public List<DataListBean> dataList;
    public String totalPage;
    public String mid;
    public String avatar;
    public String nickname;
    public String state;
    public String image;
    public String name;
    public String enterEndDate;
    public String enterCount;
    public String competitionState;
    public String intro;
    public String awardIntro;
    public String acceptAwardIntro;
    public String entered;
    public String url;
    public String passRatio;
    public String ngRatio;
    public String totalCount;
    public String mobile;
    public String sex;
    public String birthday;
    public String age;
    public String province;
    public String city;
    public String district;
    public String motto;
    public String focusedCount;
    public String toFocusedCount;
    public String id;
    public String title;
    public String competitionCategoryId;
    public String video;
    public String coverImage;
    public String passed;
    public String nged;
    public String collected;
    public String focused;
    public String collectCount;
    public String createDate;
    public String beFocused;
    public String shareCount;
    public String commentCount;
    public String content;
    public String shielded;
    public String userSig;
    public String number;
    public String androidFile;
    public String iosUrl;
    public String remarks;
    public String competitionNumber;
    public String count;
    public Member member;
    public List<String> carouselImages;
    public List<String> keys;
    public List<String> urls;
    public List<String> images;
    public List<Advertising> advertising;
    public List<SubVideos> subVideos;

  public class Advertising{
      public String id;
      public String image;
      public String url;
  }
  public class Member{
      public String id;
      public String nickname;
      public String avatar;
  }
  public class SubVideos{
      public String title;
      public String video;
  }
}
