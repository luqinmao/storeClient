package com.lqm.study.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lqm.study.R;
import com.lqm.study.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 关于界面
 */
public class AboutActivity extends BaseActivity {

    @Bind(R.id.it_return)
    TextView itReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }


    @OnClick({R.id.it_return})
    public void onViewClicked() {
        finish();
    }

}
