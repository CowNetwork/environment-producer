package network.cow.environment.producer.core

import java.util.UUID

/**
 * @author Benedikt Wüller
 */
data class AudioConsumer(val id: UUID, var state: State = State.UNREGISTERED) {

    lateinit var url: String

    enum class State {
        UNREGISTERED,
        REGISTERED,
        CONNECTED
    }

}
