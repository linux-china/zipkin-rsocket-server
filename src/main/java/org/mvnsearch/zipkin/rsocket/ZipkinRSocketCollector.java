package org.mvnsearch.zipkin.rsocket;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.exceptions.ApplicationErrorException;
import io.rsocket.exceptions.InvalidException;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import zipkin2.Callback;
import zipkin2.codec.SpanBytesDecoder;
import zipkin2.collector.Collector;
import zipkin2.collector.CollectorMetrics;
import zipkin2.collector.CollectorSampler;
import zipkin2.storage.StorageComponent;

/**
 * Zipkin RSocket Collector
 *
 * @author linux_china
 */
@Component
public class ZipkinRSocketCollector extends AbstractRSocket {
    private Collector collector;
    private CollectorMetrics metrics;
    private final ConcurrentTaskExecutor concurrentTaskExecutor = new ConcurrentTaskExecutor();

    public ZipkinRSocketCollector(StorageComponent storage,
                                  CollectorSampler sampler,
                                  CollectorMetrics metrics) {
        this.metrics = metrics;
        this.collector = Collector.newBuilder(getClass())
                .storage(storage)
                .sampler(sampler)
                .metrics(metrics.forTransport("rsocket"))
                .build();
    }

    @Override
    public Mono<Void> fireAndForget(final Payload payload) {
        metrics.incrementMessages();
        metrics.incrementBytes(payload.data().readableBytes());
        if (!payload.data().isReadable()) {
            return Mono.error(new InvalidException("Payload data is empty"));
        }

        return Mono.create(sink -> {
            try {
                Callback<Void> result = new Callback<Void>() {
                    @Override
                    public void onSuccess(Void value) {
                        sink.success();
                    }

                    @Override
                    public void onError(Throwable t) {
                        sink.error(new ApplicationErrorException("Failed to save span", t));
                    }
                };
                collector.acceptSpans(payload.data().nioBuffer(), SpanBytesDecoder.PROTO3, result, concurrentTaskExecutor);
            } catch (Exception e) {
                sink.error(new ApplicationErrorException("Failed to save span", e));
            }
        });
    }

}
