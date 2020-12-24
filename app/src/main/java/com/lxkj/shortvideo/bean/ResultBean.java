package com.lxkj.shortvideo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kxn on 2019/1/21 0021.
 */
public class ResultBean extends BaseBean implements Serializable {
    public List<DataListBean> dataList;
    public String totalPage;
    public String totalCount;
    public String pageSize;

    public String sid;//用户id
    public String logo;//店铺logo
    public String name;//店铺名称
    public String balance;//余额
    public String opening;//营业状态，1.营业中，0.打烊
    public String todayEarnings;//今日收益
    public String totalEarnings;//累计收益
    public String carriage;//运费
    public String status;//登录状态，1登录成功，2.审核中，3.审核失败
    public String phone;//平台客服电话
    public String platformPhone;
    public String shopCategoryId;
    public String shopCategoryName;
    public String legalPerson;
    public String district;
    public String location;
    public String lng;
    public String lat;
    public String intro;
    public String deposit;
    public String minDeposit;
    public String goodsNo;
    public String video;
    public String detail;
    public String price;
    public String linePrice;
    public String pinkage;
    public String mid;
    public String leaderName;
    public String communityName;
    public String mobile;
    public String memberRemarks;
    public String shopRemarks;
    public String cancelReason;
    public String goodsAmount;
    public String couponValue;
    public String amount;
    public String realAmount;
    public String orderStatus;
    public String refundStatus;
    public String logisticsCode;
    public String logisticsName;
    public String logisticsNo;
    public String delayTake;
    public String placeDate;
    public String placeTimestamp;
    public String cancelDate;
    public String payDate;
    public String sendDate;
    public String takeDate;
    public String payChannel;
    public String orderNo;
    public String orderType;
    public String withdraw;
    public String money;
    public String earnings;
    public String account;
    public String accountName;
    public String sort;
    public String body;
    public String detailUrl;
    public String id;
    public List<String> carousel;
    public List<String> label;
    public List<String> introImages;
    public List<String> identityCard;
    public List<String> businessLicense;
    public List<String> urls;
    public List<GoodsListBean> goodsList;
    public AttributesBean goodsCategory1;
    public AttributesBean goodsCategory2;
    public AttributesBean goodsCategory3;
    public AttributesBean goodsClassify;
    public List<SpecsBean> specs;


}
