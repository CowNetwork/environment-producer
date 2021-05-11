package network.cow.environment.producer.spigot

import org.bukkit.entity.Player
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author Benedikt Wüller
 */

fun Player.getDirectionalVector(pitch: Double = this.location.pitch.toDouble(), yaw: Double = this.location.yaw.toDouble()): Vector {
    val vector = Vector()
    vector.y = -sin(Math.toRadians(pitch))
    val xz: Double = cos(Math.toRadians(pitch))
    vector.x = -xz * sin(Math.toRadians(yaw))
    vector.z = xz * cos(Math.toRadians(yaw))
    return vector.normalize()
}
