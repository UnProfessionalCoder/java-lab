package com.newbig.app.web.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChatLisener implements ApplicationListener<ContextRefreshedEvent> {

    public Integer websocketPort = 9688;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
        log.info(websocketPort + "----000000");
//        NewBigChatStart.start(websocketPort);
    }
}
