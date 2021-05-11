package network.cow.environment.producer.spigot.source

import network.cow.environment.producer.core.source.GlobalAudioSource
import network.cow.environment.producer.spigot.Environment
import network.cow.environment.protocol.consumer.Sprite
import org.bukkit.entity.Player

/**
 * @author Benedikt Wüller
 */
class GlobalAudioSource(
        key: String,
        volume: Double = 1.0,
        loop: Boolean = false,
        loopFadeDuration: Int = 0,
        rate: Double = 1.0,
        section: Sprite = Sprite()
) : GlobalAudioSource<Player>(Environment, key, volume, loop, loopFadeDuration, rate, section)
