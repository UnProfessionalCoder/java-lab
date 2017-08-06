package com.newbig.app;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by xiaofan on 17-5-18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
//@org.springframework.boot.google.autoconfigure.web.reactive.WebFluxTest
@Ignore
public class MockitoBasedTest {

    @Before
    public void setUp() throws Exception {
        // 初始化测试用例类中由Mockito的注解标注的所有模拟对象
        MockitoAnnotations.initMocks(this);
    }

}
