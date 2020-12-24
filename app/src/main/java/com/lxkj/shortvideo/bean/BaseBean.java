package com.lxkj.shortvideo.bean;

/**
 * Created by kxn on 2019/12/5 0005.
 */
public class BaseBean {
    public String result;//0成功 1失败
    public String resultNote;

    public String getResultNote() {
        return resultNote;
    }

    public void setResultNote(String resultNote) {
        this.resultNote = resultNote;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
