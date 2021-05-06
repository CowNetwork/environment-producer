package network.cow.environment.producer.core.source

import network.cow.environment.producer.core.AudioEngine
import network.cow.environment.producer.core.message.payload.Sprite
import network.cow.environment.producer.core.message.payload.UpdateAudioPayload
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
open class GlobalAudioSource<ContextType : Any>(
        engine: AudioEngine<ContextType>,
        key: String,
        volume: Double = 1.0,
        loop: Boolean = false,
        loopFadeDuration: Int = 0,
        rate: Double = 1.0,
        section: Sprite = Sprite()
) : AudioSource<ContextType>(engine, key, volume, loop, loopFadeDuration, rate, section) {

    override fun createUpdateAudioPayload(id: UUID, volume: Double, rate: Double) : UpdateAudioPayload {
        return UpdateAudioPayload(id, volume, rate, this.loop, this.loopFadeDuration)
    }

}
