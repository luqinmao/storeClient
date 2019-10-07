package com.lqm.study.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.lqm.study.R;
import com.lqm.study.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发现更多界面
 */
public class MoreActivity extends BaseActivity {

    @Bind(R.id.it_return)
    TextView itReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

    }


    @OnClick({R.id.it_return})
    public void onViewClicked() {
        finish();
    }

}
