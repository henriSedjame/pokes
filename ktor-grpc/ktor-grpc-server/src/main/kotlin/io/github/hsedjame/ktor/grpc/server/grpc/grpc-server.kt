package io.github.hsedjame.ktor.grpc.server.grpc

import com.typesafe.config.ConfigFactory
import io.grpc.BindableService
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.ServerServiceDefinition
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.util.network.*
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

const val banner = """
 ___  ____    _________     ___     _______                ______    _______      _______      ______  
|_  ||_  _|  |  _   _  |  .'   `.  |_   __ \             .' ___  |  |_   __ \    |_   __ \   .' ___  | 
  | |_/ /    |_/ | | \_| /  .-.  \   | |__) |           / .'   \_|    | |__) |     | |__) | / .'   \_| 
  |  __'.        | |     | |   | |   |  __ /            | |   ____    |  __ /      |  ___/  | |        
 _| |  \ \_     _| |_    \  `-'  /  _| |  \ \_     _    \ `.___]  |  _| |  \ \_   _| |_     \ `.___.'\ 
|____||____|   |_____|    `.___.'  |____| |___|   (_)    `._____.'  |____| |___| |_____|     `.____ .' 

"""


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

        loadConfiguration()

        val startDuration = measureTimeMillis {
            server = ServerBuilder
                .forPort(configuration.port)
                .apply(configuration.serverConfigurer)
                .build()

            server!!.start()
        }

        println(banner.trimIndent())

        server!!.listenSockets.forEach {
            environment.log.info("Grpc server listening on port ${it.port}")
        }

        environment.log.info("Grpc server started in $startDuration milliseconds.")

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

    private fun loadConfiguration() {

        val config = HoconApplicationConfig(ConfigFactory.load())
        configuration.port = getStringConfig(config, "grpc.port", "6565").toInt()

        val services = getListConfig(config, "grpc.services")

        configuration.serverConfigurer = {
            services.forEach {
                when (val service = Class.forName(it).getConstructor().newInstance()) {
                    is BindableService -> this.addService(service)
                    is ServerServiceDefinition -> this.addService(service)
                    else -> throw Error("'$it' is neither BindableService nor BindableService")
                }
            }
        }
    }
}

fun  getStringConfig(config: ApplicationConfig, key: String, defaultValue: String?= null, errorMessage: String? = null) : String =
    config.propertyOrNull(key)?.getString() ?: System.getenv(key) ?: defaultValue ?: throw Error(errorMessage)

fun getListConfig(config: ApplicationConfig, key: String, defaultValue: List<String>? = listOf(), errorMessage: String? = null) : List<String> =
    config.propertyOrNull(key)?.getList() ?: System.getenv(key)?.split(",")?.map { it.trim() } ?: defaultValue ?: throw Error(errorMessage)

