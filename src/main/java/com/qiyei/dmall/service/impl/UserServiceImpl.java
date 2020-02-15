package com.qiyei.dmall.service.impl;

import com.qiyei.dmall.common.Response;
import com.qiyei.dmall.dao.UserMapper;
import com.qiyei.dmall.pojo.User;
import com.qiyei.dmall.service.IUserService;
import com.qiyei.dmall.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Created by qiyei2015 on 2020/2/15.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description:
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    /**
     * 自动注入
     */
    @Autowired
    private UserMapper userMapper;

    @Override
    public Response<User> login(String name, String password) {

        //1 检查用户名
        int count = userMapper.checkUserName(name);
        if (count == 0){
            return Response.createByErrorMessage("用户名不存在");
        }

        //2 MD5加密密码
        User user = userMapper.selectLogin(name, MD5Utils.MD5EncodeUtf8(password));
        if (user == null){
            return Response.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return Response.createBySuccess("登录成功",user);
    }

}
