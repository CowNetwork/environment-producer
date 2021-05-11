package network.cow.environment.producer.spigot

import network.cow.environment.producer.core.trigger.condition.and
import network.cow.environment.producer.core.trigger.condition.andNot
import network.cow.environment.producer.spigot.source.GlobalAudioSource
import network.cow.environment.producer.spigot.trigger.condition.SpigotCondition
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Benedikt Wüller
 */
class EnvironmentPlugin : JavaPlugin() {

    override fun onEnable() {
        Bukkit.getPluginManager().registerEvents(Environment, this)

        val underWater = SpigotCondition { it.eyeLocation.block.type == Material.WATER }
        val dayTime = SpigotCondition { it.world.isDayTime }
        val inSunlight = SpigotCondition { it.location.block.lightFromSky > 10 }

        val birdsSource = GlobalAudioSource("common.environment.birds", 0.3, true, 5000)
        val underWaterSource = GlobalAudioSource("common.environment.under_water", 0.5, true, 10000)

        Environment.addTrigger(birdsSource, dayTime and inSunlight andNot underWater, 3000, playOnce = true)
        Environment.addTrigger(underWaterSource, underWater, 1000, playOnce = true)
    }

    override fun onDisable() {
        Environment.disconnect()
    }

}
