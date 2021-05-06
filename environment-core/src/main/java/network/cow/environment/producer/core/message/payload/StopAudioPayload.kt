package network.cow.environment.producer.core.message.payload

import java.util.UUID

/**
 * @author Benedikt Wüller
 */
data class StopAudioPayload(val id: UUID, val duration: Int)
