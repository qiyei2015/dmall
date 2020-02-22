package com.qiyei.dmall.controller.portal;

import com.qiyei.dmall.common.Constant;
import com.qiyei.dmall.common.Response;
import com.qiyei.dmall.common.ResponseCode;
import com.qiyei.dmall.pojo.User;
import com.qiyei.dmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Created by qiyei2015 on 2020/2/13.
 * @version: 1.0
 * @email: 1273482124@qq.com
 * @description: 用户管理相关控制器
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    /**
     * 与UserServiceImpl中service注解名字相同
     */
    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     * 配置ResponseBody便于自动反序列化
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<User> login(String username, String password, HttpSession session){
        //调用service
        Response<User> response = iUserService.login(username,password);
        if (response.isSuccess()){
            session.setAttribute(Constant.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<String> logout(String username, String password, HttpSession session){
        //调用service
        session.removeAttribute(Constant.CURRENT_USER);
        Response<String> response = Response.createBySuccessMessage("退出登录成功");
        return response;
    }

    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<String> register(User user){
        //调用service
        Response<String> response = iUserService.register(user);
        return response;
    }

    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<String> checkValid(String type,String value){
        //调用service
        Response<String> response = iUserService.checkValid(type,value);
        return response;
    }

    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if (user == null){
            return Response.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }
        return Response.createBySuccess(user);
    }

    @RequestMapping(value = "get_password_question.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<String> getPasswordQuestion(String username){
        return iUserService.queryQuestion(username);
    }

    @RequestMapping(value = "check_password_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<String> checkPasswordAnswer(String username,String password,String answer){
        return iUserService.checkPasswordAnswer(username,password,answer);
    }

    /**
     * 登陆中重置密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null){
            return Response.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(user,passwordOld,passwordNew);
    }

    /**
     * 忘记密码重置
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    @RequestMapping(value = "update_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<User> updateUserInfo(HttpSession session,User user){
        //登录状态下
        User currentUser = (User)session.getAttribute(Constant.CURRENT_USER);
        if(currentUser == null){
            return Response.createByErrorMessage("用户未登录");
        }

        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        Response<User> response = iUserService.updateUserInfo(user);
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Constant.CURRENT_USER,response.getData());
        }
        return response;
    }

    @RequestMapping(value = "get_user_info_with_login.do",method = RequestMethod.POST)
    @ResponseBody
    public Response<User> getUserInfoWithLogin(HttpSession session){
        //登录状态下
        User currentUser = (User)session.getAttribute(Constant.CURRENT_USER);
        if(currentUser == null){
            return Response.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        return iUserService.getUserInfo(currentUser.getId());
    }


}
