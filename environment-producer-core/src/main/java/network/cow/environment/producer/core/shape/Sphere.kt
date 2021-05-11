package network.cow.environment.producer.core.shape

import network.cow.environment.protocol.Point3D
import kotlin.math.pow

/**
 * @author Benedikt Wüller
 */
class Sphere(var center: Point3D, radius: Double) : Shape {

    var squaredRadius = radius.pow(2); private set

    var radius: Double = radius
        set(value) {
            field = value
            this.squaredRadius = value.pow(2)
        }

    override fun isInside(position: Point3D) = position.squaredDistanceTo(this.center) <= this.squaredRadius

}
