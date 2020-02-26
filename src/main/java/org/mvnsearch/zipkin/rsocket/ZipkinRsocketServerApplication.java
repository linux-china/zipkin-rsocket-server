package org.mvnsearch.zipkin.rsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
public class ZipkinRsocketServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinRsocketServerApplication.class, args);
    }

}
