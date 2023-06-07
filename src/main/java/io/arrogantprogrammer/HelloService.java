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

        return helloRequest.onItem().transform(item -> {
            return item.getName();
        }).collect().asList().map(names -> {
            return new StringBuilder("Hello, ")
                    .append(
                    String.join(", ", names))
                    .append("!").toString();
        }).onItem().transform(result -> {
            return HelloReply.newBuilder().setMessage(result).build();
        });

    }

}
