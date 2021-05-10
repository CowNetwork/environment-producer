package network.cow.environment.producer.core.message

/**
 * @author Benedikt Wüller
 */
open class Message(val type: String, val payload: Payload) {
    val apiVersion: Int = 1
}
