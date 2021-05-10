package network.cow.environment.producer.core.message

import network.cow.environment.producer.core.PayloadRegistry

/**
 * @author Benedikt Wüller
 */
interface Payload {

    fun getType() = PayloadRegistry.getType(this)

}
