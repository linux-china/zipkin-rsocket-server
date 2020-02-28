Zipkin RSocket Server
=====================

Zipkin Server with RSocket Collector

# Why RSocket

* Fast: Async and NoAck(Fire and Forget)
* Easy to integrate with Reactive System

# How to start Zipkin Server

It's Spring Boot 2.2.x App, and you have many ways to start Spring Boot App.

# How to send spans to Zipkin RSocket Collector

```
//create RSocket 
RSocket rsocket = RSocketFactory
                .connect()
                .transport(UriTransportRegistry.clientForUri("tcp://127.0.0.1:42252"))
                .start()
                .block();
//send spans
Flux.fromIterable(spans())
                .map(SpanBytesEncoder.PROTO3::encode)
                .map(DefaultPayload::create)
                .flatMap(payload -> rsocket.fireAndForget(payload))
                
```

