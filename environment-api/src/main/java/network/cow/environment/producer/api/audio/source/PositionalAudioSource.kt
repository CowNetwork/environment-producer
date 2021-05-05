package network.cow.environment.producer.api.audio.source

import network.cow.environment.producer.api.audio.AudioSection
import network.cow.environment.producer.api.audio.DistanceModel
import kotlin.properties.Delegates

/**
 * @author Benedikt Wüller
 */
class PositionalAudioSource(
        key: String,
        position: Triple<Double, Double, Double>,
        maxVolumeRadius: Double,
        rollOffFactor: Double = DEFAULT_ROLL_OFF_FACTOR,
        distanceModel: DistanceModel = DistanceModel.EXPONENTIAL,
        volume: Double = 1.0,
        loop: Boolean = false,
        rate: Double = 1.0,
        section: AudioSection = AudioSection()
) : AudioSource(key, volume, loop, rate, section) {

    companion object {
        const val DEFAULT_ROLL_OFF_FACTOR = 1.75
    }

    var x: Double by Delegates.observable(position.first, ::onChange)
    var y: Double by Delegates.observable(position.second, ::onChange)
    var z: Double by Delegates.observable(position.third, ::onChange)

    var maxVolumeRadius: Double by Delegates.observable(maxVolumeRadius, ::onChange)
    var rollOffFactor: Double by Delegates.observable(rollOffFactor, ::onChange)
    var distanceModel: DistanceModel by Delegates.observable(distanceModel, ::onChange)

}
