package network.cow.environment.producer.core

import java.util.UUID

/**
 * @author Benedikt Wüller
 */
data class AudioConsumer(val contextId: UUID, var state: State = State.UNREGISTERED, val callback: (String) -> Unit) {

    lateinit var id: UUID
    lateinit var url: String

    enum class State {
        UNREGISTERED,
        REGISTERED,
        CONNECTED
    }

}
