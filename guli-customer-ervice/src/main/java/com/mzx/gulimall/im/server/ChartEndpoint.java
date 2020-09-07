package com.mzx.gulimall.im.server;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.im.config.GetHttpSessionConfiguration;
import com.mzx.gulimall.im.vo.MessageVo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 前台WebSocket链接入口.
 * <p>
 * 这里的session使用自定义的session.
 *
 * @author ZhenXinMa
 * @date 2020/8/18 22:10
 */
@ServerEndpoint(value = "/server", configurator = GetHttpSessionConfiguration.class)
@Component
public class ChartEndpoint {


    public volatile static Integer flag = 0;

    /**
     * 静态字段存在方法区内，在类加载的时候进行初始化
     */
    public static Map<String, ChartEndpoint> onLines = new ConcurrentHashMap<>();


    /**
     * 该Session用户发送消息.
     */
    private Session session;
    /**
     * HTTP中的session用于获取用户名.
     */
    private HttpSession httpSession;

    /**
     * 模拟情况下 第一个登陆的用户必须是客户张三.
     *
     * @param session
     * @param config
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {

        /*
         * --------------------------------------------------------
         * 这里从config中获取到自己指定的session.
         * 先测试下自己能不能获取到该session.
         * --------------------------------------------------------
         * */

        this.session = session;
        // 从session里面获取user应该是可以获取到的.
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        Object user = httpSession.getAttribute("user");
        System.out.println("当前登录的用户是: "+user.toString());
        if (flag == 0) {

            // 表示第一个是用户张三
            onLines.put("张三", this);
            System.out.println("用户张三连接成功. ");
            flag = 1;
        } else {

            onLines.put("小丽", this);
            System.out.println("客服小丽连接成功. ");
        }

    }

    /**
     * 统一消息格式: 发送人:收件人:消息
     *
     * @param session
     * @param message
     */
    @OnMessage
    public void onMessage(Session session, String message) {

        // 发送消息的格式: 发送的消息格式为: 当前用户名字(线上为用户的ID):客服姓名:具体内容
        // 截取中间
        // "aaa"
//        System.out.println("没有进行格式化的message: " + message);
//        message = message.substring(1,message.length() -1);
//        String[] strings = message.split(":");
//        System.out.println(strings.length);
//        String fromName = strings[0];
//        String toName = strings[1];
//        String messageText = "";
//        System.out.println("fromName" + fromName);
//        System.out.println("toName" + toName);
//        if (strings.length < 3) {
//
//            // 小于3说明 发送的内容为空.
//        } else {
//
//            messageText = strings[2];
//            System.out.println("messageText" + messageText);
//        }
//
//        System.out.println("当前接受到的消息为: "+ message);
//        // 通过将消息转发到客服小丽.
//        ChartEndpoint chartEndpoint = onLines.get(toName);
//        Session toSession = chartEndpoint.getSession();
//        try {
//
//            // 页面接收到的消息格式为: 小丽:张三:客服发送的消息格式
//            toSession.getBasicRemote().sendText(message);
//            System.out.println("发送消息成功");
//        } catch (IOException e) {
//
//            e.printStackTrace();
//        }

        // 字符串带"" 表示的是JSON字符串
        System.out.println("接受到的消息格式为: " + message);
        try {

            MessageVo messageVo = JSON.parseObject(message, MessageVo.class);
            if (messageVo != null && !StringUtils.isEmpty(messageVo.getToName())) {

                ChartEndpoint chartEndpoint = onLines.get(messageVo.getToName());
                if (chartEndpoint != null) {

                    // 传过去应该是一个JSON.
                    String s = JSON.toJSONString(messageVo);
                    chartEndpoint.getSession().getBasicRemote().sendText(s);
                }

            }

        } catch (IOException e) {

            e.printStackTrace();
        }


    }

    @OnClose
    public void onClose(Session session) {

        System.out.println("有用户断开连接》 ");
    }


    public Session getSession() {

        return this.session;
    }


}
