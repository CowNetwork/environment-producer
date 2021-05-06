package network.cow.environment.producer.core

import network.cow.environment.producer.core.message.payload.FadeAudioPayload
import network.cow.environment.producer.core.message.payload.PlayAudioPayload
import network.cow.environment.producer.core.message.payload.SetPositionPayload
import network.cow.environment.producer.core.message.payload.StopAudioPayload
import network.cow.environment.producer.core.message.payload.UpdateAudioPayload
import network.cow.environment.producer.core.source.AudioSource
import network.cow.environment.producer.core.trigger.Trigger
import network.cow.environment.producer.core.trigger.condition.Condition
import network.cow.environment.producer.core.trigger.rate.RateProvider
import network.cow.environment.producer.core.trigger.rate.RateProviders
import network.cow.environment.producer.core.trigger.volume.VolumeProvider
import network.cow.environment.producer.core.trigger.volume.VolumeProviders
import java.util.UUID
import java.util.WeakHashMap

/**
 * @author Benedikt Wüller
 */
abstract class AudioEngine<ContextType : Any> {

    private val consumers = WeakHashMap<ContextType, AudioConsumer>()
    private val triggers = mutableSetOf<Trigger<ContextType>>()

    protected fun update() {
        this.consumers.filter {
            it.value.isConnected
        }.keys.forEach { context ->
            // Update consumer positions.
            this.setPosition(context, SetPositionPayload(this.getPosition(context)))

            // Update triggers.
            this.triggers.forEach { trigger -> trigger.update(context) }
        }
    }

    protected fun addConsumer(context: ContextType, callback: (String) -> Unit) {
        // TODO: set isConnected = true only when browser/consumer is connected
        this.consumers[context] = AudioConsumer(UUID.randomUUID(), true)
        // TODO: send consumer registered request and call callback with url
        callback("https://environment.cow.network/TODO")
    }

    protected fun removeConsumer(context: ContextType) {
        this.triggers.forEach { it.invalidate(context) }
        this.consumers.remove(context)
    }

    fun addTrigger(trigger: Trigger<ContextType>) {
        this.triggers.add(trigger)
    }

    fun addTrigger(source: AudioSource<ContextType>, condition: Condition<ContextType>, fadeDuration: Int = 0,
                   volumeProvider: VolumeProvider<ContextType> = VolumeProviders.maxVolume(),
                   rateProvider: RateProvider<ContextType> = RateProviders.default(),
                   fireAndForget: Boolean = false
    ) : Trigger<ContextType> {
        val trigger = Trigger(source, condition, fadeDuration, volumeProvider, rateProvider, fireAndForget)
        this.addTrigger(trigger)
        return trigger
    }

    fun removeTrigger(trigger: Trigger<ContextType>) {
        this.triggers.remove(trigger)
        this.consumers.keys.forEach(trigger::invalidate)
    }

    private fun setPosition(context: ContextType, payload: SetPositionPayload) = this.send(context, payload)

    fun playAudio(context: ContextType, payload: PlayAudioPayload) = this.send(context, payload)
    fun updateAudio(context: ContextType, payload: UpdateAudioPayload) = this.send(context, payload)
    fun fadeAudio(context: ContextType, payload: FadeAudioPayload) = this.send(context, payload)
    fun stopAudio(context: ContextType, payload: StopAudioPayload) = this.send(context, payload)

    protected abstract fun send(context: ContextType, payload: Any)

    protected abstract fun getPosition(context: ContextType) : Point3D

}
