package network.cow.environment.producer.core.message.payload

import java.util.UUID

/**
 * @author Benedikt Wüller
 */
data class FadeAudioPayload(val id: UUID, val volume: Double, val duration: Int)
