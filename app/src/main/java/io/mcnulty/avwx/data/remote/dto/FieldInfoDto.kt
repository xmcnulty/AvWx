package io.mcnulty.avwx.data.remote.dto

import com.google.gson.annotations.SerializedName

data class FieldInfoDto(
    val city: String,
    val country: String,
    @SerializedName("elevation_ft")
    val elevationFeet: Int,
    @SerializedName("elevation_m")
    val elevationMeters: Int,
    val gps: String,
    val iata: String,
    val icao: String,
    val latitude: Double,
    val local: String,
    val longitude: Double,
    val name: String,
    val note: String,
    val reporting: Boolean,
    val runways: List<Runway>,
    val state: String,
    val type: String,
    val website: String,
    val wiki: String
) {
    data class Runway(
        val bearing1: Int,
        val bearing2: Int,
        val ident1: String,
        val ident2: String,
        @SerializedName("length_ft")
        val lengthFeet: Int,
        val lights: Boolean,
        val surface: String,
        @SerializedName("width_ft")
        val widthFeet: Int
    )
}