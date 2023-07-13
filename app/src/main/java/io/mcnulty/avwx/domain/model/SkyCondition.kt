package io.mcnulty.avwx.domain.model

sealed class SkyCondition(val altitude: Int, prefix: String) {
    class Broken(altitude: Int) : SkyCondition(altitude, "BKN")
    class Scattered(altitude: Int) : SkyCondition(altitude, "SCT")
    class Few(altitude: Int) : SkyCondition(altitude, "FEW")
    class Overcast(altitude: Int) : SkyCondition(altitude, "OVC")
}