Zipkin RSocket Server
=====================

Zipkin Server with RSocket Collector

# Why RSocket

* Fast: Async and NoAck(Fire and Forget)
* Easy to integrate with Reactive System

# How to send spans to Zipkin RSocket Collector

```
//create RSocket 
RSocket rsocket = RSocketFactory
                .connect()
                .transport(UriTransportRegistry.clientForUri("ws://127.0.0.1:8080/zpkin_rsocket_collector"))
                .start()
                .block();
//send spans
Flux.fromIterable(spans())
                .map(SpanBytesEncoder.PROTO3::encode)
                .map(DefaultPayload::create)
                .flatMap(payload -> rsocket.fireAndForget(payload))
                
```

