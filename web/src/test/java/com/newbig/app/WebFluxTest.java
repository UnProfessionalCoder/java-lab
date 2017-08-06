package com.newbig.app;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.newbig.app.web.model.User;
import com.newbig.app.web.model.dto.UserDto;
import com.newbig.app.web.model.vo.ResponseVO;
import com.newbig.app.web.service.UserService;
import org.assertj.core.util.Lists;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Map;

/**
 * Created by xiaofan on 17-5-22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
public class WebFluxTest {
//    @Autowired
//    private WebTestClient webClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Test
    public void testWelcome() throws Exception {
//        this.webClient.get().uri("/")
//                .accept(MediaType.APPLICATION_JSON_UTF8).exchange()
//                .expectBody(String.class).isEqualTo("Hello World");
        ResponseVO responseVO = ResponseVO.success(Lists.newArrayList(new UserDto(1L, "aaaaa",new Date())));
//        long a=System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++){
            redisTemplate.opsForValue().set("1111", responseVO);
            ResponseVO r = (ResponseVO) redisTemplate.opsForValue().get("1111");
            System.out.println(r.getResult());
        Map<String,Object> mso = Maps.newHashMap();
        mso.put("a",responseVO);
            redisTemplate.opsForHash().put("a","b",mso);
        ResponseVO r1 = (ResponseVO) ((Map)redisTemplate.opsForHash().get("a","b")).get("a");
        System.out.println(r1.getResult());
//        }
//        System.out.println(System.currentTimeMillis()-a);
    }
    @Test
    public void testInsert(){
        long a=System.currentTimeMillis();
        for(int i=0;i<1000;i++) {
            userService.addUser(null);
        }
        System.out.println(System.currentTimeMillis()-a);
    }

    @Test
    public void testSelect(){
        for(int i=0;i<1000;i++) {
            long a=System.currentTimeMillis();
            User u=userService.getUserById(866838610438238208L);
            System.out.println(JSON.toJSONString(u));
            System.out.println(System.currentTimeMillis()-a);
        }
    }
}
