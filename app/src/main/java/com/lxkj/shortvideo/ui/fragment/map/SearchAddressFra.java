package com.lxkj.shortvideo.ui.fragment.map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.lxkj.shortvideo.AppConsts;
import com.lxkj.shortvideo.R;
import com.lxkj.shortvideo.ui.fragment.TitleFragment;
import com.lxkj.shortvideo.ui.fragment.map.adapter.HintAddressAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kxn on 2019/12/11 0011.
 */
public class SearchAddressFra extends TitleFragment implements Inputtips.InputtipsListener {
    @BindView(R.id.etKeywords)
    EditText etKeywords;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tvNoChoose)
    TextView tvNoChoose;
    Unbinder unbinder;
    private HintAddressAdapter hintAdapter;
    private List<Tip> hints = new ArrayList<Tip>();

    @Override
    public String getTitleName() {
        return "搜索地址";
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        rootView = inflater.inflate(R.layout.fra_search_address, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        etKeywords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                hintAdapter.setKeyWord(editable.toString());
                InputtipsQuery inputquery = new InputtipsQuery(editable.toString(), AppConsts.city);
                inputquery.setCityLimit(true);//限制在当前城市
                Inputtips inputTips = new Inputtips(mContext, inputquery);
                inputTips.setInputtipsListener(SearchAddressFra.this);
                inputTips.requestInputtipsAsyn();
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                act.finishSelf();
            }
        });
        hintAdapter = new HintAddressAdapter(mContext, hints);
        lv.setAdapter(hintAdapter);

//        tvNoChoose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EventBus.getDefault().post(new AddressEvent(0,""));
//                act.finishSelf();
//            }
//        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {
            lv.setVisibility(View.VISIBLE);
            hints.clear();
            hints.addAll(tipList);
            hintAdapter.notifyDataSetChanged();
        }
    }
}
