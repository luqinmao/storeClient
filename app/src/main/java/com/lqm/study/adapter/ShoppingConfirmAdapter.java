package com.lqm.study.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lqm.study.R;
import com.lqm.study.model.pojo.OrderItemVoListBean;
import com.lqm.study.util.ImageLoaderManager;

import java.util.List;

/**
 * 商品确认
 */
public class ShoppingConfirmAdapter extends BaseQuickAdapter<OrderItemVoListBean, BaseViewHolder> {


    public ShoppingConfirmAdapter(@Nullable List<OrderItemVoListBean> data) {
        super(R.layout.item_shopping_confirm, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderItemVoListBean item) {

        helper.setText(R.id.tv_title,item.getProductName());
        helper.setText(R.id.tv_price,"￥"+item.getCurrentUnitPrice());
        helper.setText(R.id.tv_num,"x "+item.getQuantity());

        ImageView imageView = helper.getView(R.id.iv_image);
        ImageLoaderManager.loadImage(mContext,item.getProductImage(),imageView);


    }



}
