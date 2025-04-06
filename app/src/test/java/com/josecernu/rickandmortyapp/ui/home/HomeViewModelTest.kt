package com.josecernu.rickandmortyapp.ui.home

import android.util.Log
import com.apollographql.apollo3.api.Optional
import com.josecernu.rickandmorty.RickAndMortyQuery
import com.josecernu.rickandmortyapp.data.NetworkProcess
import com.josecernu.rickandmortyapp.data.Repository
import com.josecernu.rickandmortyapp.data.dao.FavoriteDao
import com.josecernu.rickandmortyapp.data.domain.RickyAndMortyBasicInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @MockK
    private lateinit var repository: Repository

    @MockK
    private lateinit var favoriteDao: FavoriteDao

    private lateinit var viewModel: HomeViewModel
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        viewModel = HomeViewModel(repository, favoriteDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCharacters of the first page with loading`() =
        runTest {
            coEvery { repository.makeApiCall(RickAndMortyQuery(page = Optional.Present(1))) } returns flowOf(NetworkProcess.Loading)

            viewModel.getCharacters()

            TestCase.assertTrue(viewModel.loader.value)
            TestCase.assertFalse(viewModel.loaderMore.value)
            TestCase.assertFalse(viewModel.isError.replayCache.firstOrNull() ?: false)
            TestCase.assertEquals(emptyList<RickyAndMortyBasicInfo>(), viewModel.characterList.value)
            coVerify(exactly = 1) { repository.makeApiCall(RickAndMortyQuery(page = Optional.Present(1))) }
        }

    @Test
    fun `getCharacters of the first page with failure`() =
        runTest {
            coEvery { repository.makeApiCall(RickAndMortyQuery(page = Optional.Present(1))) } returns flowOf(NetworkProcess.Failure(""))

            viewModel.getCharacters()

            TestCase.assertFalse(viewModel.loaderMore.value)
            TestCase.assertFalse(viewModel.isError.replayCache.firstOrNull() ?: false)
            TestCase.assertEquals(emptyList<RickyAndMortyBasicInfo>(), viewModel.characterList.value)
            coVerify(exactly = 1) { repository.makeApiCall(RickAndMortyQuery(page = Optional.Present(1))) }
        }

    @Test
    fun `getCharacters - first page - success`() =
        runTest {
            val mockCharacter =
                mockk<RickAndMortyQuery.Result> {
                    every { id } returns "1"
                    every { name } returns "Rick Sanchez"
                    every { origin } returns
                        mockk {
                            every { name } returns "Origin name"
                        }
                    every { gender } returns "male"
                    every { image } returns "url"
                }
            val mockResponse =
                mockk<RickAndMortyQuery.Data> {
                    every { characters?.results } returns listOf(mockCharacter)
                }
            coEvery {
                repository.makeApiCall(RickAndMortyQuery(page = Optional.Present(1)))
            } returns flowOf(NetworkProcess.Success(mockResponse))

            viewModel.getCharacters()
            advanceUntilIdle()

            TestCase.assertFalse(viewModel.loaderMore.value)
            TestCase.assertFalse(viewModel.isError.replayCache.firstOrNull() ?: false)
            // TestCase.assertEquals(listOf(mockCharacter.toRickyAndMortyCharacter()), viewModel.characterList.value)
            coVerify(exactly = 1) { repository.makeApiCall(RickAndMortyQuery(page = Optional.Present(1))) }
        }
}
