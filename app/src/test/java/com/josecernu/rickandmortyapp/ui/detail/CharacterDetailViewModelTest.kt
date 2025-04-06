package com.josecernu.rickandmortyapp.ui.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.josecernu.rickandmorty.GetCharacterDetailQuery
import com.josecernu.rickandmortyapp.data.NetworkProcess
import com.josecernu.rickandmortyapp.data.Repository
import com.josecernu.rickandmortyapp.data.dao.FavoriteDao
import com.josecernu.rickandmortyapp.data.entity.FavoriteEntity
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
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
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest {
    @MockK
    private lateinit var repository: Repository

    @MockK
    private lateinit var favoriteDao: FavoriteDao

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: CharacterDetailViewModel
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 2
        savedStateHandle = SavedStateHandle(mapOf("id" to "1"))
        coEvery { favoriteDao.getAllFavorites() } returns emptyList()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCharacterDetail - failure`() =
        runTest {
            coEvery { repository.makeApiCall(GetCharacterDetailQuery("1")) } returns
                flowOf(
                    NetworkProcess.Failure(""),
                )

            viewModel = CharacterDetailViewModel(repository, savedStateHandle, favoriteDao)
            advanceUntilIdle()

            Assert.assertNull(viewModel.characterInfo.value)
            coVerify(exactly = 1) { repository.makeApiCall(GetCharacterDetailQuery("1")) }
        }

    @Test
    fun `getCharacterDetail - progress`() =
        runTest {
            coEvery { repository.makeApiCall(GetCharacterDetailQuery("1")) } returns
                flowOf(
                    NetworkProcess.Loading,
                )

            viewModel = CharacterDetailViewModel(repository, savedStateHandle, favoriteDao)
            advanceUntilIdle()

            Assert.assertNull(viewModel.characterInfo.value)
            coVerify(exactly = 1) { repository.makeApiCall(GetCharacterDetailQuery("1")) }
        }

    @Test
    fun `getCharacterDetail - success`() =
        runTest {
            val savedStateHandle = SavedStateHandle(mapOf("id" to "1"))

            val mockCharacterDetail =
                mockk<GetCharacterDetailQuery.Character> {
                    every { id } returns "id"
                    every { name } returns "name"
                    every { origin } returns mockk { every { name } returns "originName" }
                    every { gender } returns "gender"
                    every { image } returns "image"
                    every { status } returns "status"
                    every { species } returns "specie"
                    every { location } returns mockk { every { name } returns "locationName" }
                    every { created } returns "date"
                }

            val mockData =
                mockk<GetCharacterDetailQuery.Data> {
                    every { character } returns mockCharacterDetail
                }

            coEvery { repository.makeApiCall(GetCharacterDetailQuery("1")) } returns
                flowOf(
                    NetworkProcess.Success(mockData),
                )
            coEvery { favoriteDao.getAllFavorites() } returns emptyList()

            viewModel = CharacterDetailViewModel(repository, savedStateHandle, favoriteDao)
            advanceUntilIdle()

            // Assert.assertNotNull(viewModel.characterInfo.value)
            coVerify(exactly = 1) { repository.makeApiCall(GetCharacterDetailQuery("1")) }
        }

    @Test
    fun `isFavorite when list contains this item should return true`() =
        runTest {
            coEvery { favoriteDao.getAllFavorites() } returns listOf(FavoriteEntity("2"))

            coEvery { repository.makeApiCall(GetCharacterDetailQuery("1")) } returns
                flowOf(
                    NetworkProcess.Success(mockk()),
                )

            viewModel = CharacterDetailViewModel(repository, savedStateHandle, favoriteDao)
            advanceUntilIdle()

            val result = viewModel.isFavorite("2")

            Assert.assertTrue(result)
        }

    @Test
    fun `isFavorite when list no contains this item should return false`() =
        runTest {
            coEvery { favoriteDao.getAllFavorites() } returns listOf(FavoriteEntity("2"), FavoriteEntity("3"))

            coEvery { repository.makeApiCall(GetCharacterDetailQuery("1")) } returns
                flowOf(
                    NetworkProcess.Success(mockk()),
                )

            viewModel = CharacterDetailViewModel(repository, savedStateHandle, favoriteDao)
            advanceUntilIdle()

            val result = viewModel.isFavorite("1")

            Assert.assertFalse(result)
        }

    @Test
    fun `onClickFavorite removes character if already favorite`() =
        runTest {
            val characterId = "1"
            coEvery { favoriteDao.getAllFavorites() } returns listOf(FavoriteEntity(characterId))
            coEvery { favoriteDao.removeFavorite(any()) } just Runs

            coEvery { repository.makeApiCall(GetCharacterDetailQuery("1")) } returns
                flowOf(
                    NetworkProcess.Success(mockk()),
                )

            viewModel = CharacterDetailViewModel(repository, savedStateHandle, favoriteDao)
            advanceUntilIdle()

            viewModel.onClickFavorite(characterId)
            advanceUntilIdle()

            coVerify { favoriteDao.removeFavorite(FavoriteEntity(characterId)) }
        }

    @Test
    fun `onClickFavorite adds character if not favorite`() =
        runTest {
            val characterId = "2"
            coEvery { favoriteDao.getAllFavorites() } returns listOf(FavoriteEntity("1"))
            coEvery { favoriteDao.addFavorite(any()) } just Runs

            coEvery { repository.makeApiCall(GetCharacterDetailQuery("1")) } returns
                flowOf(
                    NetworkProcess.Success(mockk()),
                )

            viewModel = CharacterDetailViewModel(repository, savedStateHandle, favoriteDao)
            advanceUntilIdle()

            viewModel.onClickFavorite(characterId)
            advanceUntilIdle()

            coVerify { favoriteDao.addFavorite(FavoriteEntity(characterId)) }
        }
}
