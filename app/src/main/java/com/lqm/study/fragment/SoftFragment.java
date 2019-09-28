package com.lqm.study.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqm.study.R;
import com.lqm.study.adapter.SoftLeftAdapter;
import com.lqm.study.adapter.SoftRightAdapter;
import com.lqm.study.base.AppConst;
import com.lqm.study.base.BaseFragment;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.Vo.ProductModel;
import com.lqm.study.model.pojo.CategoryBean;
import com.lqm.study.model.pojo.ProductBean;
import com.lqm.study.util.T;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.inflate;

/**
 * user：lqm
 * desc：分类模块
 */

public class SoftFragment extends BaseFragment {

    @Bind(R.id.rv_left)
    RecyclerView rvLeft;
    @Bind(R.id.rv_right)
    RecyclerView rvRight;

    List<CategoryBean> leftDatas = new ArrayList<>();
    List<ProductBean> rightDatas = new ArrayList<>();

    private SoftLeftAdapter leftAdapter;
    private SoftRightAdapter rightAdapter;


    public static SoftFragment newInstance() {
        return new SoftFragment();
    }

    @Override
    public View initView() {
        View view = inflate(getActivity(), R.layout.frag_soft, null);
        ButterKnife.bind(this, view);
        return view;

    }



    @Override
    public void initData() {


        rvLeft.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        leftAdapter = new SoftLeftAdapter(leftDatas);
        rvLeft.setAdapter(leftAdapter);

        rvRight.setLayoutManager(new LinearLayoutManager(getContext()));
        rightAdapter = new SoftRightAdapter(rightDatas);
        rightAdapter.bindToRecyclerView(rvRight);
        rightAdapter.setEmptyView(R.layout.layout_empty_nor);

        setLeftUIData();

    }

    @Override
    public void initListener() {
        leftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryBean categoryBean = leftAdapter.getData().get(position);
                if (categoryBean != null ){
                    for (CategoryBean bean: leftAdapter.getData()) {
                        bean.setSelect(false);
                    }
                    leftAdapter.getData().get(position).setSelect(true);
                    leftAdapter.notifyDataSetChanged();
                    setRightUIData(categoryBean.getId());
                }
            }
        });



    }

    private void setLeftUIData() {
        OkGo.<ResponseData<List<CategoryBean>>>post(AppConst.Category.list)
                .execute(new JsonCallback<ResponseData<List<CategoryBean>>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<List<CategoryBean>>> response) {

                        ResponseData<List<CategoryBean>> model = response.body();
                        if (model.getStatus() == 0) {
                            model.getData().get(0).setSelect(true);
                            leftAdapter.setNewData(model.getData());
                            setRightUIData(model.getData().get(0).getId());
                        }

                    }

                    @Override
                    public void onError(Response<ResponseData<List<CategoryBean>>> response) {
                        T.showShort(response.message());
                    }
                });


    }

    private void setRightUIData(int categoryId) {

        OkGo.<ResponseData<ProductModel>>post(AppConst.Product.list)
                .params("categoryId",categoryId)
                .execute(new JsonCallback<ResponseData<ProductModel>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ProductModel>> response) {

                        ResponseData<ProductModel> model = response.body();
                        if (model.getStatus() == 0){
                            rightAdapter.setNewData(model.getData().getList());
                        }

                    }

                    @Override
                    public void onError(Response<ResponseData<ProductModel>> response) {
                        T.showShort(response.message());
                    }
                });

    }

}
