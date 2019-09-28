package com.lqm.study.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lqm.study.R;
import com.lqm.study.adapter.FragPagerAdapter;
import com.lqm.study.common.broadcast.CommonEvent;
import com.lqm.study.base.App;
import com.lqm.study.base.BaseActivity;
import com.lqm.study.fragment.CartFragment;
import com.lqm.study.fragment.HomeFragment;
import com.lqm.study.fragment.SoftFragment;
import com.lqm.study.fragment.UserFragment;
import com.lqm.study.util.T;
import com.lqm.study.util.UIUtil;
import com.lqm.study.widget.IconTextView;
import com.lqm.study.widget.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @Bind(R.id.viewpager)
    NoScrollViewPager viewpager;
    @Bind(R.id.it_home)
    IconTextView itHome;
    @Bind(R.id.tv_home)
    TextView tvHome;
    @Bind(R.id.ll_home)
    LinearLayout llHome;
    @Bind(R.id.it_sort)
    IconTextView itSort;
    @Bind(R.id.tv_sort)
    TextView tvSort;
    @Bind(R.id.ll_soft)
    LinearLayout llSoft;
    @Bind(R.id.it_cart)
    IconTextView itCart;
    @Bind(R.id.tv_cart)
    TextView tvCart;
    @Bind(R.id.ll_cart)
    LinearLayout llCart;
    @Bind(R.id.it_user)
    IconTextView itUser;
    @Bind(R.id.tv_user)
    TextView tvUser;
    @Bind(R.id.ll_user)
    LinearLayout llUser;

    private long mExitTime;
    private UserFragment userFragment;
    private List<Fragment> mFragments = new ArrayList<>();
    private CartFragment cartFragment;
    private SoftFragment softFragment;
    private HomeFragment homeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStateBarColor(R.color.colorPrimaryDark);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        initView();
    }


    private void initView() {

        homeFragment = HomeFragment.newInstance();
        softFragment = SoftFragment.newInstance();
        cartFragment = CartFragment.newInstance();
        userFragment = UserFragment.newInstance();
        mFragments.add(homeFragment);
        mFragments.add(softFragment);
        mFragments.add(cartFragment);
        mFragments.add(userFragment);


        viewpager.setAdapter(new FragPagerAdapter(getSupportFragmentManager(), mFragments));
        viewpager.setCurrentItem(0, false);
        viewpager.setOffscreenPageLimit(3);
        setTabUI(0);

    }

    @OnClick({R.id.ll_home, R.id.ll_soft, R.id.ll_cart, R.id.ll_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                setTabUI(0);
                break;
            case R.id.ll_soft:
                setTabUI(1);
                break;
            case R.id.ll_cart:
                setTabUI(2);
                break;
            case R.id.ll_user:
                setTabUI(3);
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                T.showShort(this, "再按一次退出");
                mExitTime = System.currentTimeMillis();
            } else {
                App.exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setTabUI(int position) {

        itHome.setTextColor(UIUtil.getColor(R.color.tab_nor_color));
        tvHome.setTextColor(UIUtil.getColor(R.color.tab_nor_color));
        itSort.setTextColor(UIUtil.getColor(R.color.tab_nor_color));
        tvSort.setTextColor(UIUtil.getColor(R.color.tab_nor_color));
        itCart.setTextColor(UIUtil.getColor(R.color.tab_nor_color));
        tvCart.setTextColor(UIUtil.getColor(R.color.tab_nor_color));
        itUser.setTextColor(UIUtil.getColor(R.color.tab_nor_color));
        tvUser.setTextColor(UIUtil.getColor(R.color.tab_nor_color));

        if (position == 0){
            itHome.setTextColor(UIUtil.getColor(R.color.tab_sel_color));
            tvHome.setTextColor(UIUtil.getColor(R.color.tab_sel_color));
        }else if (position ==1){
            itSort.setTextColor(UIUtil.getColor(R.color.tab_sel_color));
            tvSort.setTextColor(UIUtil.getColor(R.color.tab_sel_color));
        }else if (position ==2){
            itCart.setTextColor(UIUtil.getColor(R.color.tab_sel_color));
            tvCart.setTextColor(UIUtil.getColor(R.color.tab_sel_color));
        } else if (position ==3){
            itUser.setTextColor(UIUtil.getColor(R.color.tab_sel_color));
            tvUser.setTextColor(UIUtil.getColor(R.color.tab_sel_color));
        }
        viewpager.setCurrentItem(position);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CommonEvent event) {
        if (event.getMessage().equals("登录状态改变")) {
            userFragment.setUserUI();
        }
    }

}
