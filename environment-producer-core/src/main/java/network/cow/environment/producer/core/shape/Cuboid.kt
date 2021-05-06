package network.cow.environment.producer.core.shape

import network.cow.environment.producer.core.Point3D
import network.cow.environment.producer.core.maxOf
import network.cow.environment.producer.core.minOf

/**
 * @author Benedikt Wüller
 */
class Cuboid(from: Point3D, to: Point3D) : Shape {

    var min = minOf(from, to)
    var max = maxOf(from, to)

    override fun isInside(position: Point3D): Boolean {
        if (position.x < this.min.x || position.x > this.max.x) return false
        if (position.y < this.min.y || position.y > this.max.y) return false
        if (position.z < this.min.z || position.z > this.max.z) return false
        return true
    }

}
