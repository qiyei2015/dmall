package com.qiyei.dmall.service.impl;

import com.qiyei.dmall.common.Constant;
import com.qiyei.dmall.common.Response;
import com.qiyei.dmall.common.TokenCache;
import com.qiyei.dmall.dao.UserMapper;
import com.qiyei.dmall.pojo.User;
import com.qiyei.dmall.service.IUserService;
import com.qiyei.dmall.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public Response<String> queryQuestion(String username) {
        //1 检查用户名
        Response<String> response = checkValid(Constant.USERNAME,username);
        if (!response.isSuccess()){
            return Response.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.queryQuestionByName(username);
        return Response.createBySuccess(question);
    }

    @Override
    public Response<String> checkPasswordAnswer(String username, String password, String answer) {
        int count = userMapper.checkPasswordAnswer(username,password,answer);
        if (count > 0){
            String token = UUID.randomUUID().toString();
            TokenCache.setToken(username,token);
            return Response.createBySuccess(token);
        }
        return Response.createByErrorMessage("密码答案错误");
    }

    @Override
    public Response<String> resetPassword(User user, String passwordOld, String passwordNew) {
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        int count = userMapper.checkPassword(user.getId(),MD5Utils.MD5EncodeUtf8(passwordOld));
        if (count <= 0){
            return Response.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Utils.MD5EncodeUtf8(passwordNew));
        count = userMapper.updateByPrimaryKeySelective(user);
        if (count > 0){
            Response.createBySuccessMessage("重置密码成功");
        }
        return Response.createByErrorMessage("重置密码错误");
    }

    @Override
    public Response<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return Response.createByErrorMessage("参数错误,token需要传递");
        }
        Response validResponse = this.checkValid(Constant.USERNAME,username);
        if(validResponse.isSuccess()){
            //用户不存在
            return Response.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getToken(username);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return Response.createByErrorMessage("token无效或者过期");
        }

        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            String md5Password  = MD5Utils.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount > 0){
                return Response.createBySuccessMessage("修改密码成功");
            }
        }else{
            return Response.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return Response.createByErrorMessage("修改密码失败");
    }

    @Override
    public Response<User> updateUserInfo(User user) {
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
        int resultCount = userMapper.checkEmailByUserId(user.getId(),user.getEmail());
        if(resultCount > 0){
            return Response.createByErrorMessage("email已存在,请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return Response.createBySuccess("更新个人信息成功",updateUser);
        }
        return Response.createByErrorMessage("更新个人信息失败");
    }

    @Override
    public Response<User> getUserInfo(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if(user == null){
            return Response.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return Response.createBySuccess(user);
    }
}
