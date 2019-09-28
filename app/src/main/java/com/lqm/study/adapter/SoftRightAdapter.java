package com.lqm.study.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lqm.study.R;
import com.lqm.study.activity.ProductDetailActivity;
import com.lqm.study.common.AppConst;
import com.lqm.study.model.pojo.ProductBean;
import com.lqm.study.util.ImageLoaderManager;

import java.util.List;

/**
 * 商品列表
 */
public class SoftRightAdapter extends BaseQuickAdapter<ProductBean,BaseViewHolder> {



    public SoftRightAdapter(@Nullable List<ProductBean> data) {
        super(R.layout.item_soft_right, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ProductBean item) {

        helper.setText(R.id.tv_title,item.getName());
        helper.setText(R.id.tv_price,"￥："+item.getPrice());

        TextView tvStock =  helper.getView(R.id.tv_stock);
        if (item.getStock() ==0){
            tvStock.setVisibility(View.INVISIBLE);
        }else {
            tvStock.setVisibility(View.VISIBLE);
            tvStock.setText("库存："+item.getStock());
        }

        ImageView imageView = helper.getView(R.id.iv_image);
        ImageLoaderManager.loadImage(mContext, item.getMainImage(),imageView);


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ProductDetailActivity.class);
                intent.putExtra("productId",item.getId());
                mContext.startActivity(intent);
            }
        });


    }


}
