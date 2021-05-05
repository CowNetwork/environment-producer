package network.cow.environment.producer.api.audio.source

import network.cow.environment.producer.api.audio.AudioSection

/**
 * @author Benedikt Wüller
 */
open class GlobalAudioSource(key: String, volume: Double = 1.0, loop: Boolean = false, rate: Double = 1.0, section: AudioSection = AudioSection())
    : AudioSource(key, volume, loop, rate, section)
