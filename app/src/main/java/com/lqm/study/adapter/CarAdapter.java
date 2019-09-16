package com.lqm.study.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lqm.study.R;
import com.lqm.study.model.pojo.ShoppingCardBean;
import com.lqm.study.util.ImageLoaderManager;
import com.lqm.study.util.T;
import com.lqm.study.util.UIUtil;
import com.lqm.study.widget.IconTextView;
import com.lqm.study.widget.NumberButton;
import com.lqm.study.widget.RoundImageView;

import java.util.List;

import butterknife.Bind;

/**
 * 购物车列表
 */
public class CarAdapter extends BaseQuickAdapter<ShoppingCardBean, BaseViewHolder> {

    private NumberChangeInterface numberInterface;

    public CarAdapter(@Nullable List<ShoppingCardBean> data) {
        super(R.layout.item_car, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShoppingCardBean item) {

        helper.setText(R.id.tv_title,item.getProductSubtitle());
        helper.setText(R.id.tv_price,"￥"+item.getProductPrice());
        IconTextView  itSelect = helper.getView(R.id.it_select);
        NumberButton  btnNumber = helper.getView(R.id.btn_number);
        ImageView imageView = helper.getView(R.id.iv_image);
        ImageLoaderManager.loadImage(mContext,item.getProductMainImage(),imageView);

        if (item.getProductChecked() == 1){
            itSelect.setText(UIUtil.getString(R.string.ic_select));
            itSelect.setTextColor(UIUtil.getColor(R.color.main));
        }else{
            itSelect.setText(UIUtil.getString(R.string.ic_un_select));
            itSelect.setTextColor(UIUtil.getColor(R.color.text1));
        }

        btnNumber.setBuyMax(item.getProductStock())
                .setInventory(item.getProductStock())
                .setCurrentNumber(item.getQuantity())
                .setOnWarnListener(new NumberButton.OnWarnListener() {
                    @Override
                    public void onWarningForInventory(int inventory) {
                        T.showShort("当前库存:" + inventory);
                    }

                    @Override
                    public void onWarningForBuyMax(int buyMax) {
                        T.showShort("超过最大购买数:" + buyMax);
                    }

                    @Override
                    public void onTextChanger(int number) {
                        numberInterface.onNumberChange(item,number);
                    }

                });

    }


    public interface NumberChangeInterface{
        void onNumberChange(ShoppingCardBean item,int number);
    }

    public void setCallBack(NumberChangeInterface numberInterface) {
        this.numberInterface = numberInterface;
    }

}
