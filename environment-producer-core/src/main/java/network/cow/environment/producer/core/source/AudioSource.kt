package network.cow.environment.producer.core.source

import network.cow.environment.producer.core.AudioEngine
import network.cow.environment.producer.core.AudioInstance
import network.cow.environment.producer.core.message.payload.FadeAudioPayload
import network.cow.environment.producer.core.message.payload.PlayAudioPayload
import network.cow.environment.producer.core.message.payload.Sprite
import network.cow.environment.producer.core.message.payload.StopAudioPayload
import network.cow.environment.producer.core.message.payload.UpdateAudioPayload
import java.util.UUID
import java.util.WeakHashMap
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * @author Benedikt Wüller
 */
abstract class AudioSource<ContextType : Any>(
        private val engine: AudioEngine<ContextType>,
        val key: String,
        volume: Double = 1.0,
        loop: Boolean = false,
        val loopFadeDuration: Int = 0,
        rate: Double = 1.0,
        val section: Sprite = Sprite()
) {

    protected val instances = WeakHashMap<ContextType, AudioInstance<*>>()

    var baseVolume: Double by Delegates.observable(volume, ::updateInstances)
    var baseRate: Double by Delegates.observable(rate, ::updateInstances)
    var loop: Boolean by Delegates.observable(loop, ::updateInstances)

    init {
        if (this.baseVolume < 0.0 || this.baseVolume > 1.0) throw IllegalArgumentException("The volume must be between 0.0 and 1.0.")
        if (this.baseRate < 0.5 || this.baseRate > 4.0) throw IllegalArgumentException("The rate must be between 0.5 and 4.0.")
    }

    fun addInstance(context: ContextType) : AudioInstance<ContextType> {
        val instance = AudioInstance(context, this)
        this.instances[context] = instance
        return instance
    }

    fun getInstance(context: ContextType) = this.instances[context]

    fun removeInstance(context: ContextType) {
        val instance = this.instances.remove(context) ?: return
        instance.stop()
    }

    fun removeInstance(instance: AudioInstance<ContextType>) = this.removeInstance(instance.context)

    fun play(context: ContextType, volume: Double = 1.0, rate: Double = 1.0, fadeDuration: Int = 0) {
        val instance = this.addInstance(context)
        instance.rate = rate
        if (fadeDuration > 0) {
            // If a fade duration is set, start muted and fade in.
            instance.volume = 0.0
            instance.play()
            instance.fadeTo(volume, fadeDuration)
        } else {
            // Otherwise start with the requested volume.
            instance.volume = volume
            instance.play()
        }
        this.removeInstance(context)
    }

    internal fun sendPlay(context: ContextType, id: UUID, volume: Double, rate: Double) {
        if (volume < 0 || volume > 1.0) throw IllegalArgumentException("The volume must be between 0.0 and 1.0.")
        val croppedRate = minOf(maxOf(rate * this.baseRate, 0.5), 4.0)
        val payload = this.createUpdateAudioPayload(id, volume, croppedRate)
        this.engine.playAudio(context, PlayAudioPayload(
                this.key, this.section,
                id, volume * this.baseVolume, croppedRate,
                payload.loop, payload.loopFadeDuration,
                payload.position, payload.pannerAttributes
        ))
    }

    internal fun sendUpdate(context: ContextType, id: UUID, volume: Double, rate: Double) {
        if (volume < 0 || volume > 1.0) throw IllegalArgumentException("The volume must be between 0.0 and 1.0.")
        val croppedRate = minOf(maxOf(rate * this.baseRate, 0.5), 4.0)
        val payload = this.createUpdateAudioPayload(id, volume, croppedRate)
        this.engine.updateAudio(context, payload)
    }

    internal fun sendFade(context: ContextType, id: UUID, volume: Double, duration: Int) {
        if (duration < 0) throw IllegalArgumentException("The duration must be equal to or greater than zero.")
        if (volume < 0 || volume > 1.0) throw IllegalArgumentException("The volume must be between 0.0 and 1.0.")
        val payload = FadeAudioPayload(id, volume * this.baseVolume, duration)
        this.engine.fadeAudio(context, payload)
    }

    internal fun sendStop(context: ContextType, id: UUID, duration: Int) {
        if (duration < 0) throw IllegalArgumentException("The duration must be equal to or greater than zero.")
        this.engine.stopAudio(context, StopAudioPayload(id, duration))
    }

    @Suppress("UNUSED_PARAMETER") // The unused parameters are required for method references to work.
    protected fun updateInstances(property: KProperty<*>, oldValue: Any, newValue: Any) {
        this.instances.values.forEach { it.update(property, oldValue, newValue) }
    }

    protected abstract fun createUpdateAudioPayload(id: UUID, volume: Double, rate: Double) : UpdateAudioPayload

}
