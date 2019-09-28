package com.lqm.study.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lqm.study.R;
import com.lqm.study.activity.AboutActivity;
import com.lqm.study.activity.AddressListActivity;
import com.lqm.study.activity.LoginRegisterActivity;
import com.lqm.study.activity.MoreActivity;
import com.lqm.study.activity.OrderListActivity;
import com.lqm.study.base.App;
import com.lqm.study.base.AppConst;
import com.lqm.study.base.BaseFragment;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.User;
import com.lqm.study.util.ImageLoaderManager;
import com.lqm.study.util.T;
import com.lqm.study.util.UIUtil;
import com.lqm.study.widget.RoundImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.inflate;

/**
 * user：lqm
 * desc：个人中心模块
 */

public class UserFragment extends BaseFragment {

    @Bind(R.id.iv_photo)
    RoundImageView ivPhoto;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.ll_login_status)
    LinearLayout llLoginStatus;
    @Bind(R.id.tv_logout)
    TextView tvLogout;
    @Bind(R.id.rl_all_order)
    RelativeLayout rlAllOrder;
    @Bind(R.id.ll_un_pay)
    LinearLayout llUnPay;
    @Bind(R.id.ll_wait_delivery)
    LinearLayout llWaitDelivery;
    @Bind(R.id.ll_shipped)
    LinearLayout llShipped;
    @Bind(R.id.ll_trade_success)
    LinearLayout llTradeSuccess;
    @Bind(R.id.ll_address)
    LinearLayout llAddress;
    @Bind(R.id.ll_about)
    LinearLayout llAbout;
    @Bind(R.id.ll_more)
    LinearLayout llMore;
    private User mUserInfo;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View initView() {
        View view = inflate(getActivity(), R.layout.frag_user, null);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void initData() {

        setUserUI();
    }

    public void setUserUI() {
        mUserInfo = App.getUserInfo();
        tvLogout.setVisibility(App.getUserInfo() == null ? View.GONE : View.VISIBLE);

        if (mUserInfo != null) {
            tvUserName.setText(mUserInfo.getUsername());
            if (!TextUtils.isEmpty(mUserInfo.getPhoto())){
                ImageLoaderManager.loadImage(getContext(),AppConst.IMAGE_HOST+mUserInfo.getPhoto(),ivPhoto);
            }
        } else {
            tvUserName.setText("未登录");
            ivPhoto.setImageDrawable(UIUtil.getResource().getDrawable(R.mipmap.default_img));
        }
    }

    @OnClick({R.id.ll_login_status, R.id.tv_logout,R.id.rl_all_order, R.id.ll_un_pay,
            R.id.ll_wait_delivery, R.id.ll_shipped, R.id.ll_trade_success,
            R.id.ll_address, R.id.ll_about, R.id.ll_more})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_login_status:
                if (App.getUserInfo() == null) {
                    intent.setClass(getContext(), LoginRegisterActivity.class);
                }

                break;
            case R.id.tv_logout:
                App.setUserInfo(null);
                setUserUI();
                T.showShort(getContext(), "注销成功");
                tvLogout.setVisibility(App.getUserInfo() == null ? View.GONE : View.VISIBLE);

                OkGo.<ResponseData<String>>post(AppConst.User.logout)
                    .execute(new JsonCallback<ResponseData<String>>() {
                        @Override
                        public void onSuccess(Response<ResponseData<String>> response) {

                       }

                        @Override
                        public void onError(Response<ResponseData<String>> response) {
                            T.showShort(getContext(), response.getException().toString());
                        }

                        @Override
                        public void onFinish() {
                            startActivity(new Intent(getContext(),LoginRegisterActivity.class));
                            getActivity().finish();
                        }
                    });
                break;

            case R.id.rl_all_order:
                intent.setClass(getContext(), OrderListActivity.class);
                intent.putExtra("tagSelect",0);
                break;
            case R.id.ll_un_pay:
                intent.setClass(getContext(), OrderListActivity.class);
                intent.putExtra("tagSelect",1);
                break;
            case R.id.ll_wait_delivery:
                intent.setClass(getContext(), OrderListActivity.class);
                intent.putExtra("tagSelect",2);
                break;
            case R.id.ll_shipped:
                intent.setClass(getContext(), OrderListActivity.class);
                intent.putExtra("tagSelect",3);
                break;
            case R.id.ll_trade_success:
                intent.setClass(getContext(), OrderListActivity.class);
                intent.putExtra("tagSelect",4);
                break;
            case R.id.ll_address:
                intent.setClass(getContext(), AddressListActivity.class);
                break;
            case R.id.ll_about:
                intent.setClass(getContext(), AboutActivity.class);
                break;
            case R.id.ll_more:
                intent.setClass(getContext(), MoreActivity.class);
                break;
        }
        if (intent.getComponent() !=null){
            startActivity(intent);
        }
    }

}
