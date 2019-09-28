package com.lqm.study.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqm.study.R;
import com.lqm.study.adapter.AddressAdapter;
import com.lqm.study.base.AppConst;
import com.lqm.study.base.BaseActivity;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.Vo.ShippingVo;
import com.lqm.study.model.pojo.ShippingBean;
import com.lqm.study.util.T;
import com.lqm.study.widget.CustomPopWindow;
import com.lqm.study.widget.IconTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import butterknife.Bind;
import butterknife.OnClick;

public class AddressListActivity extends BaseActivity {




    @Bind(R.id.it_return)
    IconTextView itReturn;
    @Bind(R.id.it_add)
    IconTextView itAdd;
    @Bind(R.id.rl_add_address)
    RelativeLayout rlAddAddress;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;
    private AddressAdapter addressAdapter;
    private CustomPopWindow popWindow;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        init();
        initView();
        initData();
        initListener();
    }

    private void init() {
        from =  getIntent().getStringExtra("from");
    }

    private void initView() {

        rvContent.setLayoutManager(new LinearLayoutManager(AddressListActivity.this));
        addressAdapter = new AddressAdapter(null);
        addressAdapter.bindToRecyclerView(rvContent);
        addressAdapter.setEmptyView(R.layout.layout_empty_nor);

    }

    private void initData() {
        OkGo.<ResponseData<ShippingVo>>post(AppConst.Shipping.list)
            .execute(new JsonCallback<ResponseData<ShippingVo>>() {
                @Override
                public void onSuccess(Response<ResponseData<ShippingVo>> response) {
                    ResponseData<ShippingVo> model = response.body();
                    if (model.getData() !=null && model.getData().getList() != null){
                        addressAdapter.setNewData(model.getData().getList());
                    }

                }

                @Override
                public void onError(Response<ResponseData<ShippingVo>> response) {
                    T.showShort(AddressListActivity.this,response.message());
                }
            });

    }

    private void initListener() {

        addressAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                final ShippingBean bean = addressAdapter.getData().get(position);

                View popView = View.inflate(AddressListActivity.this, R.layout.layout_popup_delete_shipping, null);
                popWindow = new CustomPopWindow.PopupWindowBuilder(AddressListActivity.this)
                        .setView(popView)
                        .enableBackgroundDark(true)
                        .create()
                        .showAtLocation(itReturn, Gravity.CENTER, 0, 0);

                popView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popWindow.dissmiss();
                    }
                });

                popView.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OkGo.<ResponseData<String>>post(AppConst.Shipping.del)
                            .params("shippingId",bean.getId())
                            .execute(new JsonCallback<ResponseData<String>>() {
                                @Override
                                public void onSuccess(Response<ResponseData<String>> response) {
                                    ResponseData<String> model = response.body();
                                    if (model.getStatus() ==0){
                                        T.showShort("删除成功!");
                                        initData();
                                    }
                                }

                                @Override
                                public void onError(Response<ResponseData<String>> response) {
                                    T.showShort(AddressListActivity.this,response.message());
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
        
        
        //点击
        addressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShippingBean bean =  addressAdapter.getData().get(position);
                if (OrderConfirmActivity.FROM_ORDER_CONFIRM.equals(from)){
                    Intent intent = new Intent();
                    intent.putExtra("data",bean);
                    setResult(OrderConfirmActivity.CODE_ADDRESS_RESULT, intent);
                    finish();
                }else {
                    Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
                    intent.putExtra("shippingData",bean);
                    startActivity(intent);
                }
            }
        });
        


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @OnClick({R.id.it_return, R.id.rl_add_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.it_return:
                finish();
                break;
            case R.id.rl_add_address:
                startActivity(new Intent(AddressListActivity.this,AddAddressActivity.class));
                break;
        }
    }
}
