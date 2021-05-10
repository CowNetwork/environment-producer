package network.cow.environment.producer.core.message.service

import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class ConsumerDisconnectedPayload(val consumerId: UUID) : ProducerBoundPayload
