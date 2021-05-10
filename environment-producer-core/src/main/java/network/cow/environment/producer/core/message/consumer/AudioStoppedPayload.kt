package network.cow.environment.producer.core.message.consumer

import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class AudioStoppedPayload(consumerId: UUID, val id: UUID) : ProducerBoundPayload(consumerId)
