package network.cow.environment.producer.core.trigger.volume

/**
 * @author Benedikt Wüller
 */
fun interface VolumeProvider<ContextType> {

    fun getVolume(context: ContextType) : Double

}
