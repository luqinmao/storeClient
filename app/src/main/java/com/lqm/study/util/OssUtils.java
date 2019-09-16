package com.lqm.study.util;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 阿里云对象存储
 */
public class OssUtils {

    private static OssUtils instance;
    private final String endpoint ="oss-cn-beijing.aliyuncs.com";//主机地址（OSS文档中有提到）
    private final String bucketName ="lqmdemo";//（文件夹名字）
    private OSSClient ossClient;

    public OssUtils() {

    }

    public static OssUtils getInstance() {
        if (instance ==null) {
            if (instance ==null) {
                return new OssUtils();
            }
        }
        return instance;

    }



    /**
     * 上传图片 上传文件
     * @param context application上下文对象
     * @param ossUpCallback 成功的回调
     * @param imgName 上传到oss后的文件名称，图片要记得带后缀 如：.jpg
     * @param imgPath 图片的本地路径
     */
    public void upImage(Context context,String accessKeyId,String secretKeyId,String securityToken,final OssUtils.OssUpCallback ossUpCallback,final String imgName, String imgPath) {

        getOSs(context,accessKeyId,secretKeyId,securityToken);

        final Date data =new Date();

        data.setTime(System.currentTimeMillis());

        PutObjectRequest putObjectRequest =new PutObjectRequest(bucketName,imgName, imgPath);

        putObjectRequest.setProgressCallback(new OSSProgressCallback() {

            @Override
            public void onProgress(Object request, long currentSize, long totalSize) {
                ossUpCallback.onProgress(currentSize, totalSize);
            }

        });

        ossClient.asyncPutObject(putObjectRequest,new OSSCompletedCallback() {

            @Override
            public void onSuccess(OSSRequest request, OSSResult result) {
                ossUpCallback.onSuccess(imgName,ossClient.presignPublicObjectURL(bucketName,imgName));

            }

            @Override
            public void onFailure(OSSRequest request, ClientException clientException, ServiceException serviceException) {
                ossUpCallback.onFail(clientException.getMessage());
            }
        });

    }


    public interface OssUpCallback {

        void onSuccess(String imgName,String imgUrl);

        void onFail(String message);

         void onProgress(long progress,long totalSize);

    }


    private void getOSs(Context context,String accessKeyId,String secretKeyId,String securityToken) {
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, secretKeyId, securityToken);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf =new ClientConfiguration();
        conf.setConnectionTimeout(15 *1000);// 连接超时，默认15秒
        conf.setSocketTimeout(15 *1000);// socket超时，默认15秒
        conf.setMaxConcurrentRequest(5);// 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2);// 失败后最大重试次数，默认2次
        ossClient =new OSSClient(context,endpoint, credentialProvider);
    }


}
