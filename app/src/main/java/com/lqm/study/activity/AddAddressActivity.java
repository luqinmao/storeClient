package com.lqm.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lqm.study.R;
import com.lqm.study.common.AppConst;
import com.lqm.study.common.BaseActivity;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.pojo.ShippingBean;
import com.lqm.study.util.T;
import com.lqm.study.widget.IconTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加收货地址（或者修改地址）
 */
public class AddAddressActivity extends BaseActivity {


    @Bind(R.id.it_return)
    IconTextView itReturn;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_address)
    EditText etAddress;
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    private ShippingBean shippingData;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);

        initView();

    }

    private void initView() {
        shippingData = (ShippingBean) getIntent().getSerializableExtra("shippingData");
        from =  getIntent().getStringExtra("from");

        if (shippingData !=null){
            etName.setText(shippingData.getReceiverName());
            etPhone.setText(shippingData.getReceiverPhone());
            etAddress.setText(shippingData.getReceiverAddress());
        }

    }

    @OnClick({R.id.it_return, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.it_return:
                finish();
                break;
            case R.id.tv_submit:

                    String name = etName.getText().toString();
                    String phone = etPhone.getText().toString();
                    String address = etAddress.getText().toString();
                    if (TextUtils.isEmpty(name)){
                        T.showShort("收货人不能为空");
                    }else if (TextUtils.isEmpty(phone)){
                        T.showShort("电话号码不能为空");
                    }else if (TextUtils.isEmpty(address)){
                        T.showShort("收货地址不能为空");
                    }else{
                        if (shippingData == null){
                            addShipping(name,phone,address);
                        }else{
                            modifyShipping(name,phone,address);
                        }
                    }
                break;
        }
    }


    private void addShipping(String name,String phone,String address){
        OkGo.<ResponseData<String>>post(AppConst.Shipping.add)
                .params("receiverName",name)
                .params("receiverPhone",phone)
                .params("receiverAddress",address)
                .execute(new JsonCallback<ResponseData<String>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<String>> response) {
                        ResponseData<String> model = response.body();
                        if (model.getStatus() ==0){
                            T.showShort("添加成功");
                            if (OrderConfirmActivity.FROM_ORDER_CONFIRM.equals(from)){
                                Intent intent = new Intent();
                                setResult(OrderConfirmActivity.CODE_ADD_ADDRESS_RESULT, intent);
                            }
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<String>> response) {
                        T.showShort(response.message());
                    }
            });
    }

    private void modifyShipping(String name,String phone,String address){
        OkGo.<ResponseData<String>>post(AppConst.Shipping.update)
                .params("id",shippingData.getId())
                .params("receiverName",name)
                .params("receiverPhone",phone)
                .params("receiverAddress",address)
                .execute(new JsonCallback<ResponseData<String>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<String>> response) {
                        ResponseData<String> model = response.body();
                        if (model.getStatus() ==0){
                            T.showShort("修改成功");
                            finish();
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<String>> response) {
                        T.showShort(response.message());
                    }
                });
    }

}
