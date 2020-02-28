package org.mvnsearch.zipkin.rsocket;

import io.rsocket.RSocketFactory;
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
        return
                RSocketFactory.receive()
                        .acceptor(acceptor)
                        .transport(TcpServerTransport.create("localhost", 42252))
                        .start()
                        .block();
    }
}
