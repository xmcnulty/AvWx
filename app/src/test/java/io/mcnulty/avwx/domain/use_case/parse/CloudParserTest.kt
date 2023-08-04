package io.mcnulty.avwx.domain.use_case.parse

import com.google.common.truth.Truth.assertThat
import io.mcnulty.avwx.data.remote.dto.metar.CloudDto
import io.mcnulty.avwx.domain.model.metar.fields.Clouds
import org.junit.Test

class CloudParserTest {

    @Test
    fun `test empty dto list`() {
        val clouds = CloudParser.parse(emptyList())

        assertThat(clouds).isEmpty()
    }

    @Test
    fun `parse cloud with no modifier`() {
        val dto = listOf(
            CloudDto(
                altitude = 22,
                modifier = "",
                repr = "SCT022",
                type = "SCT"
            )
        )

        val clouds = CloudParser.parse(dto)

        assertThat(clouds).hasSize(1)
        assertThat(clouds[0].coverage).isEqualTo(Clouds.Coverage.SCATTERED)
        assertThat(clouds[0].altitude).isEqualTo(2200)
        assertThat(clouds[0].type).isNull()
        assertThat(clouds[0].code).isEqualTo("SCT022")
    }

    @Test
    fun `parse cloud with type`() {
        val dto = listOf(
            CloudDto(
                altitude = 22,
                modifier = "CB",
                repr = "SCT022CB",
                type = "SCT"
            )
        )

        val clouds = CloudParser.parse(dto)

        assertThat(clouds).hasSize(1)
        assertThat(clouds[0].coverage).isEqualTo(Clouds.Coverage.SCATTERED)
        assertThat(clouds[0].altitude).isEqualTo(2200)
        assertThat(clouds[0].type).isEqualTo(Clouds.Type.CUMULONIMBUS)
        assertThat(clouds[0].code).isEqualTo("SCT022CB")
    }

    @Test
    fun `test multiple cloud layers`() {
        val dto = listOf(
            CloudDto(
                altitude = 22,
                modifier = "CB",
                repr = "SCT022CB",
                type = "SCT"
            ),
            CloudDto(
                altitude = 50,
                modifier = "TCU",
                repr = "BKN050TCU",
                type = "BKN"
            )
        )

        val clouds = CloudParser.parse(dto)

        assertThat(clouds).hasSize(2)
        assertThat(clouds[0].coverage).isEqualTo(Clouds.Coverage.SCATTERED)
        assertThat(clouds[0].altitude).isEqualTo(2200)
        assertThat(clouds[0].type).isEqualTo(Clouds.Type.CUMULONIMBUS)
        assertThat(clouds[0].code).isEqualTo("SCT022CB")
        assertThat(clouds[1].coverage).isEqualTo(Clouds.Coverage.BROKEN)
        assertThat(clouds[1].altitude).isEqualTo(5000)
        assertThat(clouds[1].type).isEqualTo(Clouds.Type.TOWERING_CUMULUS)
        assertThat(clouds[1].code).isEqualTo("BKN050TCU")
    }

    @Test
    fun `test vertical visibility`() {
        val dto = listOf(
            CloudDto(
                altitude = 50,
                modifier = "",
                repr = "VV050",
                type = "VV"
            )
        )

        val clouds = CloudParser.parse(dto)

        assertThat(clouds).hasSize(1)
        assertThat(clouds[0].coverage).isEqualTo(Clouds.Coverage.VERTICAL_VISIBILITY)
        assertThat(clouds[0].altitude).isEqualTo(5000)
        assertThat(clouds[0].type).isNull()
        assertThat(clouds[0].code).isEqualTo("VV050")
    }
}