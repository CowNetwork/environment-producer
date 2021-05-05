package network.cow.environment.producer.api.payload

import java.util.UUID

/**
 * @author Benedikt Wüller
 */
data class StopAudioPayload(val id: UUID, val duration: Int)
