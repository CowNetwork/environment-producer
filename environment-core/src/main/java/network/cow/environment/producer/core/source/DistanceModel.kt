package network.cow.environment.producer.core.source

/**
 * @author Benedikt Wüller
 */
enum class DistanceModel(val key: String) {
    INVERSE("inverse"),
    LINEAR("linear"),
    EXPONENTIAL("exponential")
}
