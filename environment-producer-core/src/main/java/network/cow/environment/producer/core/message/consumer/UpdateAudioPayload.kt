package network.cow.environment.producer.core.message.consumer

import network.cow.environment.producer.core.Point3D
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
open class UpdateAudioPayload(
        consumerId: UUID,
        val id: UUID,
        val volume: Double,
        val rate: Double,
        val loop: Boolean,
        val loopFadeDuration: Int,
        val position: Point3D? = null,
        val pannerAttributes: PannerAttributes? = null
) : ConsumerBoundPayload(consumerId)

data class PannerAttributes(val distanceModel: String, val referenceDistance: Double, val rollOffFactor: Double)
