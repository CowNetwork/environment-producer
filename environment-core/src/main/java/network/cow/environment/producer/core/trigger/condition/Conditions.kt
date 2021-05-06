package network.cow.environment.producer.core.trigger.condition

/**
 * @author Benedikt Wüller
 */
object Conditions {

    fun <T : Any> active() = Condition<T> { true }
    fun <T : Any> inactive() = Condition<T> { false }

}
