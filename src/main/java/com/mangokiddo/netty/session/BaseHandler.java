package com.mangokiddo.netty.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author: mango
 * @time: 2023/3/9 16:58
 */
public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) {
        handleSession(ctx, ctx.channel(), msg);
    }

    /**
     * 抽象会话处理方法
     * @param ctx
     * @param channel
     * @param request
     */
    protected abstract void handleSession(ChannelHandlerContext ctx, final Channel channel, T request);


}
