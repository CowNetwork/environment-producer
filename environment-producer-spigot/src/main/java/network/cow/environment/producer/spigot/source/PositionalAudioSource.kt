package network.cow.environment.producer.spigot.source

import network.cow.environment.producer.core.source.DistanceModel
import network.cow.environment.producer.core.source.PositionalAudioSource
import network.cow.environment.producer.spigot.Environment
import network.cow.environment.producer.spigot.toPoint
import network.cow.environment.protocol.consumer.Sprite
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * @author Benedikt Wüller
 */
class PositionalAudioSource(
        key: String,
        position: Vector,
        maxVolumeRadius: Double,
        rollOffFactor: Double = DEFAULT_ROLL_OFF_FACTOR,
        distanceModel: DistanceModel = DistanceModel.EXPONENTIAL,
        volume: Double = 1.0,
        loop: Boolean = false,
        loopFadeDuration: Int = 0,
        rate: Double = 1.0,
        section: Sprite = Sprite()
) : PositionalAudioSource<Player>(Environment, key, position.toPoint(), maxVolumeRadius, rollOffFactor, distanceModel, volume, loop, loopFadeDuration, rate, section)
