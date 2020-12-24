package com.lxkj.shortvideo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by kxn on 2019/12/30 0030.
 */
public class AtTextWatcher implements TextWatcher {
    char atEndFlag = ' ';
    AtListener mListener;
    DeleteAtListener delListener;
    private int atIndex = 0;
    private int endFlagIndex = 0;

    public AtTextWatcher(AtListener listener,DeleteAtListener delListener) {
        this.mListener = listener;
        this.delListener = delListener;
    }

    public void insertTextForAt(EditText et, CharSequence text) {
//        if(atIndex == -1)
//            return;
        StringBuilder sb = new StringBuilder("@" + text);
        sb.append(atEndFlag);
        text = sb.toString();
        Editable text1 = et.getText();
        text1.insert(atIndex, text);
//        et.invalidate();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (count == 1) {//删除一个字符
            char c = s.charAt(start);
            if (c == atEndFlag) {
                endFlagIndex = start;
            }
        }
    }

    /**
     * @param s      新文本内容，即文本改变之后的内容
     * @param start  被修改文本的起始偏移量
     * @param before 被替换旧文本长度
     * @param count  替换的新文本长度
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        atIndex = s.length();
        if (count == 1) {//新增(输入)一个字符
            char c = s.charAt(start);
            if (c == '@') {
                if (mListener != null) {
                    mListener.triggerAt();
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (endFlagIndex != -1) {
            int index = endFlagIndex;
            while ((index -= 1) != -1) {
                char c = s.charAt(index);
                if (c == '@') {
                    break;
                }
            }
            int endFlagIndex = this.endFlagIndex;
            this.endFlagIndex = -1;
            if (index != -1)
                s.delete(index, endFlagIndex);
        }
    }

    /**
     * 输入 @ 监听
     */
    public interface AtListener {
        void triggerAt();
    }

    /**
     * 删除 @ 监听
     */
    public interface DeleteAtListener {
        void deleteAt();
    }
}

