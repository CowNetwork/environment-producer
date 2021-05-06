package network.cow.environment.producer.core

import kotlin.math.pow
import kotlin.math.sqrt

data class Point3D(val x: Double, val y: Double, val z: Double) {

    fun squaredDistanceTo(other: Point3D) = (other.x - this.x).pow(2) + (other.y - this.y).pow(2) + (other.z - this.z).pow(2)

    fun distanceTo(other: Point3D) = sqrt(this.squaredDistanceTo(other))

}

fun minOf(vararg points: Point3D) = Point3D(
        points.map { it.x }.minOrNull() ?: Double.NaN,
        points.map { it.y }.minOrNull() ?: Double.NaN,
        points.map { it.z }.minOrNull() ?: Double.NaN
)

fun maxOf(vararg vectors: Point3D) = Point3D(
        vectors.map { it.x }.maxOrNull() ?: Double.NaN,
        vectors.map { it.y }.maxOrNull() ?: Double.NaN,
        vectors.map { it.z }.maxOrNull() ?: Double.NaN
)
