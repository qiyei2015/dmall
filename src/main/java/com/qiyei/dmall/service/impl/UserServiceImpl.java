package com.qiyei.dmall.service.impl;

import com.qiyei.dmall.common.Constant;
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
        int count = userMapper.checkUsername(name);
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

    @Override
    public Response<String> register(User user) {
        // TODO: 2020/2/15  校验用户名，邮箱，电话
        Response<String> response = checkValid(Constant.USERNAME,user.getUsername());
        if (!response.isSuccess()){
            return response;
        }

        response = checkValid(Constant.EMAIL,user.getEmail());
        if (!response.isSuccess()){
            return response;
        }

        user.setRole(Constant.Role.ROLE_CUSTOMER);
        user.setPassword(MD5Utils.MD5EncodeUtf8(user.getPassword()));
        //2 MD5加密密码
        int count = userMapper.insert(user);
        if (count == 0){
            return Response.createByErrorMessage("注册失败");
        }
        return Response.createBySuccessMessage("注册成功");
    }

    @Override
    public Response<String> checkValid(String type, String value) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(value)){
            return Response.createByErrorMessage("参数错误");
        }
        Response response = Response.createBySuccess();
        int count = 0;
        switch (type){
            case Constant.USERNAME:
                //1 检查用户名
                count = userMapper.checkUsername(value);
                if (count > 0){
                    response =  Response.createByErrorMessage("已经注册过了,请直接登录");
                }
                break;
            case Constant.EMAIL:
                count = userMapper.checkEmail(value);
                if (count > 0){
                    response =  Response.createByErrorMessage("Email已经存在");
                }
                break;
            default:
                response = Response.createByErrorMessage("找不到匹配的参数类型");
                break;
        }
        return response;
    }
}
