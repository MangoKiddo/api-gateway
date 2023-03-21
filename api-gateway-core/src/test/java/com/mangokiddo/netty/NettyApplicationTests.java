package com.mangokiddo.netty;

import com.mangokiddo.netty.session.SessionServer;
import io.netty.channel.Channel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class NettyApplicationTests {

    private final Logger logger = LoggerFactory.getLogger(NettyApplicationTests.class);

    @Test
    void contextLoads() throws ExecutionException, InterruptedException {

        SessionServer sessionServer = new SessionServer();
        Future<Channel> submit = Executors.newFixedThreadPool(2).submit(sessionServer);
        Channel channel = submit.get();

        if (channel == null){
            logger.error("netty server error!");
            throw new RuntimeException("netty server start error channel is null");
        }
        while(!channel.isActive()){
            logger.info("NettyServer启动服务 ...");
            Thread.sleep(500);
        }
        logger.info("NettyServer启动服务完成 {}", channel.localAddress());

        Thread.sleep(Long.MAX_VALUE);
    }

}
