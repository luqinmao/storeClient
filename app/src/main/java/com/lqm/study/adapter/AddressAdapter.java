package com.lqm.study.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lqm.study.R;
import com.lqm.study.activity.AddAddressActivity;
import com.lqm.study.model.TestModel;
import com.lqm.study.model.pojo.ShippingBean;

import java.util.List;

/**
 * 收获地址列表
 */
public class AddressAdapter extends BaseQuickAdapter<ShippingBean,BaseViewHolder> {


    public AddressAdapter(@Nullable List<ShippingBean> data) {
        super(R.layout.item_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShippingBean item) {

        helper.setText(R.id.tv_shipping_name,"收货人:"+item.getReceiverName());
        helper.setText(R.id.tv_shipping_phone,"电话:"+item.getReceiverPhone());
        helper.setText(R.id.tv_shipping_address,"收货地址:"+item.getReceiverAddress());

    }


}
