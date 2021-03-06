package network.cow.environment.producer.core.trigger.condition

/**
 * @author Benedikt Wüller
 */
class AndCondition<ContextType : Any>(private vararg val conditions: Condition<ContextType>) : Condition<ContextType> {

    override fun validate(context: ContextType) = this.conditions.count { it.validate(context) } == this.conditions.size

}

infix fun <T : Any> Condition<T>.and(other: Condition<T>) : Condition<T> = AndCondition(this, other)
fun <T : Any> Condition<T>.and(vararg others: Condition<T>) : Condition<T> = AndCondition(this, *others)

infix fun <T : Any> Condition<T>.andNot(other: Condition<T>) : Condition<T> = this and not(other)
