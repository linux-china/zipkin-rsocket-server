Zipkin Server with RSocket Collector
====================================

Let Zipkin server to receive spans by RSocket Protocol.

# Why RSocket

* Faster: Async and NoAck(Fire and Forget)
* Easy to integrate with Reactive System

# How to start Zipkin Server with Docker

```yaml
version: "3"
services:
  zipkin:
    image: linuxchina/zipkin-rsocket-server
    ports:
      - "9411:9411"
      - "42252:42252"
```

# How to use it in Spring Boot

If you use "org.springframework.cloud:spring-cloud-starter-zipkin" and it's easy.

* Add following configuration in your application.properties

```
spring.autoconfigure.exclude=org.springframework.cloud.sleuth.zipkin2.ZipkinBackwardsCompatibilityAutoConfiguration
spring.zipkin.base-url=tcp://127.0.0.1:42252
spring.zipkin.encoder=proto3
spring.sleuth.sampler.rate=10
```

* Create RSocket and Zipkin Sender beans

```
    @Bean
    public RSocket rsocket(@Value("${spring.zipkin.base-url}") String zipkinBaseUrl) {
        return RSocketFactory
                .connect()
                .dataMimeType("application/vnd.google.protobuf")
                .transport(UriTransportRegistry.clientForUri(zipkinBaseUrl))
                .start()
                .block();
    }

    @Bean(ZipkinAutoConfiguration.SENDER_BEAN_NAME)
    public Sender zipkinRSocketSender(RSocket rsocket) {
        return new RSocketSender(rsocket);
    }
```

* Start your application

# References

* Distributed Tracing with Spring Cloud Sleuth and Spring Cloud Zipkin: https://dzone.com/articles/distributed-tracing-with-spring-cloud-sleuth-and-s