package com.codinger.dao;

import com.codinger.entity.UserEntity;

public interface UserDao  {

    public void saveUser(UserEntity user);

    public UserEntity findUserByUserName(String userName);

    public long updateUser(UserEntity user);

    public void deleteUserById(Long id);

}
