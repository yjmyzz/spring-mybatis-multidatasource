package com.cnblogs.yjmyzz.service.impl;

import com.cnblogs.yjmyzz.entity.UserEntity;
import com.cnblogs.yjmyzz.mapper.UserEntityMapper;
import com.cnblogs.yjmyzz.service.UserService;
import com.cnblogs.yjmyzz.utils.DBContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangjunming on 2/15/16.
 * author: yangjunming@huijiame.com
 */

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserEntityMapper userEntityMapper;


    @Override
    public void addUser(UserEntity userEntity) {
        //switch db
        DBContext.setDBKey(DBContext.getDBKeyByUserId(userEntity.getUserId()));
        userEntityMapper.insertSelective(userEntity);
    }

    @Override
    public UserEntity getUser(int userId) {
        //switch db
        DBContext.setDBKey(DBContext.getDBKeyByUserId(userId));
        return userEntityMapper.selectByPrimaryKey(userId);
    }
}
