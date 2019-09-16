package com.lqm.study.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lqm.study.R;
import com.lqm.study.common.BaseActivity;
import com.lqm.study.widget.IconTextView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 关于界面
 */
public class AboutActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();

    }

    private void initView() {


    }

}
