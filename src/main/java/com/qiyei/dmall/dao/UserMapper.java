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
}