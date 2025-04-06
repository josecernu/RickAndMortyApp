package com.josecernu.rickandmortyapp.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.josecernu.rickandmortyapp.data.Repository
import com.josecernu.rickandmortyapp.data.dao.FavoriteDao
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

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
        savedStateHandle = SavedStateHandle(mapOf("id" to "1"))
        coEvery { favoriteDao.getAllFavorites() } returns emptyList()
        viewModel = CharacterDetailViewModel(repository, savedStateHandle, favoriteDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
