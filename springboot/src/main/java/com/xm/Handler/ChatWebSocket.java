package com.xm.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xm.entity.Message;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@ServerEndpoint("/ws/{userId}")
@Component
public class ChatWebSocket {

    // 存储所有连接的用户
    private static final ConcurrentHashMap<Integer, Session> sessions = new ConcurrentHashMap<>();

    private Integer userId;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        this.userId = userId;
        sessions.put(userId, session);
        System.out.println("用户 " + userId + " 建立连接");
    }

    @OnClose
    public void onClose() {
        sessions.remove(userId);
        System.out.println("用户 " + userId + " 断开连接");
    }

    @OnMessage
    public void onMessage(String messageJson, Session session) throws IOException {
        // 解析消息
        Message msg = new ObjectMapper().readValue(messageJson, Message.class);
        System.out.println("收到消息: " + msg);

        // 转发消息给目标用户
        Session receiverSession = sessions.get(msg.getReceiveId());
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.getBasicRemote().sendText(messageJson);
        }


    }

}
