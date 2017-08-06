package com.newbig.app;

//import com.github.pagehelper.PageInfo;

import com.github.jsonzou.jmockdata.JMockData;
import com.github.pagehelper.PageInfo;
import com.newbig.app.web.mapper.UserMapper;
import com.newbig.app.web.model.User;
import com.newbig.app.web.model.dto.UserDto;
import com.newbig.app.web.service.impl.UserServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

//import tk.mybatis.mapper.entity.Example;

/**
 * Created by xiaofan on 17-5-18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@Ignore
public class UserServiceTest {

    @Mock
    UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserList(){
        List<User> userList = Lists.newArrayList();
        userList.add(new User(1L,"Todo Sample 1"));
        userList.add(new User(2L,"Todo Sample 2"));
        userList.add(new User(3L,"Todo Sample 3"));
        when(userMapper.selectAll()).thenReturn(userList);

        PageInfo<List<User>> result = userService.getUserListByPage(1,20);
        assertEquals(3, result.getList().size());
    }

    @Test
    public void testGetToDoById(){
        User toDo = new User(1L,"Todo Sample 1");
        when(userMapper.selectByPrimaryKey(1L)).thenReturn(toDo);
        User result = userService.getUserById(1L);
        assertEquals("Todo Sample 1", result.getName());
    }

    @Test
    public void saveToDo() {
        UserDto userDto = new UserDto();
        userDto.setName("aaaaa");
        User user = new User(1L, "aaaaa");
        when(userMapper.insertSelective(user)).thenReturn(1);
        User result = userService.addUser(userDto);
        assertEquals("aaaaa", result.getName());
    }



}

