package io.github.hsedjame.ktor.grpc.server.grpc

import io.grpc.Server
import io.grpc.ServerBuilder
import io.ktor.application.*
import io.ktor.server.engine.*
import java.util.concurrent.TimeUnit

object GrpcServer : ApplicationEngineFactory<GrpcApplicationEngine, GrpcApplicationEngine.Configuration> {
    override fun create(environment: ApplicationEngineEnvironment, configure: GrpcApplicationEngine.Configuration.() -> Unit): GrpcApplicationEngine {
        return GrpcApplicationEngine(environment, configure)
    }
}

@OptIn(EngineAPI::class)
class GrpcApplicationEngine(
    environment: ApplicationEngineEnvironment,
    configure: Configuration.() -> Unit = {}
) : BaseApplicationEngine(environment) {

    private val configuration = Configuration().apply(configure)
    private var server: Server? = null

    class Configuration : BaseApplicationEngine.Configuration() {
        var port: Int = 6565
        var serverConfigurer: ServerBuilder<*>.() -> Unit = {}
    }

    override fun start(wait: Boolean): ApplicationEngine {
        server = ServerBuilder
            .forPort(configuration.port)
            .apply(configuration.serverConfigurer)
            .build()

        server!!.start()

        if (wait) {
            server!!.awaitTermination()
        }

        return this
    }

    override fun stop(gracePeriodMillis: Long, timeoutMillis: Long) {
        environment.monitor.raise(ApplicationStopPreparing, environment)
        server?.awaitTermination(gracePeriodMillis, TimeUnit.MILLISECONDS)
        server?.shutdownNow()
    }
}
