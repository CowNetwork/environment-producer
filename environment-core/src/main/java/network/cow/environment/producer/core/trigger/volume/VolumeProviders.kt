package network.cow.environment.producer.core.trigger.volume

/**
 * @author Benedikt Wüller
 */
object VolumeProviders {

    fun <T : Any> maxVolume() = VolumeProvider<T> { 1.0 }
    fun <T : Any> silent() = VolumeProvider<T> { 0.0 }

}
