package com.lqm.study.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.lqm.study.R;
import com.lqm.study.common.AppConst;
import com.lqm.study.common.BaseActivity;
import com.lqm.study.helper.AliPayHelp;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.PayResult;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.Vo.ShoppingCardVo;
import com.lqm.study.model.pojo.ProductBean;
import com.lqm.study.util.ImageLoaderManager;
import com.lqm.study.util.T;
import com.lqm.study.widget.IconTextView;
import com.lqm.study.widget.NumberButton;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品详情页
 */
public class ProductDetailActivity extends BaseActivity {


    @Bind(R.id.it_return)
    IconTextView itReturn;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.btn_number)
    NumberButton btnNumber;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.tv_to_car)
    TextView tvToCar;
    @Bind(R.id.tv_to_buy)
    TextView tvToBuy;
    private int productId;

    private int buyBumber = 1;
    private ProductBean mProductData;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            switch (msg.what){
                case SDK_PAY_FLAG:
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ProductDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ProductDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initView();

    }


    private void initView() {
        productId = getIntent().getIntExtra("productId",0);

        OkGo.<ResponseData<ProductBean>>post(AppConst.Product.detail)
            .params("productId",productId)
            .execute(new JsonCallback<ResponseData<ProductBean>>() {
                @Override
                public void onSuccess(Response<ResponseData<ProductBean>> response) {
                    ResponseData<ProductBean> model = response.body();
                    if (model.getData() !=null){
                        setUIData(model.getData());
                    }

                }

                @Override
                public void onError(Response<ResponseData<ProductBean>> response) {
                    T.showShort(ProductDetailActivity.this,response.message());
                }
            });

    }

    private void setUIData(final ProductBean data) {
        mProductData = data;
        tvTitle.setText(data.getName());
        tvDesc.setText(data.getDetail());
        tvPrice.setText("￥"+data.getPrice());
        tvTotalPrice.setText("合计：￥"+data.getPrice());
        ImageLoaderManager.loadImage(ProductDetailActivity.this,data.getMainImage(),ivImage);

        btnNumber.setBuyMax(data.getStock())
                .setInventory(data.getStock())
                .setCurrentNumber(1)
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
                        buyBumber = number;
                        tvTotalPrice.setText("合计：￥"+buyBumber*data.getPrice());
                    }

                });

    }




    @OnClick({R.id.it_return, R.id.tv_to_car, R.id.tv_to_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.it_return:
                finish();
                break;
            case R.id.tv_to_car:
                int buyCount = btnNumber.getNumber();
                int productId = mProductData.getId();

                OkGo.<ResponseData<ShoppingCardVo>>post(AppConst.Cart.add)
                    .params("productId",productId)
                    .params("count",buyCount)
                    .execute(new JsonCallback<ResponseData<ShoppingCardVo>>() {
                        @Override
                        public void onSuccess(Response<ResponseData<ShoppingCardVo>> response) {
                            ResponseData<ShoppingCardVo> model = response.body();
                            T.showShort("添加成功!");
                        }

                        @Override
                        public void onError(Response<ResponseData<ShoppingCardVo>> response) {
                            T.showShort("添加到购物车失败!");
                            T.showShort(response.message());
                        }

                    });

                break;
            case R.id.tv_to_buy:
                 Intent intent = new Intent(ProductDetailActivity.this,OrderConfirmActivity.class);
                 intent.putExtra("productData", mProductData);
                 intent.putExtra("quantity", buyBumber);
                 startActivity(intent);

                break;
        }
    }


    public void toPay(){
        OkGo.<ResponseData<String>>post(AppConst.Order.pay)
            .params("orderNo",Long.valueOf("1492090946105"))
            .execute(new JsonCallback<ResponseData<String>>() {
                @Override
                public void onSuccess(Response<ResponseData<String>> response) {
                    final ResponseData<String> responseData = response.body();

                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(ProductDetailActivity.this);
                            Map<String,String> result = alipay.payV2(responseData.getData(),true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }

                @Override
                public void onError(Response<ResponseData<String>> response) {
                    T.showShort(response.message());

                }
            });




    }

}
