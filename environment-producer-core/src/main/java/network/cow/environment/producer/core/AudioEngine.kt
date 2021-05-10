package network.cow.environment.producer.core

import com.google.gson.GsonBuilder
import network.cow.environment.producer.core.message.Message
import network.cow.environment.producer.core.message.Payload
import network.cow.environment.producer.core.message.consumer.FadeAudioPayload
import network.cow.environment.producer.core.message.consumer.PannerAttributes
import network.cow.environment.producer.core.message.consumer.PlayAudioPayload
import network.cow.environment.producer.core.message.consumer.ProducerBoundPayload
import network.cow.environment.producer.core.message.consumer.SetPositionPayload
import network.cow.environment.producer.core.message.consumer.Sprite
import network.cow.environment.producer.core.message.consumer.StopAudioPayload
import network.cow.environment.producer.core.message.consumer.UpdateAudioPayload
import network.cow.environment.producer.core.message.service.RegisterConsumerPayload
import network.cow.environment.producer.core.message.service.UnregisterConsumerPayload
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
    private val instanceListeners = mutableMapOf<UUID, (ProducerBoundPayload) -> Unit>()

    protected fun update() {
        this.consumers.filter {
            it.value.state == AudioConsumer.State.CONNECTED
        }.entries.forEach { (context, consumer) ->
            // Update consumer positions.
            this.send(SetPositionPayload(consumer.id, this.getPosition(context)))

            // Update triggers.
            this.triggers.forEach { trigger -> trigger.update(context) }
        }
    }

    protected fun addConsumer(context: ContextType, callback: (String) -> Unit) {
        // TODO: set isConnected = true only when browser/consumer is connected
        this.consumers[context] = AudioConsumer(UUID.randomUUID(), AudioConsumer.State.CONNECTED)
        this.send(RegisterConsumerPayload(this.getContextId(context)))
        // TODO: wait for register success message
        callback("https://environment.cow.network/TODO")
    }

    protected fun removeConsumer(context: ContextType) {
        this.triggers.forEach { it.invalidate(context) }
        val consumer = this.consumers.remove(context) ?: return
        this.send(UnregisterConsumerPayload(consumer.id))
    }

    internal fun addListener(id: UUID, callback: (ProducerBoundPayload) -> Unit) {
        this.instanceListeners[id] = callback
    }

    internal fun removeListener(id: UUID) {
        this.instanceListeners.remove(id)
    }

    fun addTrigger(trigger: Trigger<ContextType>) {
        this.triggers.add(trigger)
    }

    fun addTrigger(source: AudioSource<ContextType>, condition: Condition<ContextType>, fadeDuration: Int = 0,
                   volumeProvider: VolumeProvider<ContextType> = VolumeProviders.maxVolume(),
                   rateProvider: RateProvider<ContextType> = RateProviders.default(),
                   triggerOnce: Boolean = false, fireAndForget: Boolean = false
    ) : Trigger<ContextType> {
        val trigger = Trigger(source, condition, fadeDuration, volumeProvider, rateProvider, triggerOnce, fireAndForget)
        this.addTrigger(trigger)
        return trigger
    }

    fun removeTrigger(trigger: Trigger<ContextType>) {
        this.triggers.remove(trigger)
        this.consumers.keys.forEach(trigger::invalidate)
    }

    fun playAudio(context: ContextType, key: String, sprite: Sprite, id: UUID, volume: Double, rate: Double, loop: Boolean, loopFadeDuration: Int,
                  position: Point3D? = null, pannerAttributes: PannerAttributes? = null) {
        val consumer = this.getConsumer(context)
        this.send(PlayAudioPayload(consumer.id, key, sprite, id, volume, rate, loop, loopFadeDuration, position, pannerAttributes))
    }

    fun updateAudio(context: ContextType, id: UUID, volume: Double, rate: Double, loop: Boolean, loopFadeDuration: Int, position: Point3D? = null,
                    pannerAttributes: PannerAttributes? = null) {
        val consumer = this.getConsumer(context)
        this.send(UpdateAudioPayload(consumer.id, id, volume, rate, loop, loopFadeDuration, position, pannerAttributes))
    }

    fun fadeAudio(context: ContextType, id: UUID, volume: Double, duration: Int) {
        val consumer = this.getConsumer(context)
        this.send(FadeAudioPayload(consumer.id, id, volume, duration))
    }

    fun stopAudio(context: ContextType, id: UUID, duration: Int) {
        val consumer = this.getConsumer(context)
        this.send(StopAudioPayload(consumer.id, id, duration))
    }

    private fun send(payload: Payload) {
        val message = Message(payload.getType(), payload)
        println(GsonBuilder().create().toJson(message))
    }

    private fun getConsumer(context: ContextType) = this.consumers[context] ?: TODO("error")

    protected abstract fun getPosition(context: ContextType) : Point3D

    protected abstract fun getContextId(context: ContextType) : UUID

}
