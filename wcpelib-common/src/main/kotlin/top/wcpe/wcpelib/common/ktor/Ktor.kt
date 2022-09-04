package top.wcpe.wcpelib.common.ktor

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.function.Consumer

/**
 * 由 WCPE 在 2022/4/26 22:57 创建
 *
 * Created by WCPE on 2022/4/26 22:57
 *
 * GitHub  : https://github.com/wcpe
 * QQ      : 1837019522
 * @author : WCPE
 * @since  : v1.0.11-alpha-dev-2
 */
class Ktor(val port: Int) {

    private val ktorServerMap = mutableMapOf<String, Pair<NettyApplicationEngine, Job>>()

    fun createKtorServer(key: String, consumer: Consumer<Application>): Pair<NettyApplicationEngine, Job> {
        var pair = ktorServerMap[key]
        if (pair != null) {
            return pair
        }
        val ktorServer = embeddedServer(Netty, port) {
            consumer.accept(this)
        }
        pair = ktorServer to GlobalScope.launch { ktorServer.start(true) }
        ktorServerMap[key] = pair
        return pair
    }

    fun removeKtorServer(key: String) {
        ktorServerMap.remove(key)?.let {
            it.first.application.cancel()
            it.second.cancel()
        }
    }

    fun closeAllServer() {
        for ((_, value) in ktorServerMap) {
            value.first.application.cancel()
            value.second.cancel()
        }
        ktorServerMap.clear()
    }
}