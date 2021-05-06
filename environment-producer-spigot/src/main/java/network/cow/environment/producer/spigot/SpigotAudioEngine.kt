package network.cow.environment.producer.spigot

import com.google.gson.GsonBuilder
import network.cow.environment.producer.core.AudioEngine
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Benedikt Wüller
 */
object SpigotAudioEngine : AudioEngine<Player>(), Listener {

    init {
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(EnvironmentPlugin::class.java), Runnable(this::update), 2L, 2L)
    }

    override fun send(context: Player, payload: Any) {
        println(context)
        println(GsonBuilder().create().toJson(payload))
    }

    override fun getPosition(context: Player) = context.location.toPoint()

    @EventHandler
    private fun onPlayerJoin(event: PlayerJoinEvent) {
        this.addConsumer(event.player) { url -> event.player.sendMessage(url) }
    }

    @EventHandler
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        this.removeConsumer(event.player)
    }

}
