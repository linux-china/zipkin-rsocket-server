package org.mvnsearch.zipkin.rsocket;

import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import zipkin2.Span;
import zipkin2.codec.SpanBytesEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * zipkin spans sender test by RSocket
 *
 * @author linux_china
 */
public class ZipkinSpansSenderTest implements RSocket {
    private static RSocket rsocket;

    @BeforeAll
    public static void setUp() throws Exception {
        rsocket = RSocketConnector.create()
                .dataMimeType("application/vnd.google.protobuf")
                .connect(TcpClientTransport.create("127.0.0.1", 42252))
                .block();
    }

    @AfterAll
    public static void tearDown() {
        rsocket.dispose();
    }

    @Test
    public void testSendSpans() throws Exception {
        Flux.fromIterable(spans())
                .map(SpanBytesEncoder.PROTO3::encode)
                .map(DefaultPayload::create)
                .flatMap(payload -> rsocket.fireAndForget(payload))
                .then()
                .block();
    }


    public List<Span> spans() {
        Span spanRequester = Span.newBuilder().traceId(1, 1).id(1L).build();
        Span spanBroker = Span.newBuilder().traceId(1, 1).id(2L).parentId(1).build();
        Span spanResponder = Span.newBuilder().traceId(1, 1).id(3L).parentId(1).build();
        return Arrays.asList(spanRequester, spanBroker, spanResponder);
    }

}
