package com.josecernu.rickandmortyapp.ui.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josecernu.rickandmorty.GetCharacterDetailQuery
import com.josecernu.rickandmortyapp.data.NetworkProcess
import com.josecernu.rickandmortyapp.data.Repository
import com.josecernu.rickandmortyapp.data.dao.FavoriteDao
import com.josecernu.rickandmortyapp.data.domain.RickyAndMortyDetailInfo
import com.josecernu.rickandmortyapp.data.entity.FavoriteEntity
import com.josecernu.rickandmortyapp.data.mapper.toRickyAndMortyDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel
    @Inject
    constructor(
        private val repository: Repository,
        savedStateHandle: SavedStateHandle,
        private val favoriteDao: FavoriteDao,
    ) : ViewModel() {
        private companion object {
            const val TAG = "CharacterDetailViewModel"
            const val CHARACTER_ID_KEY = "id"
        }

        private val characterId: String = savedStateHandle.get<String>(CHARACTER_ID_KEY) ?: "0"

        private val _characterInfo = MutableStateFlow<RickyAndMortyDetailInfo?>(null)
        val characterInfo: StateFlow<RickyAndMortyDetailInfo?> = _characterInfo
        private val _favorites = mutableListOf<String>()
        val favorites = _favorites

        private val _loader = MutableStateFlow(true)
        val loader: StateFlow<Boolean> = _loader
        val isError = MutableSharedFlow<Boolean>()

        init {
            viewModelScope.launch {
                _favorites.addAll(favoriteDao.getAllFavorites().map { it.characterId })
            }
            getCharacterDetail()
        }

        private fun getCharacterDetail() {
            viewModelScope.launch(Dispatchers.IO) {
                repository.makeApiCall(GetCharacterDetailQuery(characterId))
                    .collectLatest { result ->
                        when (result) {
                            is NetworkProcess.Failure -> {
                                _loader.value = false
                                isError.emit(true)
                                Log.d(TAG, "ErrorCode: ${result.errorCode} Message: ${result.message} Data: ${result.data}")
                            }
                            NetworkProcess.Loading -> {
                                _loader.value = true
                                Log.d(TAG, "Loading")
                            }
                            is NetworkProcess.Success -> {
                                Log.d(TAG, "Success")
                                val detail = result.data?.character?.toRickyAndMortyDetail()
                                _characterInfo.value = detail
                                _loader.value = false
                            }
                        }
                    }
            }
        }

        fun isFavorite(characterId: String): Boolean {
            return _favorites.contains(characterId)
        }

        fun onClickFavorite(characterId: String) {
            viewModelScope.launch {
                if (isFavorite(characterId)) {
                    favoriteDao.removeFavorite(FavoriteEntity(characterId))
                } else {
                    favoriteDao.addFavorite(FavoriteEntity(characterId))
                }
            }
            updateFavorites()
        }

        private fun updateFavorites() {
            viewModelScope.launch {
                _favorites.addAll(favoriteDao.getAllFavorites().map { it.characterId })
            }
        }
    }
