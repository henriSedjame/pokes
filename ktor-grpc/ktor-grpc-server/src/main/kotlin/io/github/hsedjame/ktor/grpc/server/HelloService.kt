package io.github.hsedjame.ktor.grpc.server

class HelloService : GreeterImplBase() {
    override suspend fun sayHello(request: HelloRequest): HelloResponse {
        return HelloResponse.newBuilder()
            .setMessage("Hello ${request.name}")
            .build()
    }
}