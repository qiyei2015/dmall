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

    /**
     * 注册
     * @param user
     * @return
     */
    Response<String> register(User user);

    /**
     * 校验
     * @param type
     * @param value
     * @return
     */
    Response<String> checkValid(String type,String value);

    /**
     * 根据用户名查询密码
     * @param username
     * @return
     */
    Response<String> queryQuestion(String username);

    /**
     * 校验用户名密码
     * @param username
     * @param password
     * @param answer
     * @return
     */
    Response<String> checkPasswordAnswer(String username,String password,String answer);

    /**
     * 登陆中重置密码
     * @param user
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    Response<String> resetPassword(User user,String passwordOld,String passwordNew);

    /**
     * 忘记密码重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    Response<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    Response<User> updateUserInfo(User user);

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    Response<User> getUserInfo(Integer id);
}
