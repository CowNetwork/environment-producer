package network.cow.environment.producer.core.trigger.condition

/**
 * @author Benedikt Wüller
 */
fun interface Condition<ContextType : Any> {

    fun validate(context: ContextType) : Boolean

}
