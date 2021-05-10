package network.cow.environment.producer.core.message.service

import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class ConsumerRegisteredPayload(val contextId: UUID, val consumerId: UUID, val consumerUrl: String) : ProducerBoundPayload
