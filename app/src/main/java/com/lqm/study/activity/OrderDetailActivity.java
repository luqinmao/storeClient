package com.lqm.study.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.lqm.study.R;
import com.lqm.study.adapter.OrderListItemAdapter;
import com.lqm.study.common.AppConst;
import com.lqm.study.common.BaseActivity;
import com.lqm.study.common.enums.OrderStatusEnum;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.PayResult;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.Vo.OrderItemVo;
import com.lqm.study.util.T;
import com.lqm.study.widget.IconTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity {


    @Bind(R.id.it_return)
    IconTextView itReturn;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.tv_order_no)
    TextView tvOrderNo;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_payment)
    TextView tvPayment;
    @Bind(R.id.ic_address)
    IconTextView icAddress;
    @Bind(R.id.tv_shipping_name)
    TextView tvShippingName;
    @Bind(R.id.tv_shipping_phone)
    TextView tvShippingPhone;
    @Bind(R.id.tv_shipping_address)
    TextView tvShippingAddress;
    @Bind(R.id.rl_address)
    RelativeLayout rlAddress;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.tv_pay)
    TextView tvPay;
    @Bind(R.id.tv_confirm_goods)
    TextView tvConfirmGoods;
    private long orderNo;


    private OrderListItemAdapter orderAdapter;

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
                        getServerData();
                    } else {
                        T.showShort("支付失败:resultStatus--"+resultStatus+"resultInfo---"+resultInfo);
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        initView();

    }

    private void initView() {
        orderNo = getIntent().getLongExtra("orderNo", 0);

        rvContent.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
        orderAdapter =  new OrderListItemAdapter(null);
        rvContent.setAdapter(orderAdapter);

        getServerData();

    }

    private void getServerData() {
        OkGo.<ResponseData<OrderItemVo>>post(AppConst.Order.detail)
                .params("orderNo", orderNo)
                .execute(new JsonCallback<ResponseData<OrderItemVo>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<OrderItemVo>> response) {
                        ResponseData<OrderItemVo> model = response.body();
                        if (model.getData() != null) {
                            setUIData(model.getData());
                        } else {
                            T.showShort(model.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<OrderItemVo>> response) {
                        T.showShort(response.getException().getMessage());

                    }
                });


    }

    private void setUIData(OrderItemVo bean) {

        tvStatus.setText("订单状态："+bean.getStatusDesc());
        tvOrderNo.setText("订单编号："+bean.getOrderNo());
        tvPayment.setText("支付金额：￥"+bean.getPayment());
        tvTime.setText("交易时间："+bean.getPaymentTime());
        tvShippingName.setText("收货人:"+bean.getShippingVo().getReceiverName());
        tvShippingPhone.setText("电话："+bean.getShippingVo().getReceiverPhone());
        tvShippingAddress.setText("地址："+bean.getShippingVo().getReceiverAddress());

        orderAdapter.setNewData(bean.getOrderItemVoList());

        if (bean.getStatus()== OrderStatusEnum.NO_PAY.getCode()){
            tvCancel.setVisibility(View.VISIBLE);
            tvPay.setVisibility(View.VISIBLE);
            tvConfirmGoods.setVisibility(View.GONE);
        }else if (bean.getStatus()== OrderStatusEnum.PAID.getCode()){
            tvCancel.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvConfirmGoods.setVisibility(View.GONE);
        }else if (bean.getStatus()== OrderStatusEnum.SHIPPED.getCode()){
            tvCancel.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvConfirmGoods.setVisibility(View.VISIBLE);
        }else if (bean.getStatus()== OrderStatusEnum.ORDER_SUCCESS.getCode()){
            tvCancel.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvConfirmGoods.setVisibility(View.GONE);
        }else{
            tvCancel.setVisibility(View.GONE);
            tvPay.setVisibility(View.GONE);
            tvConfirmGoods.setVisibility(View.GONE);
        }

    }


    @OnClick({R.id.it_return, R.id.tv_cancel, R.id.tv_pay, R.id.tv_confirm_goods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.it_return:
                finish();
                break;
            case R.id.tv_cancel:
                cancelOrder(orderNo);
                break;
            case R.id.tv_pay:
                toPay(orderNo);
                break;
            case R.id.tv_confirm_goods:
                confirmGoods(orderNo);
                break;
        }
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
                            getServerData();
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
                                PayTask alipay = new PayTask(OrderDetailActivity.this);
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
                            getServerData();
                        }

                    }

                    @Override
                    public void onError(Response<ResponseData<String>> response) {
                        T.showShort(response.getException().getMessage());
                    }
                });
    }

}
