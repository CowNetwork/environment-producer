package network.cow.environment.producer.core

import java.util.UUID

/**
 * @author Benedikt Wüller
 */
data class AudioConsumer(val id: UUID, var isConnected: Boolean = false) {
    lateinit var url: String
}
