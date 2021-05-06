package network.cow.environment.producer.core.trigger.condition

/**
 * @author Benedikt Wüller
 */
class ExclusiveOrCondition<ContextType : Any>(private vararg val conditions: Condition<ContextType>) : Condition<ContextType> {

    override fun validate(context: ContextType) = this.conditions.count { it.validate(context) } == 1

}

infix fun <T : Any> Condition<T>.xor(other: Condition<T>) : Condition<T> = ExclusiveOrCondition(this, other)
fun <T : Any> Condition<T>.xor(vararg others: Condition<T>) : Condition<T> = ExclusiveOrCondition(this, *others)

infix fun <T : Any> Condition<T>.xorNot(other: Condition<T>) = this xor not(other)
