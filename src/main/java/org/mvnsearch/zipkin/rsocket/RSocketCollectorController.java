package org.mvnsearch.zipkin.rsocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

/**
 * zipkin rsocket collector
 *
 * @author linux_china
 */
@Controller
public class RSocketCollectorController {
    @MessageMapping("zipkin.rsocket.collector")
    public Mono<Void> collector(String body) {
        return Mono.empty();
    }
}
