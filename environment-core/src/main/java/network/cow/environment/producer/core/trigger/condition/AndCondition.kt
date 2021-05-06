package network.cow.environment.producer.core.trigger.condition

/**
 * @author Benedikt Wüller
 */
class AndCondition<ContextType : Any>(private vararg val conditions: Condition<ContextType>) : Condition<ContextType> {

    override fun validate(context: ContextType) = this.conditions.all { it.validate(context) }

}

infix fun <T : Any> Condition<T>.and(other: Condition<T>) : Condition<T> = AndCondition(this, other)
fun <T : Any> Condition<T>.and(vararg others: Condition<T>) : Condition<T> = AndCondition(this, *others)
