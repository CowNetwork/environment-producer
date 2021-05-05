package network.cow.environment.producer.api

import network.cow.environment.producer.api.payload.FadeAudioPayload
import network.cow.environment.producer.api.payload.PlayAudioPayload
import network.cow.environment.producer.api.payload.SetPositionPayload
import network.cow.environment.producer.api.payload.StopAudioPayload
import network.cow.environment.producer.api.payload.UpdateAudioPayload

/**
 * @author Benedikt Wüller
 */
abstract class AudioEngine<ContextType : Any> {

    fun setPosition(context: ContextType, payload: SetPositionPayload) = this.send(context, payload)
    fun playAudio(context: ContextType, payload: PlayAudioPayload) = this.send(context, payload)
    fun updateAudio(context: ContextType, payload: UpdateAudioPayload) = this.send(context, payload)
    fun fadeAudio(context: ContextType, payload: FadeAudioPayload) = this.send(context, payload)
    fun stopAudio(context: ContextType, payload: StopAudioPayload) = this.send(context, payload)

    protected abstract fun send(context: ContextType, payload: Any)

}
