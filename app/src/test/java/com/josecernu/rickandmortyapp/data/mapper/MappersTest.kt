package com.josecernu.rickandmortyapp.data.mapper

import com.josecernu.rickandmorty.GetCharacterDetailQuery
import com.josecernu.rickandmorty.RickAndMortyQuery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MappersTest {
    @Test
    fun `mapRickyAndMortyCharacter with null values should return RickyAndMortyBasicInfo with values`() {
        val resultMock =
            mockk<RickAndMortyQuery.Result> {
                every { id } returns null
                every { name } returns null
                every { origin } returns null
                every { gender } returns null
                every { image } returns null
            }

        val result = resultMock.toRickyAndMortyCharacter()

        assertEquals("", result.id)
        assertEquals("", result.name)
        assertEquals("", result.originName)
        assertEquals("", result.gender)
        assertEquals("", result.image)
    }

    @Test
    fun `mapRickyAndMortyCharacter with not null values should return RickyAndMortyBasicInfo with values`() {
        val originMock =
            mockk<RickAndMortyQuery.Origin> {
                every { name } returns "Earth"
            }

        val resultMock =
            mockk<RickAndMortyQuery.Result> {
                every { id } returns "1"
                every { name } returns "Rick"
                every { origin } returns originMock
                every { gender } returns "Male"
                every { image } returns "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
            }

        val result = resultMock.toRickyAndMortyCharacter()

        assertEquals("1", result.id)
        assertEquals("Rick", result.name)
        assertEquals("Earth", result.originName)
        assertEquals("Male", result.gender)
        assertEquals("https://rickandmortyapi.com/api/character/avatar/1.jpeg", result.image)
    }

    @Test
    fun `mapRickyAndMortyCharacterDetail with null values should return RickyAndMortyDetailInfo with values`() {
        val resultMock =
            mockk<GetCharacterDetailQuery.Character> {
                every { id } returns null
                every { name } returns null
                every { origin } returns null
                every { gender } returns null
                every { image } returns null
                every { status } returns null
                every { species } returns null
                every { location } returns null
                every { created } returns null
            }

        val result = resultMock.toRickyAndMortyDetail()

        with(result.basicInfo) {
            assertEquals("", id)
            assertEquals("", name)
            assertEquals("", originName)
            assertEquals("", gender)
            assertEquals("", image)
        }

        assertEquals("", result.status)
        assertEquals("", result.specie)
        assertEquals("", result.locationName)
        assertEquals("", result.creationDate)
    }

    @Test
    fun `mapRickyAndMortyCharacterDetail with not null values should return RickyAndMortyDetailInfo with values`() {
        val originMock =
            mockk<GetCharacterDetailQuery.Origin> {
                every { name } returns "Earth"
            }

        val locationMock =
            mockk<GetCharacterDetailQuery.Location> {
                every { name } returns "Citadel of Ricks"
            }

        val resultMock =
            mockk<GetCharacterDetailQuery.Character> {
                every { id } returns "1"
                every { name } returns "Rick"
                every { origin } returns originMock
                every { gender } returns "Male"
                every { image } returns "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
                every { status } returns "Alive"
                every { species } returns "Human"
                every { location } returns locationMock
                every { created } returns "2017-11-04T18:48:46.250Z"
            }

        val result = resultMock.toRickyAndMortyDetail()

        with(result.basicInfo) {
            assertEquals("1", id)
            assertEquals("Rick", name)
            assertEquals("Earth", originName)
            assertEquals("Male", gender)
            assertEquals("https://rickandmortyapi.com/api/character/avatar/1.jpeg", image)
        }

        assertEquals("Alive", result.status)
        assertEquals("Human", result.specie)
        assertEquals("Citadel of Ricks", result.locationName)
        assertEquals("2017-11-04T18:48:46.250Z", result.creationDate)
    }
}
