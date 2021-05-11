package network.cow.environment.producer.core.source

import network.cow.environment.producer.core.AudioEngine
import network.cow.environment.protocol.Point3D
import network.cow.environment.protocol.consumer.PannerAttributes
import network.cow.environment.protocol.consumer.Sprite
import kotlin.properties.Delegates

/**
 * @author Benedikt Wüller
 */
open class PositionalAudioSource<ContextType : Any>(
        engine: AudioEngine<ContextType>,
        key: String,
        position: Point3D,
        maxVolumeRadius: Double,
        rollOffFactor: Double = DEFAULT_ROLL_OFF_FACTOR,
        distanceModel: DistanceModel = DistanceModel.EXPONENTIAL,
        volume: Double = 1.0,
        loop: Boolean = false,
        loopFadeDuration: Int = 0,
        rate: Double = 1.0,
        section: Sprite = Sprite()
) : AudioSource<ContextType>(engine, key, volume, loop, loopFadeDuration, rate, section) {

    companion object {
        const val DEFAULT_ROLL_OFF_FACTOR = 1.75
    }

    var x: Double by Delegates.observable(position.x, ::updateInstances)
    var y: Double by Delegates.observable(position.x, ::updateInstances)
    var z: Double by Delegates.observable(position.x, ::updateInstances)

    var maxVolumeRadius: Double by Delegates.observable(maxVolumeRadius, ::updateInstances)
    var rollOffFactor: Double by Delegates.observable(rollOffFactor, ::updateInstances)
    var distanceModel: DistanceModel by Delegates.observable(distanceModel, ::updateInstances)

    init {
        if (this.maxVolumeRadius < 0.0) throw IllegalArgumentException("The max volume radius must be greater than or equal to zero.")
        if (this.rollOffFactor < 0.0) throw IllegalArgumentException("The roll off factor must be greater than or equal to zero.")
        if (this.distanceModel == DistanceModel.LINEAR && this.rollOffFactor > 1.0) throw IllegalArgumentException("The roll off factor must not be greater than 1.0 when using the ${DistanceModel.LINEAR} distance model.")
    }

    override fun getPosition(): Point3D? = Point3D(this.x, this.y, this.z)

    override fun getPannerAttributes(): PannerAttributes? = PannerAttributes(this.distanceModel.key, this.maxVolumeRadius, this.rollOffFactor)

}
