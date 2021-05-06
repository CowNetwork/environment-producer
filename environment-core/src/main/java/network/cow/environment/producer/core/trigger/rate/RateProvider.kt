package network.cow.environment.producer.core.trigger.rate

/**
 * @author Benedikt Wüller
 */
fun interface RateProvider<ContextType> {

    fun getRate(context: ContextType) : Double

}
