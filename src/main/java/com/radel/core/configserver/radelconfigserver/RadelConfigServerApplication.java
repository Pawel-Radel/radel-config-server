package com.radel.core.configserver.radelconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class RadelConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RadelConfigServerApplication.class, args);
    }

}
