package com.newbig.app.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.newbig.app.web.mapper.UserMapper;
import com.newbig.app.web.model.User;
import com.newbig.app.web.model.dto.UserDto;
import com.newbig.app.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by haibo on 2017/3/12.
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public PageInfo getUserListByPage(Integer pageNum, Integer pageSize) {
//        PageHelper.startPage(pageNum, pageSize, false);
//        Example example = new Example(User.class);
        List<User> lu = userMapper.selectAll();
        return new PageInfo(lu);
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User updateUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        userMapper.updateByPrimaryKeySelective(user);
        return getUserById(user.getId());
    }

    @Override
    public Integer deleteUserById(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }

    @Override
    public User addUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        userMapper.insertSelective(user);
        return user;
    }
}
