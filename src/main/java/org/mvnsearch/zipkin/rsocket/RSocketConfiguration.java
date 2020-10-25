package org.mvnsearch.zipkin.rsocket;

import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RSocket Configuration
 *
 * @author linux_china
 */
@Configuration
public class RSocketConfiguration {

    @Bean(destroyMethod = "dispose")
    public CloseableChannel rsocketServer(ZipkinRSocketAcceptor acceptor) {
        return RSocketServer.create(acceptor)
                .bind(TcpServerTransport.create("0.0.0.0", 42252))
                .block();
    }
}
