package com.ckjs.ck.Interface;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 * 网络请求后的status值的1，0，2三种状态事件处理的接口封装
 */

public interface TaskResultApi {
    void initStatusOne();

    void initStatusZero();

    void initStatusTwo();
}
