package network.cow.environment.producer.core.trigger.condition

import network.cow.environment.producer.core.shape.Shape
import network.cow.environment.protocol.Point3D

/**
 * @author Benedikt Wüller
 */
abstract class RegionalCondition<ContextType : Any>(private val shape: Shape) : Condition<ContextType> {

    override fun validate(context: ContextType): Boolean {
        val position = this.getPosition(context)
        return this.shape.isInside(position)
    }

    protected abstract fun getPosition(context: ContextType) : Point3D

}
