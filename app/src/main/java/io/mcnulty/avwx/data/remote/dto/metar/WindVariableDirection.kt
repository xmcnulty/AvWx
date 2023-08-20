package io.mcnulty.avwx.data.remote.dto.metar

data class WindVariableDirection(
    val value: Int
) : io.mcnulty.avwx.data.remote.dto.Dto {
    override fun description(): String {
        return "WindVariableDirection - value: $value"
    }
}