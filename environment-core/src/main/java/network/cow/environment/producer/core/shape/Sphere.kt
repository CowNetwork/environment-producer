package network.cow.environment.producer.core.shape

import network.cow.environment.producer.core.Point3D
import kotlin.math.pow

/**
 * @author Benedikt Wüller
 */
class Sphere(val center: Point3D, val radius: Double) : Shape {

    val squaredRadius = radius.pow(2)

    override fun isInside(position: Point3D) = position.squaredDistanceTo(this.center) <= this.squaredRadius

}
