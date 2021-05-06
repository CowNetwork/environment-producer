package network.cow.environment.producer.core

import com.google.gson.GsonBuilder
import network.cow.environment.producer.core.message.payload.FadeAudioPayload
import network.cow.environment.producer.core.message.payload.PlayAudioPayload
import network.cow.environment.producer.core.message.payload.SetPositionPayload
import network.cow.environment.producer.core.message.payload.StopAudioPayload
import network.cow.environment.producer.core.message.payload.UpdateAudioPayload
import network.cow.environment.producer.core.trigger.Trigger
import java.util.UUID
import java.util.WeakHashMap

/**
 * @author Benedikt Wüller
 */
abstract class AudioEngine<ContextType : Any> {

    private val consumers = WeakHashMap<ContextType, UUID>()
    private val triggers = mutableListOf<Trigger<ContextType>>()

    protected fun update() {
        this.consumers.keys.forEach { context ->
            // Update consumer positions.
//            this.setPosition(context, SetPositionPayload(this.getPosition(context)))

            // Update triggers.
            this.triggers.forEach { trigger -> trigger.update(context) }
        }
    }

    protected fun addConsumer(context: ContextType, callback: (String) -> Unit) {
        val id = UUID.randomUUID()
        this.consumers[context] = id
        // TODO: send consumer registered request and call callback with url
        callback("https://environment.cow.network/TODO")
    }

    protected fun removeConsumer(context: ContextType) {
        this.triggers.forEach { it.invalidate(context) }
        this.consumers.remove(context)
    }

    private fun setPosition(context: ContextType, payload: SetPositionPayload) = this.send(context, payload)

    fun playAudio(context: ContextType, payload: PlayAudioPayload) = this.send(context, payload)
    fun updateAudio(context: ContextType, payload: UpdateAudioPayload) = this.send(context, payload)
    fun fadeAudio(context: ContextType, payload: FadeAudioPayload) = this.send(context, payload)
    fun stopAudio(context: ContextType, payload: StopAudioPayload) = this.send(context, payload)

    protected abstract fun send(context: ContextType, payload: Any)

    protected abstract fun getPosition(context: ContextType) : Point3D

}
