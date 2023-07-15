package io.mcnulty.avwx.domain.model

import java.util.prefs.PreferenceChangeEvent

/**
 * Represents different types of precipitation in a METAR (Meteorological Aerodrome Report) report.
 *
 * @param intensity The intensity of the precipitation.
 * @param marker The marker associated with the precipitation type.
 */
sealed class Precipitation(val intensity: Intensity, val marker: String) {
    /**
     * Represents the intensity levels of precipitation.
     *
     * @property marker The marker associated with the precipitation intensity.
     */
    enum class Intensity(val marker: String) {
        LIGHT("-"), MODERATE(""), HEAVY("+"), VICINITY("VC")
    }

    class Rain(intensity: Intensity = Intensity.MODERATE) : Precipitation(intensity, "RA")
    class Drizzle(intensity: Intensity = Intensity.MODERATE) : Precipitation(intensity, "DZ")
    class Snow(intensity: Intensity = Intensity.MODERATE) : Precipitation(intensity, "SN")
    class Hail(intensity: Intensity = Intensity.MODERATE) : Precipitation(intensity, "GR")
    class SnowPellets(intensity: Intensity = Intensity.MODERATE) : Precipitation(intensity, "GS")
    class IcePellets(intensity: Intensity = Intensity.MODERATE) : Precipitation(intensity, "PL")
    class SnowGrains(intensity: Intensity = Intensity.MODERATE) : Precipitation(intensity, "SG")
    class IceCrystals(intensity: Intensity = Intensity.MODERATE) : Precipitation(intensity, "IC")
    class UnknownPrecipitation(intensity: Intensity = Intensity.MODERATE) : Precipitation(intensity, "UP")

    /**
     * Returns a string representation of the Precipitation object.
     *
     * The returned string consists of the precipitation marker and the intensity marker.
     * If the intensity is not provided or set to Intensity.MODERATE, only the precipitation marker is included.
     *
     * @return A string representation of the Precipitation object.
     */
    override fun toString(): String {
        return if (intensity == Intensity.MODERATE) marker else "$marker${intensity.marker}"
    }
}
