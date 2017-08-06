package uts.user.dao;


import java.util.List;

import com.newbig.app.web.config.MyBatisConfig;
import com.newbig.app.web.mapper.UserMapper;
import com.newbig.app.web.model.User;
import com.newbig.app.web.service.UserService;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit4.SpringRunner;


public class UserMapperTest {

    private EmbeddedDatabase db;

    UserMapper userMapper;

    @Before
    public void setUp() {
        //db = new EmbeddedDatabaseBuilder().addDefaultScripts().build();
        db = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("sql/schema.sql")
            .addScript("sql/data.sql")
            .build();
    }

    @Test
    public void testAddUser() {
        User user = new User(1L,"admin");
        userMapper.insertSelective(user);
        Assert.assertNotNull(user.getId());
    }

    @Test
    public void testDeleteUserById() {
        User user = new User(1L,"admin");
        userMapper.delete(user);
        Integer count = userMapper.deleteByPrimaryKey(user.getId());
        Assert.assertEquals(new Integer(1), count);
    }

    @Test
    public void testUpdateUser() {
        User user = new User(1L,"admin");
        userMapper.insertSelective(user);
        user.setName("hr");
        userMapper.updateByPrimaryKeySelective(user);
        Assert.assertEquals("hr", user.getName());
    }
    @After
    public void tearDown() {
        db.shutdown();
    }
}

