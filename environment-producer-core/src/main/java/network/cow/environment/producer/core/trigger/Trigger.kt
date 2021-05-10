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
        val triggerOnce: Boolean = false,
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
            !isActive && wasActive -> {
                if (this.triggerOnce && !this.fireAndForget) { // Only fade out if the trigger is single use and may still be used in the future.
                    val instance = this.source.getInstance(context) ?: return
                    instance.fadeTo(0.0, this.fadeDuration)
                } else if (!this.triggerOnce) { // Remove only if this trigger is not single use.
                    this.invalidate(context)
                }
            }
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
                val instance = if (this.triggerOnce) this.source.getInstance(context) else null

                // If the trigger is single use, fire and forget and an audio instance has already been created, nothing should be done.
                if (instance != null && this.triggerOnce && this.fireAndForget) return

                val newInstance = instance ?: this.source.addInstance(context)
                val volume = this.volumeProvider.getVolume(context)

                newInstance.suppressUpdate {
                    newInstance.rate = this@Trigger.rateProvider.getRate(context)
                    newInstance.volume = 0.0
                }

                newInstance.play()
                newInstance.fadeTo(volume, this.fadeDuration)
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
