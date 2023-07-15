package io.mcnulty.avwx.domain.model

/**
 * Represents different types of visibility obstructions in a METAR (Meteorological Aerodrome Report) report.
 *
 * Each visibility obstruction is associated with a specific code.
 *
 * @param code The code associated with the visibility obstruction.
 */
sealed class VisibilityObstruction(val code: String) {

    object Fog : VisibilityObstruction("FG")
    object Smoke : VisibilityObstruction("FU")
    object Spray : VisibilityObstruction("PY")
    object Mist : VisibilityObstruction("BR")
    object Sand : VisibilityObstruction("SA")
    object Dust : VisibilityObstruction("DU")
    object VolcanicAsh : VisibilityObstruction("VA")

    /**
     * Returns a string representation of the VisibilityObstruction object.
     *
     * The returned string is the code associated with the visibility obstruction.
     *
     * @return A string representation of the VisibilityObstruction object.
     */
    override fun toString(): String = code
}
