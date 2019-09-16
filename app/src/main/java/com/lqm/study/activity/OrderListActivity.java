package com.lqm.study.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.lqm.study.R;
import com.lqm.study.adapter.TabFragAdapter;
import com.lqm.study.common.BaseActivity;
import com.lqm.study.fragment.OrderListStatusFragment;
import com.lqm.study.widget.IconTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的订单界面
 */
public class OrderListActivity extends BaseActivity {


    @Bind(R.id.it_return)
    IconTextView itReturn;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    private List<Fragment> mFragments = new ArrayList<>();
    private String[] tabTitles = {"全部","待付款","待发货","待收货","已完成",};
    private int[] orderStatuss = {-1,10,20,40,50};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        initView();

    }

    private void initView() {
        int tagSelect = getIntent().getIntExtra("tagSelect",0);

        for (int i = 0; i < tabTitles.length; i++) {
            mFragments.add(OrderListStatusFragment.newInstance(tabTitles[i],orderStatuss[i]));
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[0]));
        }
        viewPager.setAdapter(new TabFragAdapter(getSupportFragmentManager(),mFragments,Arrays.asList(tabTitles)));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(tagSelect);

    }

    @OnClick(R.id.it_return)
    public void onViewClicked() {
        finish();
    }

}
