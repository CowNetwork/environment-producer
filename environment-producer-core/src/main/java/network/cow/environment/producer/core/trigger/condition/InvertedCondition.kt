package network.cow.environment.producer.core.trigger.condition

/**
 * @author Benedikt Wüller
 */
class InvertedCondition<ContextType : Any>(private val condition: Condition<ContextType>) : Condition<ContextType> {

    override fun validate(context: ContextType): Boolean = !this.condition.validate(context)

}

fun <T : Any> Condition<T>.not() : Condition<T> = InvertedCondition(this)
fun <T : Any> not(condition: Condition<T>) = InvertedCondition(condition)
fun <T : Any> not(init: () -> Condition<T>) = InvertedCondition(init())
