package network.cow.environment.producer.core

import network.cow.environment.producer.core.source.AudioSource
import java.util.UUID
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * @author Benedikt Wüller
 */
class AudioInstance<ContextType : Any>(val context: ContextType, private val source: AudioSource<ContextType>) {

    val id = UUID.randomUUID()

    var volume: Double by Delegates.observable(1.0, ::update)
    var rate: Double by Delegates.observable(1.0, ::update)

    internal var isPlaying: Boolean = false

    private var suppressUpdate = false

    fun play() {
        if (this.isPlaying) return
        this.isPlaying = true
        this.source.sendPlay(this.context, this.id, this.volume, this.rate)
    }

    fun fadeTo(volume: Double, duration: Int) {
        if (!this.isPlaying) throw IllegalStateException("The audio instance is not playing.")
        this.source.sendFade(this.context, this.id, this.volume, duration)
        this.suppressUpdate { this.volume = volume }
    }

    fun stop(fadeDuration: Int = 0) {
        if (!this.isPlaying) return
        this.isPlaying = false
        this.source.sendStop(this.context, this.id, fadeDuration)
    }

    fun suppressUpdate(init: AudioInstance<ContextType>.() -> Unit) {
        this.suppressUpdate = true
        this.init()
        this.suppressUpdate = false
    }

    @Suppress("UNUSED_PARAMETER") // The unused parameters are required for method references to work.
    internal fun update(property: KProperty<*>, oldValue: Any, newValue: Any) {
        if (this.suppressUpdate) return
        this.source.sendUpdate(this.context, this.id, this.volume, this.rate)
    }

}
