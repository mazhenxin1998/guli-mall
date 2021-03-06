package com.mzx.gulimall.im.config;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @author ZhenXinMa
 * @date 2020/8/18 22:12
 */
public class GetHttpSessionConfiguration extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {

        /*
         * --------------------------------------------------------
         * 如果做了SpringSession的全局配置,那么这里获取到的Session就应该是
         * 全局的Session.
         * --------------------------------------------------------
         * */
        // 这个httpSession是获取不到的吗?
        // 显示可以获取到值的.
        // TODO: 但是这里有一点是需要确定的是: 如果用户未登录时不能进行访问的.
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}
