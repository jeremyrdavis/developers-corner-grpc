package io.arrogantprogrammer;

import io.quarkus.example.Greeter;
import io.quarkus.example.HelloRequest;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
