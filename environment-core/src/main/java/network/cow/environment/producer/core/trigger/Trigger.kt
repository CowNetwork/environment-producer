package network.cow.environment.producer.core.trigger

import network.cow.environment.producer.core.source.AudioSource
import network.cow.environment.producer.core.trigger.condition.Condition
import network.cow.environment.producer.core.trigger.rate.RateProvider
import network.cow.environment.producer.core.trigger.rate.RateProviders
import network.cow.environment.producer.core.trigger.volume.VolumeProvider
import network.cow.environment.producer.core.trigger.volume.VolumeProviders

/**
 * @author Benedikt Wüller
 */
open class Trigger<ContextType : Any>(
        private val source: AudioSource<ContextType>,
        private val condition: Condition<ContextType>,
        private val fadeDuration: Int = 0,
        private val volumeProvider: VolumeProvider<ContextType> = VolumeProviders.maxVolume(),
        private val rateProvider: RateProvider<ContextType> = RateProviders.default()
) {

    open fun update(context: ContextType) {
        val isActive = this.condition.validate(context)

        val instance = this.source.getInstance(context)
        when {
            // If the audio is inactive and no instance exists, do nothing.
            !isActive && instance == null -> return
            // If the audio is inactive and the instance exists, stop and remove it.
            !isActive && instance != null -> this.invalidate(context)
            // If the audio is active and the instance exists, update rate and volume (if required).
            isActive && instance != null -> {
                val currentVolume = instance.volume
                val volume = this.volumeProvider.getVolume(context)
                if (currentVolume != volume) instance.volume = volume

                val currentRate = instance.rate
                val rate = this.rateProvider.getRate(context)
                if (currentRate != rate) instance.rate = rate
            }
            // If the audio is active and no instance exists, create and start one.
            isActive && instance == null -> {
                val newInstance = this.source.createInstance(context)
                val volume = this.volumeProvider.getVolume(context)
                newInstance.rate = this.rateProvider.getRate(context)

                if (this.fadeDuration > 0) {
                    // If a fade duration is set, start muted and fade in.
                    newInstance.volume = 0.0
                    newInstance.play()
                    newInstance.fadeTo(volume, this.fadeDuration)
                } else {
                    // Otherwise start with the requested volume.
                    newInstance.volume = volume
                    newInstance.play()
                }
            }
        }
    }

    open fun invalidate(context: ContextType) {
        this.source.getInstance(context)?.stop(this.fadeDuration)
        this.source.removeInstance(context)
    }

}
