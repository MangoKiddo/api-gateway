package com.mangokiddo.netty.session;

import com.mangokiddo.netty.session.handlers.ServerSessionHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class SessionChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        socketChannel.pipeline().addLast(new HttpRequestDecoder());
        socketChannel.pipeline().addLast(new HttpResponseEncoder());
        //用于处于get请求外post请求的请求体内容
        socketChannel.pipeline().addLast(new HttpObjectAggregator(1024 * 1024 ));

        socketChannel.pipeline().addLast(new ServerSessionHandler());

    }
}
