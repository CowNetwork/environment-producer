package network.cow.environment.producer.core

import network.cow.environment.producer.core.source.AudioSource
import network.cow.environment.producer.core.trigger.Trigger
import network.cow.environment.producer.core.trigger.condition.Condition
import network.cow.environment.producer.core.trigger.rate.RateProvider
import network.cow.environment.producer.core.trigger.rate.RateProviders
import network.cow.environment.producer.core.trigger.volume.VolumeProvider
import network.cow.environment.producer.core.trigger.volume.VolumeProviders
import network.cow.environment.protocol.Payload
import network.cow.environment.protocol.Point3D
import network.cow.environment.protocol.consumer.FadeAudioPayload
import network.cow.environment.protocol.consumer.PannerAttributes
import network.cow.environment.protocol.consumer.PlayAudioPayload
import network.cow.environment.protocol.consumer.ProducerBoundPayload
import network.cow.environment.protocol.consumer.SetPositionPayload
import network.cow.environment.protocol.consumer.Sprite
import network.cow.environment.protocol.consumer.StopAudioPayload
import network.cow.environment.protocol.consumer.UpdateAudioPayload
import network.cow.environment.protocol.service.ConsumerConnectedPayload
import network.cow.environment.protocol.service.ConsumerDisconnectedPayload
import network.cow.environment.protocol.service.ConsumerRegisteredPayload
import network.cow.environment.protocol.service.RegisterConsumerPayload
import network.cow.environment.protocol.service.UnregisterConsumerPayload
import java.util.UUID
import java.util.WeakHashMap

/**
 * @author Benedikt Wüller
 */
abstract class AudioEngine<ContextType : Any> {

    private val client = WebSocketClient()

    private val consumers = mutableMapOf<UUID, AudioConsumer>()
    private val contextConsumers = WeakHashMap<ContextType, AudioConsumer>()
    private val triggers = mutableSetOf<Trigger<ContextType>>()
    private val instanceListeners = mutableMapOf<UUID, (ProducerBoundPayload) -> Unit>()

    init {
        this.client.onOpen = {
            this.consumers.forEach { (id, consumer) ->
                consumer.state = AudioConsumer.State.UNREGISTERED
                this.send(RegisterConsumerPayload(id))
            }
        }
        this.client.onMessage = ::handlePayload
        this.client.onClose = this.client::connect
        this.client.connect()
    }

    fun disconnect() {
        this.client.onMessage = {}
        this.client.onClose = {}
        this.client.onOpen = {}
        this.client.disconnect()
    }

    private fun handlePayload(payload: Payload) {
        println(payload.type)
        println(JsonService.toJson(payload))
        println(payload)

        when (payload) {
            is ConsumerRegisteredPayload -> {
                val consumer = this.contextConsumers.values.firstOrNull { it.contextId == payload.contextId } ?: return
                consumer.id = payload.consumerId
                this.consumers[consumer.id] = consumer
                consumer.state = AudioConsumer.State.REGISTERED
                consumer.url = payload.consumerUrl
                consumer.callback(consumer.url)
                println("registered")
            }
            is ConsumerConnectedPayload -> {
                val consumer = this.consumers[payload.consumerId] ?: return
                consumer.state = AudioConsumer.State.CONNECTED
            }
            is ConsumerDisconnectedPayload -> {
                val consumer = this.consumers[payload.consumerId] ?: return
                if (consumer.state != AudioConsumer.State.CONNECTED) return
                consumer.state = AudioConsumer.State.REGISTERED
            }
        }
    }

    protected fun update() {
        this.contextConsumers.filter {
            it.value.state == AudioConsumer.State.CONNECTED
        }.entries.forEach { (context, consumer) ->
            // Update consumer positions.
            this.send(SetPositionPayload(consumer.id, this.getPosition(context), this.getFrontVector(context), this.getUpVector(context)))

            // Update triggers.
            this.triggers.forEach { trigger -> trigger.update(context) }
        }
    }

    protected fun addConsumer(context: ContextType, callback: (String) -> Unit) {
        val contextId = this.getContextId(context)
        val consumer = AudioConsumer(contextId, AudioConsumer.State.UNREGISTERED, callback)
        this.contextConsumers[context] = consumer
        this.send(RegisterConsumerPayload(contextId))
    }

    protected fun removeConsumer(context: ContextType) {
        this.triggers.forEach { it.invalidate(context) }
        val consumer = this.contextConsumers.remove(context) ?: return
        this.consumers.remove(consumer.id)
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
                   playOnce: Boolean = false, fireAndForget: Boolean = false
    ) : Trigger<ContextType> {
        val trigger = Trigger(source, condition, fadeDuration, volumeProvider, rateProvider, playOnce, fireAndForget)
        this.addTrigger(trigger)
        return trigger
    }

    fun removeTrigger(trigger: Trigger<ContextType>) {
        this.triggers.remove(trigger)
        this.contextConsumers.keys.forEach(trigger::invalidate)
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

    private fun send(payload: Payload) = this.client.send(payload)

    private fun getConsumer(context: ContextType) = this.contextConsumers[context] ?: TODO("error")

    protected abstract fun getPosition(context: ContextType) : Point3D

    protected abstract fun getFrontVector(context: ContextType) : Point3D

    protected abstract fun getUpVector(context: ContextType) : Point3D

    protected abstract fun getContextId(context: ContextType) : UUID

}
