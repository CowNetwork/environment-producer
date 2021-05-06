package network.cow.environment.producer.core.trigger.condition

/**
 * @author Benedikt Wüller
 */
class GlobalCondition<ContextType : Any>(var isActive: Boolean) : Condition<ContextType> {

    override fun validate(context: ContextType) = this.isActive

}
