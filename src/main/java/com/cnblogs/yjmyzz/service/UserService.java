package com.cnblogs.yjmyzz.service;

import com.cnblogs.yjmyzz.entity.UserEntity;

/**
 * Created by yangjunming on 2/15/16.
 * author: yangjunming@huijiame.com
 */
public interface UserService {

    void addUser(UserEntity userEntity);

    UserEntity getUser(int userId);
}
