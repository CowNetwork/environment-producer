package network.cow.environment.producer.api.payload

import network.cow.environment.producer.api.Position
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class PlayAudioPayload(
        val key: String,
        val sprite: Sprite,
        id: UUID,
        volume: Double,
        rate: Double,
        loop: Boolean,
        loopFadeDuration: Int,
        position: Position? = null,
        pannerAttributes: PannerAttributes? = null
) : UpdateAudioPayload(id, volume, rate, loop, loopFadeDuration, position, pannerAttributes)

data class Sprite(val from: Int = 0, val to: Int = Int.MAX_VALUE)
