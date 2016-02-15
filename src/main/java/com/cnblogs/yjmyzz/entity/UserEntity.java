package com.cnblogs.yjmyzz.entity;

import javax.persistence.*;

@Table(name = "T_USER")
public class UserEntity {
    @Id
    @Column(name = "D_USER_ID")
    private Integer userId;

    @Column(name = "D_USER_NAME")
    private String userName;

    /**
     * @return D_USER_ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return D_USER_NAME
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }

    public UserEntity(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public UserEntity() {
    }
}