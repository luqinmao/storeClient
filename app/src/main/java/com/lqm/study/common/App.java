package com.lqm.study.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.lqm.study.common.intercepter.TokenInterceptor;
import com.lqm.study.model.User;
import com.lqm.study.util.ImageLoaderManager;
import com.lqm.study.util.PrefUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by luqinmao on 2018/2/14.
 */

public class App extends Application{

    public static List<Activity> activities = new LinkedList<Activity>();
    private static Context mContext;
    public static App mAppContext;
    private static ImagePicker mImagePicker;
    private static OkGo mOkgo;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        mAppContext = this;

        //okgo网络框架初始化
        initOkGo();

        //图片选择器初始化
        initImagePicker();


    }


    public void initOkGo() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        builder.addInterceptor(new TokenInterceptor());

        mOkgo = OkGo.getInstance()
                .init(this)
                .setOkHttpClient(builder.build()) //设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(3);
        setOkGoToken(getOkGoToken());

    }

    public static void setOkGoToken(String token) {
        PrefUtils.setString(mContext,AppConst.TOKEN_KEY,token);
        HttpHeaders headers = new HttpHeaders();
        headers.put("login_token",token);
        mOkgo.addCommonHeaders(headers);
    }

    public String getOkGoToken() {
        String loginToken = PrefUtils.getString(mContext,AppConst.TOKEN_KEY,"");
        return loginToken;
    }


    public static Context getmContext() {
        return mContext;
    }

    /**
     * 初始化图片选择控件ImagePicker
     */
    private void initImagePicker() {
        mImagePicker = ImagePicker.getInstance();
        mImagePicker.setImageLoader(new ImageLoaderManager());   //设置图片加载器
        mImagePicker.setShowCamera(true);  //显示拍照按钮
        mImagePicker.setCrop(true);        //允许裁剪（单选才有效）
        mImagePicker.setSaveRectangle(true); //是否按矩形区域保存
        mImagePicker.setSelectLimit(6);    //选中数量限制
        mImagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        mImagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        mImagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        mImagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        mImagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    public static ImagePicker getImagePickerInstance() {
        return mImagePicker;
    }

    /**
     * 完全退出
     */
    public static void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }


    public static void setUserInfo(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        PrefUtils.setString(mContext,"userInfo",userJson);

    }

    public static User getUserInfo() {
        Gson gson = new Gson();
        String userInfo = PrefUtils.getString(mContext,"userInfo",null);
        User user  =  gson.fromJson(userInfo, User.class);
        return user;
    }

}
