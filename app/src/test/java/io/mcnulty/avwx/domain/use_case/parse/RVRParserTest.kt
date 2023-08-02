package io.mcnulty.avwx.domain.use_case.parse

import com.google.common.truth.Truth.assertThat
import io.mcnulty.avwx.data.remote.dto.metar.RunwayVisibilityDto
import io.mcnulty.avwx.domain.model.metar.measurement.VisibilityUnits
import org.junit.Test

class RVRParserTest {

    private fun buildVisibilityDto(
        repr: String
    ) = RunwayVisibilityDto.RunwayVisibility(
        repr = repr,
        spoken = "",
        value = 0
    )

    @Test
    fun `test simple runway visibility with simple runway`() {
        val expectedCode = "R09/1800ft"

        val rvrDto = RunwayVisibilityDto(
            repr = "R09/1800ft",
            runway = "09",
            visibility = buildVisibilityDto("1800"),
            variableVisibility = emptyList(),
            trend = ""
        )

        val rvr = RVRParser.parse(listOf(rvrDto), VisibilityUnits.FEET)

        assertThat(rvr).hasSize(1)
        assertThat(rvr[0].code).isEqualTo(expectedCode)
        assertThat(rvr[0].description).isEqualTo("Runway 09 visual range: 1800ft")
    }

    @Test
    fun `test simple runway visibility with positioned runway`() {
        val expectedCode = "R09L/1800ft"

        val rvrDto = RunwayVisibilityDto(
            repr = "R09L/1800ft",
            runway = "09L",
            visibility = buildVisibilityDto("1800"),
            variableVisibility = emptyList(),
            trend = ""
        )

        val rvr = RVRParser.parse(listOf(rvrDto), VisibilityUnits.FEET)

        assertThat(rvr).hasSize(1)
        assertThat(rvr[0].code).isEqualTo(expectedCode)
        assertThat(rvr[0].description).isEqualTo("Runway 09L visual range: 1800ft")
    }

    @Test
    fun `test runway visibility with less than`() {
        val expectedCode = "R09/M1800ft"

        val rvrDto = RunwayVisibilityDto(
            repr = expectedCode,
            runway = "09",
            visibility = buildVisibilityDto("M1800"),
            variableVisibility = emptyList(),
            trend = ""
        )

        val rvr = RVRParser.parse(listOf(rvrDto), VisibilityUnits.FEET)

        assertThat(rvr).hasSize(1)
        assertThat(rvr[0].code).isEqualTo(expectedCode)
        assertThat(rvr[0].description).isEqualTo("Runway 09 visual range: less than 1800ft")
    }

    @Test
    fun `test runway visibility with greater than`() {
        val expectedCode = "R09/P1800ft"

        val rvrDto = RunwayVisibilityDto(
            repr = expectedCode,
            runway = "09",
            visibility = buildVisibilityDto("P1800"),
            variableVisibility = emptyList(),
            trend = ""
        )

        val rvr = RVRParser.parse(listOf(rvrDto), VisibilityUnits.FEET)

        assertThat(rvr).hasSize(1)
        assertThat(rvr[0].code).isEqualTo(expectedCode)
        assertThat(rvr[0].description).isEqualTo("Runway 09 visual range: greater than 1800ft")
    }

    @Test
    fun `test varying runway visibility`() {
        val expectedCode = "R09/1800V2000ft"

        val rvrDto = RunwayVisibilityDto(
            repr = expectedCode,
            runway = "09",
            visibility = null,
            variableVisibility = listOf(
                buildVisibilityDto("1800"),
                buildVisibilityDto("2000")
            ),
            trend = ""
        )

        val rvr = RVRParser.parse(listOf(rvrDto), VisibilityUnits.FEET)

        assertThat(rvr).hasSize(1)
        assertThat(rvr[0].code).isEqualTo(expectedCode)
        assertThat(rvr[0].description).isEqualTo("Runway 09 visual range: varying between 1800 and 2000ft")
    }

    @Test
    fun `test varying visibility with marker`() {
        val expectedCode = "R04R/6000VP6000ft"

        val rvrDto = RunwayVisibilityDto(
            repr = expectedCode,
            runway = "04R",
            visibility = null,
            variableVisibility = listOf(
                buildVisibilityDto("6000"),
                buildVisibilityDto("P6000")
            ),
            trend = ""
        )

        val rvr = RVRParser.parse(listOf(rvrDto), VisibilityUnits.FEET)

        assertThat(rvr).hasSize(1)
        assertThat(rvr[0].code).isEqualTo(expectedCode)
        assertThat(rvr[0].description).isEqualTo(
            "Runway 04R visual range: varying between 6000 and greater than 6000ft"
        )
    }

    @Test
    fun `test multiple visibilities`() {
        val expectedCode1 = "R09L/4000VM5000ft"
        val expectedCode2 = "R08R/P4000ft"

        val rvrDto1 = RunwayVisibilityDto(
            repr = expectedCode1,
            runway = "09L",
            visibility = null,
            variableVisibility = listOf(
                buildVisibilityDto("4000"),
                buildVisibilityDto("M5000")
            ),
            trend = ""
        )

        val rvrDto2 = RunwayVisibilityDto(
            repr = expectedCode2,
            runway = "08R",
            visibility = buildVisibilityDto("P4000"),
            variableVisibility = emptyList(),
            trend = ""
        )

        val rvr = RVRParser.parse(listOf(rvrDto1, rvrDto2), VisibilityUnits.FEET)

        assertThat(rvr).hasSize(2)

        // validate first rvr
        assertThat(rvr[0].code).isEqualTo(expectedCode1)
        assertThat(rvr[0].description).isEqualTo(
            "Runway 09L visual range: varying between 4000 and less than 5000ft"
        )

        // validate second rvr
        assertThat(rvr[1].code).isEqualTo(expectedCode2)
        assertThat(rvr[1].description).isEqualTo(
            "Runway 08R visual range: greater than 4000ft"
        )
    }
}