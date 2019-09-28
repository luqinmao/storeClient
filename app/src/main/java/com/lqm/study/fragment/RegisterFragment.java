package com.lqm.study.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lqm.study.R;
import com.lqm.study.activity.MainActivity;
import com.lqm.study.common.broadcast.CommonEvent;
import com.lqm.study.base.App;
import com.lqm.study.base.AppConst;
import com.lqm.study.base.BaseFragment;
import com.lqm.study.helper.JsonCallback;
import com.lqm.study.helper.ResponseCode;
import com.lqm.study.helper.ResponseData;
import com.lqm.study.model.User;
import com.lqm.study.model.pojo.StsTokenBean;
import com.lqm.study.util.OssUtils;
import com.lqm.study.util.T;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

import static android.view.View.inflate;

/**
 * user：lqm
 * desc：注册
 */

public class RegisterFragment extends BaseFragment {

    private static final int IMAGE_PICKER = 100;

    @Bind(R.id.et_account)
    EditText mEtAccount;
    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.et_email)
    EditText mEtEmail;
    @Bind(R.id.tv_register)
    TextView mTvRegister;
    @Bind(R.id.iv_photo)
    ImageView mIvPhoto;
    private File mPhotoFile;
    private String userName;
    private String password;
    private String email;

    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,

    };

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View initView() {
        View view = inflate(getActivity(), R.layout.frag_register, null);
        ButterKnife.bind(this, view);
        return view;

    }

    @OnClick({R.id.iv_photo, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_photo:
                RxPermissions rxPermissions=new RxPermissions(getActivity());
                rxPermissions.request(PERMISSIONS).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean){
                            App.getImagePickerInstance().setMultiMode(false);
                            Intent intent1 = new Intent(getContext(), ImageGridActivity.class);
                            startActivityForResult(intent1, IMAGE_PICKER);
                        }else{
                            T.showShort("请授予相关权限");
                        }
                    }
                });


                break;
            case R.id.tv_register:
                toUploadPhoto();
                break;
        }
    }

    public void toUploadPhoto() {
        userName = mEtAccount.getText().toString().trim();
        password = mEtPassword.getText().toString().trim();
        email = mEtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            T.showShort(getContext(), "用户名不能为空");
            return;
        } else if (TextUtils.isEmpty(password)) {
            T.showShort(getContext(), "密码不能为空");
            return;
        } else if (TextUtils.isEmpty(email)) {
            T.showShort(getContext(), "邮箱不能为空");
            return;
        }
        else if (mPhotoFile == null || !mPhotoFile.exists()) {
            T.showShort(getContext(), "请上传你的头像");
            return;
        }
        else {
            showWaitingDialog("正在注册...");

            //1
            OkGo.<ResponseData<StsTokenBean>>post(AppConst.getStsToken)
                    .execute(new JsonCallback<ResponseData<StsTokenBean>>() {
                        @Override
                        public void onSuccess(Response<ResponseData<StsTokenBean>> response) {
                            ResponseData<StsTokenBean> data =  response.body();
                            OssUtils.getInstance().upImage(
                                    getContext(),
                                    data.getData().getAccessKeyId(),
                                    data.getData().getAccessKeySecret(),
                                    data.getData().getSecurityToken(),
                                    new OssUtils.OssUpCallback() {

                                        @Override
                                        public void onSuccess(String imgName,String imgUrl) {
                                            toRegisterUser(imgName);
                                        }

                                        @Override
                                        public void onFail(String message) {
                                            T.showShort(message);
                                            hideWaitingDialog();
                                        }

                                        @Override
                                        public void onProgress(long progress, long totalSize) {

                                        }
                                     },mPhotoFile.getName(),mPhotoFile.getPath());
                        }

                        @Override
                        public void onError(Response<ResponseData<StsTokenBean>> response) {
                            T.showShort(response.message());
                            hideWaitingDialog();
                        }
                    });

        }
    }

    private void toRegisterUser(String imgName){
        OkGo.<ResponseData<User>>post(AppConst.User.register)
                .params("username",userName)
                .params("password",password)
                .params("email",email)
                .params("photo",imgName)
                .execute(new JsonCallback<ResponseData<User>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<User>> response) {
                        ResponseData<User> data = response.body();
                        if (data.getStatus() == ResponseCode.SUCCESS.getCode()){
                            T.showShort(getContext(),"注册成功");
                            toLogin(userName,password);
                        }else{
                            T.showShort(getContext(),"注册失败:"+data.getMsg());
                            hideWaitingDialog();
                        }

                    }

                    @Override
                    public void onError(Response<ResponseData<User>> response) {
                        T.showShort(response.getException().toString());
                        hideWaitingDialog();
                    }

                });
    }

    private void toLogin(String userName, String password) {
        OkGo.<ResponseData<User>>post(AppConst.User.login)
                .params("username",userName)
                .params("password",password)
                .execute(new JsonCallback<ResponseData<User>>() {
                    @Override
                    public void onSuccess(Response<ResponseData<User>> response) {
                        ResponseData<User> data = response.body();
                        if (data.getStatus() == ResponseCode.SUCCESS.getCode()){
                            App.setUserInfo(data.getData());
                            App.setOkGoToken(data.getMsg());
                            EventBus.getDefault().post(new CommonEvent("登录状态改变"));
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                            T.showShort(getContext(),"登录成功");

                        }else{
                            T.showShort(getContext(),"登录失败："+data.getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<ResponseData<User>> response) {
                        T.showShort(response.getException().toString());
                    }

                    @Override
                    public void onFinish() {
                        hideWaitingDialog();
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//返回多张照片
            if (data != null) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>)
                        data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    mPhotoFile = new File(images.get(0).path);
                    try {
                        if (mPhotoFile != null) {
                            Bitmap bt = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());//图片地址
                            mIvPhoto.setImageBitmap(bt);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
