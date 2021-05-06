package network.cow.environment.producer.spigot.trigger

import network.cow.environment.producer.core.source.AudioSource
import network.cow.environment.producer.core.trigger.Trigger
import network.cow.environment.producer.core.trigger.condition.Condition
import network.cow.environment.producer.core.trigger.rate.RateProvider
import network.cow.environment.producer.core.trigger.rate.RateProviders
import network.cow.environment.producer.core.trigger.volume.VolumeProvider
import network.cow.environment.producer.core.trigger.volume.VolumeProviders
import org.bukkit.entity.Player

/**
 * @author Benedikt Wüller
 */
class Trigger(
        source: AudioSource<Player>,
        condition: Condition<Player>,
        fadeDuration: Int = 0,
        volumeProvider: VolumeProvider<Player> = VolumeProviders.maxVolume(),
        rateProvider: RateProvider<Player> = RateProviders.default(),
        fireAndForget: Boolean = false,
) : Trigger<Player>(source, condition, fadeDuration, volumeProvider, rateProvider, fireAndForget)
