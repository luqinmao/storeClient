package com.lqm.study.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqm.study.R;
import com.lqm.study.adapter.OrderListAdapter;
import com.lqm.study.base.AppConst;
import com.lqm.study.base.BaseFragment;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.PayResult;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.Vo.OrderItemVo;
import com.lqm.study.model.Vo.OrderVo;
import com.lqm.study.util.T;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.inflate;

/**
 * user：lqm
 * desc：订单---全部订单
 */

public class OrderListStatusFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener{


    @Bind(R.id.rv_content)
    RecyclerView rvContent;
    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private OrderListAdapter orderAdapter;
    private String mTitle;
    private int mOrderStatus;
    private int pageSize = 10;
    private int pageNum = 1;


    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            switch (msg.what){
                case SDK_PAY_FLAG:
                    //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {// 判断resultStatus 为9000则代表支付成功
                        T.showShort("支付成功");
                        onRefresh();
                    } else {
                        T.showShort("支付失败:resultStatus--"+resultStatus+"resultInfo---"+resultInfo);
                    }
                    break;
            }

        }
    };

    public static OrderListStatusFragment newInstance(String title,int orderStatus) {
        OrderListStatusFragment fragment = new OrderListStatusFragment();
        Bundle args = new Bundle();
        args.putString("title",title);
        args.putInt("orderStatus",orderStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init() {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        mTitle = getArguments().getString("title");
        mOrderStatus = getArguments().getInt("orderStatus");
    }

    @Override
    public View initView() {
        View view = inflate(getActivity(), R.layout.frag_order_list_status, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {

        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderListAdapter(null);
        orderAdapter.bindToRecyclerView(rvContent);
        orderAdapter.setEmptyView(R.layout.layout_empty_nor);
        refreshLayout.setOnRefreshListener(this);
        orderAdapter.setOnLoadMoreListener(this, rvContent);

        onRefresh();

    }

    @Override
    public void initListener() {
        orderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                OrderItemVo bean = (OrderItemVo) baseQuickAdapter.getData().get(i);
                switch (view.getId()){
                    case R.id.tv_cancel:
                        cancelOrder(bean.getOrderNo());
                        break;

                    case R.id.tv_pay:
                        toPay(bean.getOrderNo());
                        break;

                    case R.id.tv_confirm_goods:
                        T.showShort("确认收货");
                        confirmGoods(bean.getOrderNo());
                        break;

                }
            }
        });


    }

    @Override
    public void onRefresh() {
        pageNum =1;
        OkGo.<ResponseData<OrderVo>>post(AppConst.Order.list)
            .params("status", mOrderStatus)
            .params("pageSize",pageSize)
            .params("pageNum",pageNum)
            .execute(new JsonCallback<ResponseData<OrderVo>>() {
            @Override
            public void onSuccess(Response<ResponseData<OrderVo>> response) {
                ResponseData<OrderVo> model = response.body();
                if (model.getData() !=null){
                    orderAdapter.setNewData(model.getData().getList());
                }

            }

            @Override
            public void onError(Response<ResponseData<OrderVo>> response) {
                T.showShort(getContext(),response.getException().getMessage());
            }

            @Override
            public void onFinish() {
                setRefreshing(false);
            }
        });

    }

    @Override
    public void onLoadMoreRequested() {
        pageNum += 10;
        OkGo.<ResponseData<OrderVo>>post(AppConst.Order.list)
            .params("status", mOrderStatus)
            .params("pageSize",pageSize)
            .params("pageNum",pageNum)
            .execute(new JsonCallback<ResponseData<OrderVo>>() {
                @Override
                public void onSuccess(Response<ResponseData<OrderVo>> response) {
                    ResponseData<OrderVo> model = response.body();
                    if (model.getData() !=null
                        && model.getData().getList() !=null
                        && model.getData().getList().size() >0){
                        orderAdapter.addData(model.getData().getList());
                        orderAdapter.loadMoreComplete();
                    }else{
                        orderAdapter.loadMoreEnd();
                    }

                }

                @Override
                public void onError(Response<ResponseData<OrderVo>> response) {
                    T.showShort(getContext(),response.getException().getMessage());
                    orderAdapter.loadMoreFail();
                }

            });

    }


    public void setRefreshing(final boolean refreshing) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }

    //取消订单
    private void cancelOrder(long orderNo){
        OkGo.<ResponseData<String>>post(AppConst.Order.cancel)
            .params("orderNo",orderNo)
            .execute(new JsonCallback<ResponseData<String>>() {
                @Override
                public void onSuccess(Response<ResponseData<String>> response) {
                    ResponseData<String> model = response.body();
                    if (model.getStatus() ==0){
                        T.showShort("订单已取消");
                        onRefresh();
                    }

                }

                @Override
                public void onError(Response<ResponseData<String>> response) {
                    T.showShort(response.getException().getMessage());
                }
            });
    }


    //支付订单
    public void toPay(long orderNo){
        OkGo.<ResponseData<String>>post(AppConst.Order.pay)
            .params("orderNo",orderNo)
            .execute(new JsonCallback<ResponseData<String>>() {
                @Override
                public void onSuccess(Response<ResponseData<String>> response) {
                    final ResponseData<String> responseData = response.body();

                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(getActivity());
                            Map<String,String> result = alipay.payV2(responseData.getData(),true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }

                @Override
                public void onError(Response<ResponseData<String>> response) {
                    T.showShort(response.getException().getMessage());

                }
            });
    }


    //确认收货
    private void confirmGoods(long orderNo) {
        OkGo.<ResponseData<String>>post(AppConst.Order.confirmReceivedGoods)
                .params("orderNo",orderNo)
                .execute(new JsonCallback<ResponseData<String>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<String>> response) {
                        ResponseData<String> model = response.body();
                        if (model.getStatus() ==0){
                            T.showShort("已确认");
                            onRefresh();
                        }

                    }

                    @Override
                    public void onError(Response<ResponseData<String>> response) {
                        T.showShort(response.getException().getMessage());
                    }
                });
    }




}
