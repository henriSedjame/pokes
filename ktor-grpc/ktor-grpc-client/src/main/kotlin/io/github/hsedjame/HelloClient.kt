package io.github.hsedjame

import io.github.hsedjame.ktor.grpc.server.GreeterGrpc
import io.github.hsedjame.ktor.grpc.server.HelloRequest
import io.github.hsedjame.ktor.grpc.server.HelloResponse
import io.grpc.stub.StreamObserver

class HelloClient(private val stub: GreeterGrpc.GreeterStub) {

     fun greet(name: String) {
        val request = HelloRequest.newBuilder().setName(name).build()

        val helloReponseObserver = HelloReponseObserver()
        stub.sayHello(request, helloReponseObserver)

    }
}

class HelloReponseObserver : StreamObserver<HelloResponse> {

    override fun onNext(resp: HelloResponse?) {
        println(resp?.message)
    }

    override fun onError(p0: Throwable?) {
        println("Oups it fails")
    }

    override fun onCompleted() {
        println("Completed ...")
    }

}