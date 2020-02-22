package com.qiyei.dmall.dao;

import com.qiyei.dmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    /**
     * xml中引用需要和注解中的值相同
     * @param username
     * @param password
     * @return
     */
    User selectLogin(@Param("username") String username, @Param("password")String password);

    /**
     * 根据用户名查询问题
     * @param username
     * @return
     */
    String queryQuestionByName(@Param("username") String username);

    /**
     * 校验密码答案
     * @param username
     * @param password
     * @param answer
     * @return
     */
    int checkPasswordAnswer(@Param("username") String username, @Param("password")String password,@Param("answer") String answer);

    /**
     *
     * @param id
     * @param password
     * @return
     */
    int checkPassword(@Param("id") Integer id,@Param("password") String password);

    /**
     * 更新密码
     * @param username
     * @param password
     * @return
     */
    int updatePasswordByUsername(@Param("username") String username,@Param("password") String password);

    /**
     * 校验email
     * @param id
     * @param email
     * @return
     */
    int checkEmailByUserId(@Param("id") Integer id,@Param("email") String email);
}