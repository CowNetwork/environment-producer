package network.cow.environment.producer.core

import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.websocket.ClientWebSocketSession
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocketSession
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import network.cow.environment.protocol.Message
import network.cow.environment.protocol.Payload
import network.cow.environment.protocol.PayloadRegistry

/**
 * @author Benedikt Wüller
 */
class WebSocketClient {

    var onOpen: () -> Unit = {}
    var onClose: () -> Unit = {}
    var onMessage: (Payload) -> Unit = {}

    private var session: ClientWebSocketSession? = null

    private val client = HttpClient(CIO) {
        install(WebSockets)
        install(Auth) {
            basic {
                username = "environment" // TODO
                password = "environment" // TODO
            }
        }
    }

    fun connect() {
        GlobalScope.launch {
            this@WebSocketClient.session?.close()

            val session = client.webSocketSession(
                    method = HttpMethod.Get,
                    host = "localhost", // TODO
                    port = 35721, // TODO
                    path = "/producers" // TODO
            )

            this@WebSocketClient.session = session
            this@WebSocketClient.onOpen()

            try {
                for (frame in session.incoming) {
                    val payload = this@WebSocketClient.parseFrame(frame) ?: continue
                    this@WebSocketClient.onMessage(payload)
                }
            } finally {
                this@WebSocketClient.session = null
                this@WebSocketClient.onClose()
            }
        }
    }

    fun disconnect() {
        GlobalScope.launch {
            this@WebSocketClient.session?.close()
        }
    }

    fun send(payload: Payload) {
        GlobalScope.launch {
            val message = Message(payload.type, payload)
            this@WebSocketClient.session?.send(JsonService.toJson(message))
        }
    }

    private fun parseFrame(frame: Frame) : Payload? {
        if (frame !is Frame.Text) return null
        return try {
            val text = frame.readText()
            val json = JsonService.fromJson(text, JsonObject::class.java)
            val type = json["type"].asString
            PayloadRegistry.parsePayload(type, JsonService.toJson(json["payload"]))
        } catch (e: JsonSyntaxException) {
            return null
        }
    }

}
