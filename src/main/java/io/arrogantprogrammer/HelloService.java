package io.arrogantprogrammer;

import io.quarkus.example.Greeter;
import io.quarkus.example.HelloReply;
import io.quarkus.example.HelloRequest;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.util.Arrays;
import java.util.List;

@GrpcService
public class HelloService implements Greeter {

    List<String> pre = Arrays.asList(
            "Hello, ",
            "Hi there, ",
            "Bonjour, ",
            "Hallo, "
    );

    List<String> post = Arrays.asList(
            " you cotton headed ninny muggins.",
            " your skin tone is fabulous!",
            " your wit and wisdom are apparent!",
            " you are clearly a master of hydration!");

    @Override
    public Uni<HelloReply> sayHello(HelloRequest request) {
        return Uni.createFrom().item(() -> {
            return HelloReply.newBuilder().setMessage("Hello, " + request.getName()).build();
        });
    }

    @Override
    public Multi<HelloReply> sayHelloAFewTimes(HelloRequest request) {
        List<HelloReply> replies = Arrays.asList(
                HelloReply.newBuilder().setMessage(pre.get(0) + request.getName() + post.get(0)).build(),
                HelloReply.newBuilder().setMessage(pre.get(1) + request.getName() + post.get(1)).build(),
                HelloReply.newBuilder().setMessage(pre.get(2) + request.getName() + post.get(2)).build()
        );
        return Multi.createFrom().iterable(replies);
    }

    @Override
    public Uni<HelloReply> sayHelloOnce(Multi<HelloRequest> helloRequest) {

/*
        return Uni.createFrom().item(
                HelloReply.newBuilder().setMessage(
                    helloRequest.map(helloRequest1 -> {
                        return helloRequest1.getName();
                    }).collect().asList().onItem().transform(names -> {
                        return String.join(", ", names);
                    }).toString()
        ).build());
*/            String s = helloRequest
                .map(HelloRequest::getName)
                .collect()
                .asList()
                .map(strings -> {
                    return String.join(", ", strings);
                }).toString();
        HelloReply hr = HelloReply.newBuilder().setMessage(s).build();
        return Uni.createFrom().item(hr);

    }

}
