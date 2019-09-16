package com.lqm.study.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqm.study.R;
import com.lqm.study.activity.SearchActivity;
import com.lqm.study.adapter.SoftRightAdapter;
import com.lqm.study.common.AppConst;
import com.lqm.study.common.BaseFragment;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.Vo.ProductModel;
import com.lqm.study.model.pojo.ProductBean;
import com.lqm.study.util.ImageLoaderManager;
import com.lqm.study.util.T;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

import static android.view.View.inflate;

/**
 * user：lqm
 * desc：首页模块
 */

public class HomeFragment extends BaseFragment
                implements BaseQuickAdapter.RequestLoadMoreListener{

    @Bind(R.id.rl_to_search)
    RelativeLayout rlToSearch;
    @Bind(R.id.banner)
    BGABanner banner;
    @Bind(R.id.rv_content)
    RecyclerView rvContent;

    private int pageSize = 10;
    private int pageNum =1;
    private SoftRightAdapter goodsAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View initView() {
        View view = inflate(getActivity(), R.layout.frag_home, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {

        rvContent.setFocusable(false);
        rvContent.setHasFixedSize(true);
        rvContent.setNestedScrollingEnabled(false);
        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        goodsAdapter = new SoftRightAdapter(null);
        rvContent.setAdapter(goodsAdapter);
        goodsAdapter.setOnLoadMoreListener(this, rvContent);

        initBannerData();
        getContentData();

    }


    private void initBannerData() {

        List<String> images = new ArrayList<>();
        images.add("http://img0.imgtn.bdimg.com/it/u=1391112235,2109989447&fm=26&gp=0.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1558414547968&di=5a17d5d0cc40a6ec13de127d5eb6c01d&imgtype=0&src=http%3A%2F%2Fimg1.cache.netease.com%2Fcatchpic%2F0%2F02%2F024C2DD6DEBC1A57418B930C60875D0A.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1558414546232&di=4a3fb15ec056e94f4969448ba22c03aa&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Ffb63c8cc57bf977754b2c891badf0676358fc53e74af-BnyBi9_fw658");
        banner.setData(R.layout.item_home_banner, images, null);
        banner.setAdapter(new BGABanner.Adapter<View, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, View itemView, String string, int position) {
                ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_image);
                ImageLoaderManager.loadImage(getContext(), string, imageView);
            }
        });
        banner.setDelegate(new BGABanner.Delegate<View, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, String model, int position) {

            }
        });

    }

    @OnClick(R.id.rl_to_search)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }


    private void getContentData() {
        pageNum = 1;
        OkGo.<ResponseData<ProductModel>>post(AppConst.Product.list)
                .params("pageNum",pageNum)
                .params("pageSize",pageSize)
                .execute(new JsonCallback<ResponseData<ProductModel>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ProductModel>> response) {

                        ResponseData<ProductModel> model = response.body();
                        if (model.getData() !=null){
                            goodsAdapter.setNewData(model.getData().getList());
                        }
                    }

                    @Override
                    public void onError(Response<ResponseData<ProductModel>> response) {
                        T.showShort(response.message());
                    }
                });

    }


    @Override
    public void onLoadMoreRequested() {
        pageNum += 10;
        OkGo.<ResponseData<ProductModel>>post(AppConst.Product.list)
                .params("pageNum",pageNum)
                .params("pageSize",pageSize)
                .execute(new JsonCallback<ResponseData<ProductModel>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<ProductModel>> response) {

                        ResponseData<ProductModel> model = response.body();
                        if (model.getData() !=null && model.getData().getList() !=null
                                && model.getData().getList().size() >0){
                            goodsAdapter.setNewData(model.getData().getList());
                            goodsAdapter.loadMoreComplete();
                        }else{
                            goodsAdapter.loadMoreEnd();
                        }

                    }

                    @Override
                    public void onError(Response<ResponseData<ProductModel>> response) {
                        T.showShort(response.message());
                        goodsAdapter.loadMoreFail();
                    }
                });
    }
}
