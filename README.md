Zipkin Server with RSocket Collector
====================================

Let Zipkin to receive spans by RSocket Protocol.

# Why RSocket

* Faster: Async and NoAck(Fire and Forget)
* Easy to integrate with Reactive System

# How to start Zipkin Server

It's Spring Boot 2.2.x App, and you have many ways to start Spring Boot App.

# How to send spans to Zipkin RSocket Collector

* If you use Zipkin Brave, and you can use Zipkin Sender RSocket https://github.com/linux-china/zipkin-sender-rsocket

* RSocket raw API like following?
* 
```
//create RSocket 
RSocket rsocket = RSocketFactory
                .connect()
                .dataMimeType("application/vnd.google.protobuf")
                .transport(UriTransportRegistry.clientForUri("tcp://127.0.0.1:42252"))
                .start()
                .block();
//send spans
Flux.fromIterable(spans())
                .map(SpanBytesEncoder.PROTO3::encode)
                .map(DefaultPayload::create)
                .flatMap(payload -> rsocket.fireAndForget(payload))
                
```


# References

* Distributed Tracing with Spring Cloud Sleuth and Spring Cloud Zipkin: https://dzone.com/articles/distributed-tracing-with-spring-cloud-sleuth-and-s