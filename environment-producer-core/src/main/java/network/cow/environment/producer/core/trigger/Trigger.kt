package network.cow.environment.producer.core.trigger

import network.cow.environment.producer.core.source.AudioSource
import network.cow.environment.producer.core.trigger.condition.Condition
import network.cow.environment.producer.core.trigger.rate.RateProvider
import network.cow.environment.producer.core.trigger.rate.RateProviders
import network.cow.environment.producer.core.trigger.volume.VolumeProvider
import network.cow.environment.producer.core.trigger.volume.VolumeProviders
import java.util.WeakHashMap

/**
 * @author Benedikt Wüller
 */
open class Trigger<ContextType : Any>(
        val source: AudioSource<ContextType>,
        val condition: Condition<ContextType>,
        val fadeDuration: Int = 0,
        val volumeProvider: VolumeProvider<ContextType> = VolumeProviders.maxVolume(),
        val rateProvider: RateProvider<ContextType> = RateProviders.default(),
        val fireAndForget: Boolean = false
) {

    private val states = WeakHashMap<ContextType, Boolean>()

    open fun update(context: ContextType) {
        val isActive = this.condition.validate(context)
        val wasActive = this.states[context] ?: false

        if (isActive) {
            this.states[context] = true
        } else {
            this.states.remove(context)
        }

        when {
            // If the audio is inactive and no instance exists, do nothing.
            !isActive && !wasActive -> return
            // If the audio is inactive and the instance exists, stop and remove it.
            !isActive && wasActive && !this.fireAndForget -> this.invalidate(context)
            // If the audio is active and the instance exists, update rate and volume (if required).
            isActive && wasActive && !this.fireAndForget -> {
                val instance = this.source.getInstance(context) ?: return

                val currentVolume = instance.volume
                val volume = this.volumeProvider.getVolume(context)
                if (currentVolume != volume) instance.volume = volume

                val currentRate = instance.rate
                val rate = this.rateProvider.getRate(context)
                if (currentRate != rate) instance.rate = rate
            }
            // If the audio is active and no instance exists, create and start one.
            isActive && !wasActive -> {
                val newInstance = this.source.addInstance(context)
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

                if (this.fireAndForget) {
                    this.source.removeInstance(context)
                }
            }
        }
    }

    open fun invalidate(context: ContextType) {
        if (!this.fireAndForget) {
            // Only stop the sound if fire and forget is off.
            this.source.getInstance(context)?.stop(this.fadeDuration)
        }
        this.source.removeInstance(context)
        this.states.remove(context)
    }

}
