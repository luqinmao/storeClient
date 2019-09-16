package com.lqm.study.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lqm.study.R;
import com.lqm.study.model.TestModel;
import com.lqm.study.model.pojo.CategoryBean;
import com.lqm.study.util.UIUtil;

import java.util.List;

public class SoftLeftAdapter extends BaseQuickAdapter<CategoryBean,BaseViewHolder> {


    public SoftLeftAdapter(@Nullable List<CategoryBean> data) {
        super(R.layout.item_soft_left, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryBean item) {

        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(item.getName());

        if (item.isSelect()){
            tvName.setTextColor(UIUtil.getColor(R.color.main));
            tvName.setBackgroundColor(UIUtil.getColor(R.color.tag_sel_color));
        }else{
            tvName.setTextColor(UIUtil.getColor(R.color.black));
            tvName.setBackgroundColor(UIUtil.getColor(R.color.white));
        }


    }




}
