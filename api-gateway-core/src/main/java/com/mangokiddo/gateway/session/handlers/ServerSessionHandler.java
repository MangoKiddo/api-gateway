package com.mangokiddo.gateway.session.handlers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mangokiddo.gateway.session.BaseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @description:
 * @author: mango
 * @time: 2023/3/9 18:02
 */
public class ServerSessionHandler extends BaseHandler<FullHttpRequest> {

    private final Logger logger = LoggerFactory.getLogger(ServerSessionHandler.class);

    @Override
    protected void handleSession(ChannelHandlerContext ctx, Channel channel, FullHttpRequest fullHttpRequest) {

        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);


        fullHttpResponse.content().writeBytes(JSON.toJSONBytes("你访问路径被好兄弟mango的网关管理了 URI：" + fullHttpRequest.uri(), SerializerFeature.PrettyFormat));

        HttpHeaders headers = fullHttpResponse.headers();
        headers.add(HttpHeaderNames.CONTENT_TYPE,HttpHeaderValues.APPLICATION_JSON + "; charset=utf-8");
        //持久连接
        headers.add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        //时间
        headers.add(HttpHeaderNames.DATE,new Date());
        //响应长度
        headers.add(HttpHeaderNames.CONTENT_LENGTH,fullHttpResponse.content().readableBytes());

        // 配置跨域访问
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE");
        headers.add(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

        //异步响应信息

        channel.writeAndFlush(fullHttpResponse);
    }
}
