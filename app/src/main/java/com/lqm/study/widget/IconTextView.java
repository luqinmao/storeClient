package com.lqm.study.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * @user  lqm
 * @desc  使用IConFont TextView
 */
public class IconTextView extends android.support.v7.widget.AppCompatTextView {

    private Context mContext;

    public IconTextView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        Typeface iconfont = Typeface.createFromAsset(mContext.getAssets(), "iconfont.ttf");
        setTypeface(iconfont);
    }
}
