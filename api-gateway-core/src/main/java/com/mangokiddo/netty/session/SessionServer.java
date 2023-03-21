package com.mangokiddo.netty.session;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

public class SessionServer implements Callable<Channel> {


    protected static final Logger logger = LoggerFactory.getLogger(SessionServer.class);

    NioEventLoopGroup boss = new NioEventLoopGroup(1);
    NioEventLoopGroup work = new NioEventLoopGroup();

    private Channel channel;


    @Override
    public Channel call() throws Exception {

        ChannelFuture channelFuture = null;

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new SessionChannelInitializer());

            channelFuture = serverBootstrap.bind(new InetSocketAddress(7397)).syncUninterruptibly();
            this.channel = channelFuture.channel();
        } catch (Exception e) {
            logger.error("netty server start error!", e);
            e.printStackTrace();

        } finally {
            if (channelFuture != null && channelFuture.isSuccess()) {
                logger.info("netty server has started!");
            } else {
                logger.error("netty server start error!");
            }
        }
        return channel;
    }
}
