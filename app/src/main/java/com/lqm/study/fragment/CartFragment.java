package com.lqm.study.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqm.study.R;
import com.lqm.study.activity.OrderConfirmActivity;
import com.lqm.study.adapter.CarAdapter;
import com.lqm.study.base.AppConst;
import com.lqm.study.base.BaseFragment;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.Vo.ShoppingCardVo;
import com.lqm.study.model.pojo.ShoppingCardBean;
import com.lqm.study.util.T;
import com.lqm.study.util.UIUtil;
import com.lqm.study.widget.CustomPopWindow;
import com.lqm.study.widget.IconTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.inflate;

/**
 * user：lqm
 * desc：购物车模块
 */

public class CartFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;
    @Bind(R.id.it_select_all)
    IconTextView itSelectAll;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind({R.id.refresh_layout})
    SwipeRefreshLayout refreshLayout;

    private CarAdapter carAdapter;
    private boolean isSelectAll;

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View initView() {
        View view = inflate(getActivity(), R.layout.frag_cart, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {

        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        carAdapter = new CarAdapter(null);
        carAdapter.bindToRecyclerView(rvContent);
        carAdapter.setEmptyView(R.layout.layout_empty_nor);
        refreshLayout.setOnRefreshListener(this);

        onRefresh();

    }


    @Override
    public void initListener() {

        carAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                ShoppingCardBean bean =  carAdapter.getData().get(position);
                toSelectOrUnSelect(bean);

            }
        });


        carAdapter.setCallBack(new CarAdapter.NumberChangeInterface() {
            @Override
            public void onNumberChange(ShoppingCardBean bean,int number) {
                OkGo.<ResponseData<ShoppingCardVo>>post(AppConst.Cart.update)
                    .params("productId",bean.getProductId())
                    .params("count",number)
                    .execute(new JsonCallback<ResponseData<ShoppingCardVo>>() {
                        @Override
                        public void onSuccess(Response<ResponseData<ShoppingCardVo>> response) {
                            ResponseData<ShoppingCardVo> model = response.body();
                            carAdapter.setNewData(model.getData().getCartProductVoList());
                            if (model.getData() !=null){
                                tvTotalPrice.setText("￥"+model.getData().getCartTotalPrice());
                            }
                        }

                        @Override
                        public void onError(Response<ResponseData<ShoppingCardVo>> response) {
                            T.showShort(response.getException().getMessage());
                        }
                    });
            }
        });


        carAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                final ShoppingCardBean bean =  carAdapter.getData().get(position);

                View popView = View.inflate(getContext(), R.layout.layout_popup_delete_cart_item, null);
                final CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(getContext())
                        .setView(popView)
                        .enableBackgroundDark(true)
                        .create()
                        .showAtLocation(tvTitle, Gravity.CENTER, 0, 0);

                popView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popWindow.dissmiss();
                    }
                });

                popView.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OkGo.<ResponseData<ShoppingCardVo>>post(AppConst.Cart.deleteProduct)
                            .params("productIds",bean.getProductId())
                            .execute(new JsonCallback<ResponseData<ShoppingCardVo>>() {
                                @Override
                                public void onSuccess(Response<ResponseData<ShoppingCardVo>> response) {
                                    ResponseData<ShoppingCardVo> model = response.body();
                                    carAdapter.setNewData(model.getData().getCartProductVoList());
                                    selectAllUi(model.getData().getCartTotalPrice());
                                    T.showShort("删除成功!");
                                }

                                @Override
                                public void onError(Response<ResponseData<ShoppingCardVo>> response) {
                                    T.showShort(response.message());
                                }

                                @Override
                                public void onFinish() {
                                    popWindow.dissmiss();
                                }
                            });
                    }
                });

                return false;
            }
        });

    }

    private void toSelectOrUnSelect(ShoppingCardBean data) {
        if (data.getProductChecked() == 1){ //已勾选
            OkGo.<ResponseData<ShoppingCardVo>>post(AppConst.Cart.unSelect)
                .params("productId",data.getProductId())
                .execute(new JsonCallback<ResponseData<ShoppingCardVo>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ShoppingCardVo>> response) {
                        ResponseData<ShoppingCardVo> model = response.body();
                        carAdapter.setNewData(model.getData().getCartProductVoList());

                        if (model.getData() ==null){
                            isSelectAll =false;
                        }else{
                            isSelectAll =model.getData().isAllChecked();
                        }
                        selectAllUi(model.getData().getCartTotalPrice());

                    }

                    @Override
                    public void onError(Response<ResponseData<ShoppingCardVo>> response) {
                        T.showShort("出现错误了，请稍后再试");
                    }
                });

        }else{ //未勾选
            OkGo.<ResponseData<ShoppingCardVo>>post(AppConst.Cart.select)
                .params("productId",data.getProductId())
                .execute(new JsonCallback<ResponseData<ShoppingCardVo>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ShoppingCardVo>> response) {
                        ResponseData<ShoppingCardVo> model = response.body();
                        carAdapter.setNewData(model.getData().getCartProductVoList());


                        if (model.getData() ==null){
                            isSelectAll =false;
                        }else{
                            isSelectAll =model.getData().isAllChecked();
                        }
                        selectAllUi(model.getData().getCartTotalPrice());

                    }

                    @Override
                    public void onError(Response<ResponseData<ShoppingCardVo>> response) {
                        T.showShort("出现错误了，请稍后再试");
                    }
                });
        }


    }

    private void getShoppingCarData() {

        OkGo.<ResponseData<ShoppingCardVo>>post(AppConst.Cart.list)
            .execute(new JsonCallback<ResponseData<ShoppingCardVo>>() {
                @Override
                public void onSuccess(Response<ResponseData<ShoppingCardVo>> response) {
                    ResponseData<ShoppingCardVo> model = response.body();
                    if (model.getData() == null){
                        isSelectAll =false;
                    }else{
                        isSelectAll =model.getData().isAllChecked();
                        carAdapter.setNewData(model.getData().getCartProductVoList());
                        selectAllUi(model.getData().getCartTotalPrice());
                    }
                }

                @Override
                public void onError(Response<ResponseData<ShoppingCardVo>> response) {
                    T.showShort(response.message());
                }

                @Override
                public void onFinish() {
                    setRefreshing(false);
                }
            });

    }



    @OnClick({R.id.it_select_all, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.it_select_all:
                selectAllOrUnSelectAll();
                break;
            case R.id.tv_submit:
                if (tvTotalPrice.getText().toString().equals("￥0")){
                    T.showShort("购物车没有选中的商品!");
                }else{
                    startActivity(new Intent(getContext(), OrderConfirmActivity.class));
                }
                break;
        }
    }


    private void selectAllOrUnSelectAll(){

        if (isSelectAll){
            OkGo.<ResponseData<ShoppingCardVo>>post(AppConst.Cart.unSelectAll)
                .execute(new JsonCallback<ResponseData<ShoppingCardVo>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ShoppingCardVo>> response) {
                        ResponseData<ShoppingCardVo> model = response.body();
                        carAdapter.setNewData(model.getData().getCartProductVoList());

                        isSelectAll =!isSelectAll;
                        selectAllUi(model.getData().getCartTotalPrice());
                    }

                    @Override
                    public void onError(Response<ResponseData<ShoppingCardVo>> response) {
                        T.showShort("出现错误了，请稍后再试");
                    }
                });
        }else{
            OkGo.<ResponseData<ShoppingCardVo>>post(AppConst.Cart.selectAll)
                .execute(new JsonCallback<ResponseData<ShoppingCardVo>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ShoppingCardVo>> response) {
                        ResponseData<ShoppingCardVo> model = response.body();
                        carAdapter.setNewData(model.getData().getCartProductVoList());
                        isSelectAll =!isSelectAll;
                        selectAllUi(model.getData().getCartTotalPrice());
                    }

                    @Override
                    public void onError(Response<ResponseData<ShoppingCardVo>> response) {
                        T.showShort("出现错误了，请稍后再试");
                    }
                });
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }


    private void selectAllUi(double totalPrice){
        if (isSelectAll){
            itSelectAll.setTextColor(UIUtil.getColor(R.color.main));
            itSelectAll.setText(UIUtil.getString(R.string.ic_select));
        }else{
            itSelectAll.setTextColor(UIUtil.getColor(R.color.text1));
            itSelectAll.setText(UIUtil.getString(R.string.ic_un_select));
        }
        tvTotalPrice.setText("￥"+totalPrice);
    }

    @Override
    public void onRefresh() {
        getShoppingCarData();
    }

    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }
}
