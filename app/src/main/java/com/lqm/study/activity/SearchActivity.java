package com.lqm.study.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqm.study.R;
import com.lqm.study.adapter.SoftRightAdapter;
import com.lqm.study.base.AppConst;
import com.lqm.study.base.BaseActivity;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.Vo.ProductModel;
import com.lqm.study.util.T;
import com.lqm.study.widget.AutoLinefeedLayout;
import com.lqm.study.widget.IconTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 搜索界面
 */
public class SearchActivity extends BaseActivity
        implements BaseQuickAdapter.RequestLoadMoreListener{


    @Bind(R.id.tv_ic_search)
    IconTextView tvIcSearch;
    @Bind(R.id.et_search)
    EditText etSearch;
    @Bind(R.id.tv_clean_input)
    IconTextView tvCleanInput;
    @Bind(R.id.rl_search)
    RelativeLayout rlSearch;
    @Bind(R.id.tv_return)
    TextView tvReturn;
    @Bind(R.id.layout_hot_key)
    AutoLinefeedLayout layoutHotKey;
    @Bind(R.id.ll_hot_key)
    LinearLayout llHotKey;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;
    private SoftRightAdapter mAdapter;
    private int pageSize = 10;
    private int pageNum =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        initListener();
    }

    private void initView() {
        rvContent.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        mAdapter = new SoftRightAdapter(null);
        mAdapter.bindToRecyclerView(rvContent);
        mAdapter.setEmptyView(R.layout.layout_empty_nor);
        mAdapter.setOnLoadMoreListener(this, rvContent);

        setHotKeyIU();

    }



    public void initListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etSearch.getText().toString())){
                    llHotKey.setVisibility(View.VISIBLE);
                    rvContent.setVisibility(View.GONE);
                }else{
                    searchData(etSearch.getText().toString());
                }

            }
        });

    }

    //热门搜索
    public void setHotKeyIU() {
        layoutHotKey.removeAllViews();
        final List<String> hotKeys = new ArrayList<>();
        hotKeys.add("洗衣机");
        hotKeys.add("冰箱");
        hotKeys.add("手机");
        hotKeys.add("手");
        hotKeys.add("手环");
        for (int i = 0; i < hotKeys.size(); i++) {
            View view = LinearLayout.inflate(SearchActivity.this, R.layout.item_hot_key, null);
            TextView textView = (TextView) view.findViewById(R.id.textview);
            textView.setText(hotKeys.get(i));
            textView.setBackgroundResource(R.drawable.shape_bg_round_rect_main);
            layoutHotKey.addView(view);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etSearch.setText(hotKeys.get(finalI));

                    // 将光标移至字符串尾部
                    CharSequence charSequence = etSearch.getText();
                    if (charSequence instanceof Spannable) {
                        Spannable spanText = (Spannable) charSequence;
                        Selection.setSelection(spanText, charSequence.length());
                    }
                }
            });
        }
    }

    @OnClick({R.id.tv_clean_input, R.id.tv_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_clean_input:
                etSearch.setText("");
                break;
            case R.id.tv_return:
                finish();
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        String keyWord = etSearch.getText().toString();
        if (!TextUtils.isEmpty(keyWord)) {
            getMoreData(etSearch.getText().toString());
        }
    }


    //搜索
    public void searchData(String keyword) {
        pageNum = 1;
        OkGo.<ResponseData<ProductModel>>post(AppConst.Product.list)
                .params("keyword",keyword)
                .params("pageNum",pageNum)
                .params("pageSize",pageSize)
                .execute(new JsonCallback<ResponseData<ProductModel>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ProductModel>> response) {

                        ResponseData<ProductModel> model = response.body();

                        if (model.getData() !=null && model.getData().getSize() >0){
                            llHotKey.setVisibility(View.GONE);
                            rvContent.setVisibility(View.VISIBLE);
                        }else {
                            llHotKey.setVisibility(View.GONE);
                            rvContent.setVisibility(View.VISIBLE);
                        }
                        mAdapter.setNewData(model.getData().getList());
                    }

                    @Override
                    public void onError(Response<ResponseData<ProductModel>> response) {
                        T.showShort(response.message());
                    }
                });
    }

    //加载更多
    public void getMoreData(String keyword) {
        pageNum += 10;
        OkGo.<ResponseData<ProductModel>>post(AppConst.Product.list)
                .params("keyword",keyword)
                .params("pageNum",pageNum)
                .params("pageSize",pageSize)
                .execute(new JsonCallback<ResponseData<ProductModel>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ProductModel>> response) {

                        ResponseData<ProductModel> model = response.body();
                        if (model.getData() !=null && model.getData().getList() !=null
                                && model.getData().getList().size() >0){
                            mAdapter.setNewData(model.getData().getList());
                            mAdapter.loadMoreComplete();
                        }else{
                            mAdapter.loadMoreEnd();
                        }

                    }

                    @Override
                    public void onError(Response<ResponseData<ProductModel>> response) {
                        T.showShort(response.message());
                        mAdapter.loadMoreFail();
                    }
                });
    }


}
