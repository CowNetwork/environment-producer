package network.cow.environment.producer.spigot.trigger.condition

import org.bukkit.entity.Player
import java.util.WeakHashMap

/**
 * @author Benedikt Wüller
 */
class EventCondition : SpigotCondition {

    private val states = WeakHashMap<Player, Boolean>()

    override fun validate(context: Player): Boolean {
        val state = this.states[context] ?: return false
        if (!state) return false
        this.states.remove(context)
        return true
    }

    fun trigger(player: Player) {
        this.states[player] = true
    }

}
