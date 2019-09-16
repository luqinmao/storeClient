package com.lqm.study.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lqm.study.R;
import com.lqm.study.activity.OrderDetailActivity;
import com.lqm.study.common.enums.OrderStatusEnum;
import com.lqm.study.model.Vo.OrderItemVo;
import com.lqm.study.util.T;

import java.util.List;

import butterknife.Bind;

public class OrderListAdapter extends BaseQuickAdapter<OrderItemVo, BaseViewHolder> {


    public OrderListAdapter(@Nullable List<OrderItemVo> data) {
        super(R.layout.item_order_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderItemVo item) {

        RecyclerView rvContent = helper.getView(R.id.rv_content);
        rvContent.setAdapter(new OrderListItemAdapter(item.getOrderItemVoList()));

        helper.setText(R.id.tv_status,item.getStatusDesc());

        TextView tvCancel = helper.getView(R.id.tv_cancel);
        TextView tvPay = helper.getView(R.id.tv_pay);
        TextView tvConfirmGoods = helper.getView(R.id.tv_confirm_goods);

        if (item.getStatus()== OrderStatusEnum.NO_PAY.getCode()){
            tvCancel.setVisibility(View.VISIBLE);
            tvPay.setVisibility(View.VISIBLE);
            tvConfirmGoods.setVisibility(View.GONE);
        }else if (item.getStatus()== OrderStatusEnum.PAID.getCode()){
            tvCancel.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvConfirmGoods.setVisibility(View.GONE);
        }else if (item.getStatus()== OrderStatusEnum.SHIPPED.getCode()){
            tvCancel.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvConfirmGoods.setVisibility(View.VISIBLE);
        }else if (item.getStatus()== OrderStatusEnum.ORDER_SUCCESS.getCode()){
            tvCancel.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvConfirmGoods.setVisibility(View.GONE);
        }else{
            tvCancel.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvConfirmGoods.setVisibility(View.GONE);
        }

        //按钮点击
        helper.addOnClickListener(R.id.tv_cancel);
        helper.addOnClickListener(R.id.tv_pay);
        helper.addOnClickListener(R.id.tv_confirm_goods);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,OrderDetailActivity.class);
                intent.putExtra("orderNo",item.getOrderNo());
                mContext.startActivity(intent);
            }
        });

    }

}
