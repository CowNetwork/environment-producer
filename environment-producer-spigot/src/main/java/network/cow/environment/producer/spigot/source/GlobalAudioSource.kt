package network.cow.environment.producer.spigot.source

import network.cow.environment.producer.core.message.payload.Sprite
import network.cow.environment.producer.core.source.GlobalAudioSource
import network.cow.environment.producer.spigot.SpigotAudioEngine
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
) : GlobalAudioSource<Player>(SpigotAudioEngine, key, volume, loop, loopFadeDuration, rate, section)
