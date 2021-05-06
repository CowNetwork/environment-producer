package network.cow.environment.producer.core.message.payload

import network.cow.environment.producer.core.Point3D
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
open class UpdateAudioPayload(
        val id: UUID,
        val volume: Double,
        val rate: Double,
        val loop: Boolean,
        val loopFadeDuration: Int,
        val position: Point3D? = null,
        val pannerAttributes: PannerAttributes? = null
)

data class PannerAttributes(val distanceModel: String, val referenceDistance: Double, val rollOffFactor: Double)
