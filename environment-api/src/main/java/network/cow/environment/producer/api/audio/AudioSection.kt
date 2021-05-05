package network.cow.environment.producer.api.audio

/**
 * @author Benedikt Wüller
 */
data class AudioSection(val startAtSeconds: Double = 0.0, val stopAtSeconds: Double = SECTION_FULL_LENGTH) {

    companion object {
        const val SECTION_FULL_LENGTH = -1.0
    }

    init {
        if (this.startAtSeconds < 0) throw IllegalArgumentException("The start time must not be less than zero. Given: ${this.startAtSeconds}")
        if (this.stopAtSeconds != SECTION_FULL_LENGTH) {
            if (this.stopAtSeconds < this.startAtSeconds) throw IllegalArgumentException("The stop time must not be less than the start time.")
        }
    }

}
