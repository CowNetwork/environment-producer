package network.cow.environment.producer.core.message.consumer

import network.cow.environment.producer.core.Point3D
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class PlayAudioPayload(
        consumerId: UUID,
        val key: String,
        val sprite: Sprite,
        id: UUID,
        volume: Double,
        rate: Double,
        loop: Boolean,
        loopFadeDuration: Int,
        position: Point3D? = null,
        pannerAttributes: PannerAttributes? = null
) : UpdateAudioPayload(consumerId, id, volume, rate, loop, loopFadeDuration, position, pannerAttributes)

data class Sprite(val from: Int = 0, val to: Int = Int.MAX_VALUE)
