package com.newbig.app.web.service;

import com.github.pagehelper.PageInfo;
import com.newbig.app.web.model.User;
import com.newbig.app.web.model.dto.UserDto;

/**
 * Created by haibo on 2017/3/12.
 */
public interface UserService {
    PageInfo getUserListByPage(Integer pageNum, Integer pageSize);

    User getUserById(Long id);

    User updateUser(UserDto userDto);

    Integer deleteUserById(Long id);

    User addUser(UserDto userDto);
}
