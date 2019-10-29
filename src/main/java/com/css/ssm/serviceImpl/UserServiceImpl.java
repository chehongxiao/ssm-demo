package com.css.ssm.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.css.ssm.dao.IUserDao;
import com.css.ssm.model.User;
import com.css.ssm.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {

    @Resource  
    private IUserDao userDao;  
    
    @Override
    public User getUserById(int userId) {
        return this.userDao.selectByPrimaryKey(userId);
    }

}
