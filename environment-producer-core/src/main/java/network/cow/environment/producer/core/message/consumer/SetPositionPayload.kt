package network.cow.environment.producer.core.message.consumer

import network.cow.environment.producer.core.Point3D
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class SetPositionPayload(consumerId: UUID, val position: Point3D) : ConsumerBoundPayload(consumerId)
