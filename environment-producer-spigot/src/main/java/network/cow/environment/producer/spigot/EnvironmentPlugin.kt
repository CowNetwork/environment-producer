package network.cow.environment.producer.spigot

import network.cow.environment.producer.core.trigger.condition.and
import network.cow.environment.producer.core.trigger.condition.andNot
import network.cow.environment.producer.spigot.source.GlobalAudioSource
import network.cow.environment.producer.spigot.trigger.condition.EventCondition
import network.cow.environment.producer.spigot.trigger.condition.SpigotCondition
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Benedikt Wüller
 */
class EnvironmentPlugin : JavaPlugin() {

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(SpigotAudioEngine, this)

        val birdsSource = GlobalAudioSource("common.environment.birds", 0.3, true, 5000)
        val underWater = SpigotCondition { it.eyeLocation.block.type == Material.WATER }
        val dayTime = SpigotCondition { it.world.isDayTime }
        val inSunlight = SpigotCondition { it.location.block.lightFromSky > 7 }
        SpigotAudioEngine.addTrigger(birdsSource, dayTime and inSunlight andNot underWater, 1000)

//        val pickUpSource = GlobalAudioSource("minigame.smash.item_pickup", 0.5)
//        pickUpSource.play(player)
//
//        val eventCondition = EventCondition()
//        SpigotAudioEngine.addTrigger(pickUpSource, eventCondition, fireAndForget = true)
//        // ...
//        eventCondition.trigger(player)
    }

}
