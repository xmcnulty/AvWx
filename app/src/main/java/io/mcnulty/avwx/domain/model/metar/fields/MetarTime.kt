package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.MetarBlock
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

data class MetarTime(
    val day: Int,
    val hour: Int,
    val minute: Int
) : MetarBlock {

    private val calendar: Calendar

    init {
        require(day in 1..31) { "Day must be between 1 and 31" }
        require(hour in 0..23) { "Hour must be between 0 and 23" }
        require(minute in 0..59) { "Minute must be between 0 and 59" }

        calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
    }

    override val code: String
        get() = day.toString().padStart(2, '0') +
                hour.toString().padStart(2, '0') +
                "${minute.toString().padStart(2, '0')}Z"


    /**
     * Returns a description of the time in format DD Mmmm HH:MM UTC
     */
    override val description: String
        get() {
            val format = SimpleDateFormat("dd MMMM yyyy HH:mm 'UTC'")
            format.timeZone = TimeZone.getTimeZone("UTC")
            return "Reported: ${format.format(calendar.time)}"
        }
}
