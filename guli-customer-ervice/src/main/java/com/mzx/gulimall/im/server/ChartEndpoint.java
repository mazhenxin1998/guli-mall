package com.mzx.gulimall.im.server;

import com.alibaba.fastjson.JSON;
import com.mzx.gulimall.common.model.MemberResultVo;
import com.mzx.gulimall.im.config.GetHttpSessionConfiguration;
import com.mzx.gulimall.im.vo.MessageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
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
     * 该静态数据结构保存了在线的用户的信息.
     * <p>
     * 静态字段存在方法区内，在类加载的时候进行初始化.
     * Map里面不应该存储Session,那应该存储什么?
     * 存链表?可以.
     * <p>
     * 现在需要确定onLines要确保: 以下几个条件:
     * 1. 保存当前用户的ID,并且以当前用户的ID来保存Session和接受信息的用户的ID.
     */
    public static Map<Long, Node<Long, Session, Long>> onLinesUser = new ConcurrentHashMap<>();

    /**
     * 该静态字段保存了当前在线的所有的客服信息.
     */
    public static Map<Long, Node<Long, Session, Long>> onLinesServer = new ConcurrentHashMap<>();


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
        // session里面的值不是JSON,所以说不能使用fastJSON进行转换,只能强制转换.
        // 只要是代码执行到了这里的,那么session里面一定是登录的用户.
        // TODO: 客服从后台登录的聊天系统应该和前台共享一个session服务.
        // TODO: 要不然无法做到信息传输.
        // TODO: 并且后台在登录时候应该用MemberResultVo进行保存.
        MemberResultVo user = (MemberResultVo) httpSession.getAttribute("user");
        System.out.println("当前登录的用户是: " + user.toString());
        // 将所有用户全部添加到用户列表中,包括后台客服.
        Node<Long, Session, Long> node = new Node<>();
        node.setUserId(user.getId());
        node.setSession(session);
        node.setTo(-1L);
        // 添加.
        onLinesUser.put(user.getId(), node);

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

            // TODO: 传输数据判断的应该是发送给那位在线客服.
            // 但是由于做的是客服系统,所以说客服对用户来说是不固定的.
            // 从侧方面来说,用户给客服发送消息不需要指定接受人的ID. 而在第一次随机给了之后应该对该用户进行保存,方便为了以后继续发送消息.
            // 还有就是获取session不能以name获取而是应该以用户的ID进行获取.
            MessageVo messageVo = JSON.parseObject(message, MessageVo.class);
            // 应该根据是用户还是客服来进行判断比对.
            if (messageVo.isFlag()) {

                // 表示当前用户是客服.
                // 首先我们要明确的是,客服不会主动和谁进行聊天.

            } else {

                // TODO: 难啊.

            }


            if (messageVo != null && !StringUtils.isEmpty(messageVo.getUserID())) {

                // TODO: Map里面存储ChartEndpoint值纯属是浪费.
                // 获取锁的时候,
                Node<Long, Session, Long> node = onLinesUser.get(messageVo.getUserID());
                Session s = node.getSession();
                if (s != null) {

                    // 传过去应该是一个JSON.
                    String text = JSON.toJSONString(messageVo);
                    s.getBasicRemote().sendText(text);
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

    /**
     * @param <T>
     * @param <U>
     * @param <V>
     */
    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static class Node<T, U, V> {
        /**
         * 保存当前用户的用户id.
         */
        private T userId;
        /**
         * 保存当前用户的会话.
         */
        private U session;
        /**
         * 保存当前用户对应的是和那个客服(ID)在进行聊天.
         */
        private V to;


    }


}
