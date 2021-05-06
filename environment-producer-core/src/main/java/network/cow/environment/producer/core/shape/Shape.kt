package network.cow.environment.producer.core.shape

import network.cow.environment.producer.core.Point3D

/**
 * @author Benedikt Wüller
 */
interface Shape {

    fun isInside(position: Point3D) : Boolean

}
