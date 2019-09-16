package com.lqm.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.lqm.study.R;
import com.lqm.study.adapter.ShoppingConfirmAdapter;
import com.lqm.study.common.AppConst;
import com.lqm.study.common.BaseActivity;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.PayResult;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.Vo.OrderItemVo;
import com.lqm.study.model.Vo.ShippingVo;
import com.lqm.study.model.Vo.ShoppingConfirmVo;
import com.lqm.study.model.pojo.ProductBean;
import com.lqm.study.model.pojo.ShippingBean;
import com.lqm.study.util.T;
import com.lqm.study.widget.IconTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 确认提交订单
 * (直接创建订单支付和从购物车中获取产品创建订单支付)
 */
public class OrderConfirmActivity extends BaseActivity {


    private static final int CODE_SELECT_ADDRESS = 100;
    private static final int CODE_ADD_ADDRESS = 101;

    public static final int CODE_ADDRESS_RESULT = 102;
    public static final int CODE_ADD_ADDRESS_RESULT = 103;

    public static final String FROM_ORDER_CONFIRM = "from_order_confirm";
    private static final int SDK_PAY_FLAG = 1;

    @Bind(R.id.it_return)
    IconTextView itReturn;
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
    @Bind(R.id.et_remark)
    EditText etRemark;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.rl_add_address)
    RelativeLayout rlAddAddress;

    @Bind(R.id.rv_content)
    RecyclerView rvContent;

    private int quantity;
    private boolean isRidectBuy; //true:直接买  false:从
    private ProductBean productData;
    private ShoppingConfirmAdapter mAdapter;

    private ShippingBean shippingData;



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
                        finish();
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
        setContentView(R.layout.activity_order_confirm);

        init();
        initView();
        initData();

    }



    private void init() {
        productData = (ProductBean) getIntent().getSerializableExtra("productData");
        quantity = getIntent().getIntExtra("quantity",1);
        isRidectBuy = productData ==null?false:true;

    }

    private void initView() {

        rvContent.setFocusable(false);
        rvContent.setHasFixedSize(true);
        rvContent.setNestedScrollingEnabled(false);
        rvContent.setLayoutManager(new LinearLayoutManager(OrderConfirmActivity.this));
        mAdapter = new ShoppingConfirmAdapter(null);
        rvContent.setAdapter(mAdapter);

    }

    private void initData() {
        if (isRidectBuy){
            tvTotalPrice.setText("合计:￥"+productData.getPrice() * quantity);
        }else{
            getServerData();
        }

        //地址
//        getAddressData();

    }

    private void getAddressData() {
        OkGo.<ResponseData<ShippingVo>>post(AppConst.Shipping.list)
            .execute(new JsonCallback<ResponseData<ShippingVo>>() {
                @Override
                public void onSuccess(Response<ResponseData<ShippingVo>> response) {
                    ResponseData<ShippingVo> model = response.body();
                    if (model.getData() !=null
                            && model.getData().getList() != null
                            && model.getData().getList().size()>0
                            ){
                        shippingData = model.getData().getList().get(0);
                    }
                }

                @Override
                public void onFinish() {
                    setShippingDataUi();
                }
            });

    }

    private void getServerData() {
        OkGo.<ResponseData<ShoppingConfirmVo>>post(AppConst.Order.getOrderCartProduct)
            .execute(new JsonCallback<ResponseData<ShoppingConfirmVo>>() {
                @Override
                public void onSuccess(Response<ResponseData<ShoppingConfirmVo>> response) {
                    ResponseData<ShoppingConfirmVo> model = response.body();
                    mAdapter.setNewData(model.getData().getOrderItemVoList());
                    tvTotalPrice.setText("合计:￥"+model.getData().getProductTotalPrice());
                }

                @Override
                public void onError(Response<ResponseData<ShoppingConfirmVo>> response) {
                    T.showShort(response.getException().getMessage());
                }
            });

    }


    @OnClick({R.id.it_return, R.id.rl_address, R.id.tv_submit,R.id.rl_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.it_return:
                finish();
                break;
            case R.id.rl_add_address:
                Intent intentAdd = new Intent(OrderConfirmActivity.this,AddAddressActivity.class);
                intentAdd.putExtra("from",FROM_ORDER_CONFIRM);
                startActivityForResult(intentAdd,CODE_ADD_ADDRESS);
                break;
            case R.id.rl_address:
                Intent intent = new Intent(OrderConfirmActivity.this,AddressListActivity.class);
                intent.putExtra("from",FROM_ORDER_CONFIRM);
                startActivityForResult(intent,CODE_SELECT_ADDRESS);

                break;
            case R.id.tv_submit:
                if (isRidectBuy){
                    toCreateOrderDirect();
                }else{
                    toCreateOrder();
                }

                break;
        }
    }

    //购物车进入
    private void toCreateOrder() {
        OkGo.<ResponseData<OrderItemVo>>post(AppConst.Order.create)
            .params("shippingId",shippingData.getId())
            .execute(new JsonCallback<ResponseData<OrderItemVo>>() {
                @Override
                public void onSuccess(Response<ResponseData<OrderItemVo>> response) {
                    ResponseData<OrderItemVo> model = response.body();
                    if (model.getStatus() ==0){
                        //创建订单后进行支付
                        toPay(model.getData().getOrderNo());
                    }else{
                        T.showShort(model.getMsg());
                    }

                }

                @Override
                public void onError(Response<ResponseData<OrderItemVo>> response) {
                    T.showShort("提交订单失败，请稍后再试");
                    T.showShort(response.message());
                }
            });


    }

    //直接购买
    private void toCreateOrderDirect() {
        OkGo.<ResponseData<OrderItemVo>>post(AppConst.Order.createOrderDirect)
                .params("shippingId",shippingData.getId())
                .params("productId",productData.getId())
                .params("quantity",quantity)
                .execute(new JsonCallback<ResponseData<OrderItemVo>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<OrderItemVo>> response) {
                        ResponseData<OrderItemVo> model = response.body();
                        if (model.getStatus() ==0){
                            //创建订单后进行支付
                            toPay(model.getData().getOrderNo());
                        }else{
                            T.showShort(model.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<OrderItemVo>> response) {
                        T.showShort("提交订单失败，请稍后再试");
                        T.showShort(response.message());
                    }
                });


    }



    //支付
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
                            PayTask alipay = new PayTask(OrderConfirmActivity.this);
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
                    T.showShort(response.message());

                }
            });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_SELECT_ADDRESS && data !=null){
            shippingData = (ShippingBean) data.getSerializableExtra("data");
            setShippingDataUi();
        }else if (requestCode == CODE_ADD_ADDRESS){
            getAddressData();
        }
    }

    private void setShippingDataUi(){
        if (shippingData ==null){
            rlAddress.setVisibility(View.GONE);
            rlAddAddress.setVisibility(View.VISIBLE);
        }else {
            rlAddress.setVisibility(View.VISIBLE);
            rlAddAddress.setVisibility(View.GONE);

            tvShippingName.setText("收货人:"+ shippingData.getReceiverName());
            tvShippingPhone.setText("电话:" +shippingData.getReceiverPhone() );
            tvShippingAddress.setText("地址:"+shippingData.getReceiverAddress());
        }

    }


}
