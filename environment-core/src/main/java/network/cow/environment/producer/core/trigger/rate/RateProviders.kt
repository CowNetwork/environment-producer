package network.cow.environment.producer.core.trigger.rate

/**
 * @author Benedikt Wüller
 */
object RateProviders {

    fun <T : Any> default() = RateProvider<T> { 1.0 }
    fun <T : Any> slowest() = RateProvider<T> { 0.5 }
    fun <T : Any> fastest() = RateProvider<T> { 4.0 }

}
