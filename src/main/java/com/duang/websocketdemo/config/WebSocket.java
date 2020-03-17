package com.duang.websocketdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@ServerEndpoint(value="/websocket/{sessionId}")
public class WebSocket {
    private static Map<String, Session> sessionPool = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(WebSocket.class);
    @OnOpen
    public void onOpen(Session session, @PathParam("sessionId") String sessionId){
        sessionPool.put(sessionId,session);
        logger.info("【WEBSOCKET消息】 "+sessionId+" 加入连接，当前连接数:"+sessionPool.size());
    }

    @OnClose
    public void opClose(@PathParam("sessionId") String sessionId){
        sessionPool.remove(sessionId);
        logger.info("【WEBSOCKET消息】 "+sessionId+" 已下线,当前连接数:"+sessionPool.size());
    }

    public void onMessage(String message){
        logger.info("【WEBSOCKET消息】 收到客户端消息:"+message);
    }

    //广播推送消息
    public void sendAllMessage(String message){
        Iterator<Session> iterator = sessionPool.values().iterator();
        while(iterator.hasNext()){
            try{
                iterator.next().getAsyncRemote().sendText(message);
            }catch(Exception e){
                logger.error("【WEBSOCKET消息】广播推送消息失败:"+message,e);
            }
        }
    }
    //单点推送消息
    public void sendSingleMessage(String sessionId,String message){
        Session session = sessionPool.get(sessionId);
        if(session != null){
            try{
                session.getAsyncRemote().sendText(message);
            }catch(Exception e){
                logger.error("【WEBSCOKET消息】 单播消息至"+sessionId+":"+message+"失败");
            }
        }
    }


}
