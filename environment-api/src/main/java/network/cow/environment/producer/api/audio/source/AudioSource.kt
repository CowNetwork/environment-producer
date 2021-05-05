package network.cow.environment.producer.api.audio.source

import network.cow.environment.producer.api.audio.AudioSection
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * @author Benedikt Wüller
 */
abstract class AudioSource(
        val key: String,
        volume: Double = 1.0,
        loop: Boolean = false,
        rate: Double = 1.0,
        val section: AudioSection = AudioSection()
) {

    var volume: Double by Delegates.observable(volume, ::onChange)
    var rate: Double by Delegates.observable(rate, ::onChange)
    var loop: Boolean by Delegates.observable(loop, ::onChange)

    @Suppress("UNUSED_PARAMETER") // The unused parameters are required for method references to work.
    protected open fun onChange(property: KProperty<*>, oldValue: Any, newValue: Any) {
        if (this.volume < 0.0 || this.volume > 1.0) throw IllegalArgumentException("The volume must be between 0.0 and 1.0.")
        if (this.rate < 0.5 || this.rate > 4.0) throw IllegalArgumentException("The rate must be between 0.5 and 4.0.")
    }

}
