package network.cow.environment.producer.core.trigger.condition

/**
 * @author Benedikt Wüller
 */
class OrCondition<ContextType : Any>(private vararg val conditions: Condition<ContextType>) : Condition<ContextType> {

    override fun validate(context: ContextType) = this.conditions.count { it.validate(context) } >= 1

}

infix fun <T : Any> Condition<T>.or(other: Condition<T>) : Condition<T> = OrCondition(this, other)
fun <T : Any> Condition<T>.or(vararg others: Condition<T>) : Condition<T> = OrCondition(this, *others)

infix fun <T : Any> Condition<T>.orNot(other: Condition<T>) = this or not(other)
