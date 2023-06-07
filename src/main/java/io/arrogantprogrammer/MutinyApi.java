package io.arrogantprogrammer;

import io.quarkus.example.Greeter;
import io.quarkus.example.HelloReply;
import io.quarkus.example.HelloRequest;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Path("/hellomutiny")
public class MutinyApi {

    static final Logger LOGGER = LoggerFactory.getLogger(MutinyApi.class);

    @GrpcClient
    Greeter hello;

    @GET
    @Path("/{name}")
    public Multi<String> allGreetings(@PathParam("name") String name) {
        LOGGER.debug("allGreetings called with name: {}", name);
        return hello.sayHelloAFewTimes(HelloRequest.newBuilder()
                .setName(name)
                .build()).map(helloReply -> {
            return helloReply.getMessage();
        });
    }

    @GET
    public Uni<String> multiNames(@QueryParam("names") List<String> names) throws ExecutionException, InterruptedException {

        Multi<HelloRequest> requests = Multi.createFrom().items(
                names.stream().map(n -> {
                    return HelloRequest.newBuilder().setName(n).build();
        }));

//        String s = hello.sayHelloOnce(requests).map(helloReply -> {
//            System.out.println(helloReply.getMessage());
//            return helloReply.getMessage();
//        }).subscribe().toString();
//        System.out.println(s);
//        return Uni.createFrom().item(hello.sayHelloOnce(requests).map(helloReply -> {
//            System.out.println(helloReply.getMessage());
//            return helloReply.getMessage();
//        }).subscribeAsCompletionStage().get());

        return hello.sayHelloOnce(requests)
                .onItem().transform(HelloReply::getMessage);
    }

}
