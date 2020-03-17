package com.duang.websocketdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebsocketConfig {
    //使用外部tomcat时需要注释一下方法
    @Bean
    ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
