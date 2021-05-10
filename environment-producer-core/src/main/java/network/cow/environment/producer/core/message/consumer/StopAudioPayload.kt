package network.cow.environment.producer.core.message.consumer

import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class StopAudioPayload(consumerId: UUID, val id: UUID, val duration: Int) : ConsumerBoundPayload(consumerId)
