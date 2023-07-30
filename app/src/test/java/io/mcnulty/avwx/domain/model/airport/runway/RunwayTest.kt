package io.mcnulty.avwx.domain.model.airport.runway

import org.junit.Assert.*
import org.junit.Test

/**
 * Comprehensive unit tests for [Runway].
 */
class RunwayTest {

        @Test
        fun testCodeWithoutPosition() {
            val runway = Runway(9)
            assertEquals("09", runway.code)
            assertEquals("Runway 9", runway.description)
        }

        @Test
        fun testCodeWithPosition() {
            val runway = Runway(9, RunwayPosition.LEFT)
            assertEquals("09L", runway.code)
            assertEquals("Runway 9 Left", runway.description)
        }

        @Test
        fun testCodeWithPositionCenter() {
            val runway = Runway(9, RunwayPosition.CENTER)
            assertEquals("09C", runway.code)
            assertEquals("Runway 9 Center", runway.description)
        }

        @Test
        fun testCodeWithPositionRight() {
            val runway = Runway(9, RunwayPosition.RIGHT)
            assertEquals("09R", runway.code)
            assertEquals("Runway 9 Right", runway.description)
        }

       @Test(expected = IllegalArgumentException::class)
       fun testInvalidNumber() {
           Runway(0)
       }
}