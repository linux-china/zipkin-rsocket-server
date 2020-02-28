package org.mvnsearch.zipkin.rsocket;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Zipkin RSocket acceptor
 *
 * @author linux_china
 */
@Component
@Primary
public class ZipkinRSocketAcceptor implements SocketAcceptor {
    @Autowired
    private ZipkinRSocketCollector collectorRSocketHandler;

    @Override
    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
        return Mono.just(collectorRSocketHandler);
    }
}
