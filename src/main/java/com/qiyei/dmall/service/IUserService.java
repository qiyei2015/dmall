package com.qiyei.dmall.service;

import com.qiyei.dmall.common.Response;
import com.qiyei.dmall.pojo.User;

/**
 * @author Created by qiyei2015 on 2020/2/15.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
public interface IUserService {

    /**
     * 登录接口
     * @param name
     * @param password
     * @return
     */
    Response<User> login(String name, String password);

}
