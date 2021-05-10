package network.cow.environment.producer.core.message.consumer

import network.cow.environment.producer.core.message.Payload
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
abstract class ProducerBoundPayload(consumerId: UUID) : Payload
