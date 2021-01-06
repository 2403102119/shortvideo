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


}
