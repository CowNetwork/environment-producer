package network.cow.environment.producer.spigot

import network.cow.environment.protocol.Point3D
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.util.Vector

/**
 * @author Benedikt Wüller
 */

fun Vector.toPoint() = Point3D(this.x, this.y, this.z)
fun Point3D.toVector() = Vector(this.x, this.y, this.z)

fun Location.toPoint() = this.toVector().toPoint()
fun Point3D.toLocation(world: World) = this.toVector().toLocation(world)
