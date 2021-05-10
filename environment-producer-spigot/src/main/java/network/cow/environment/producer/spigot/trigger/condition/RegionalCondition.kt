package network.cow.environment.producer.spigot.trigger.condition

import network.cow.environment.producer.core.shape.Shape
import network.cow.environment.producer.core.trigger.condition.RegionalCondition
import network.cow.environment.producer.spigot.toPoint
import org.bukkit.entity.Player

/**
 * @author Benedikt Wüller
 */
class RegionalCondition(shape: Shape) : RegionalCondition<Player>(shape), SpigotCondition {

    override fun getPosition(context: Player) = context.location.toPoint()

}
