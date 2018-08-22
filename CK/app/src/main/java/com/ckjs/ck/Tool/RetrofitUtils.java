package com.ckjs.ck.Tool;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class RetrofitUtils {
    /**
     * 开发和上线时要分别进行调整
     */
    private static String baseUrl = "https://www.chaokongs.com/v2/";//baseUrl
    //    private static String baseUrl = "http://test.chaokongs.com/v2/";//baseUrl//开发
    public static String picUrl = "http://www.chaokongs.com/";//picUrl
    //    public static String picUrl = "http://test.chaokongs.com/";//picUrl//开发


    public static String baseUrlH5 = "https://www.chaokongs.com/";//baseUrlH5
    public static String httpH5Jq = "http://www.chaokongs.com/";//httpH5Jq
    public static Retrofit retrofit;//单例retrofit对象

    /**
     * 初始化http
     *
     * @return
     */
    public static Retrofit init() {
        //初始化
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())//配置gson解析json
                    .build();
        }
        return retrofit;

    }
}
