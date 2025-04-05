package com.josecernu.rickandmortyapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import com.josecernu.rickandmorty.RickAndMortyQuery
import com.josecernu.rickandmortyapp.data.NetworkProcess
import com.josecernu.rickandmortyapp.data.Repository
import com.josecernu.rickandmortyapp.data.dao.FavoriteDao
import com.josecernu.rickandmortyapp.data.domain.RickyAndMortyBasicInfo
import com.josecernu.rickandmortyapp.data.mapper.toRickyAndMortyCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(private val repository: Repository, private val favoriteDao: FavoriteDao) : ViewModel() {
        private companion object {
            const val TAG = "HomeViewModel"
        }

        private val _characterList = MutableStateFlow<List<RickyAndMortyBasicInfo>>(emptyList())
        val characterList: StateFlow<List<RickyAndMortyBasicInfo>> = _characterList
        private val _favorites = MutableStateFlow<Set<String>>(emptySet())
        val favorites: StateFlow<Set<String>> get() = _favorites
        private val _loader = MutableStateFlow(true)
        val loader: StateFlow<Boolean> = _loader
        private val _loaderMore = MutableStateFlow(false)
        val loaderMore: StateFlow<Boolean> = _loaderMore
        val isError = MutableSharedFlow<Boolean>()

        private var currentPage = 1
        private var isLoadingMore = false
        private var isLastPage = false

        init {
            refreshData()
        }

        fun getCharacters() {
            if (isLoadingMore || isLastPage) return

            viewModelScope.launch(Dispatchers.IO) {
                isLoadingMore = true
                repository.makeApiCall(RickAndMortyQuery(page = Optional.Present(currentPage)))
                    .collectLatest { result ->
                        when (result) {
                            is NetworkProcess.Failure -> {
                                _loader.value = false
                                _loaderMore.value = false
                                isError.emit(true)
                                isLoadingMore = false
                                Log.d(TAG, "ErrorCode: ${result.errorCode} Message: ${result.message} Data: ${result.data}")
                            }
                            NetworkProcess.Loading -> {
                                if (currentPage == 1) {
                                    _loader.value = true
                                    Log.d(TAG, "Loading first page")
                                } else {
                                    _loaderMore.value = true
                                    Log.d(TAG, "Loading pagination")
                                }
                            }
                            is NetworkProcess.Success -> {
                                Log.d(TAG, "Success")
                                val newList = result.data?.characters?.results?.mapNotNull { it?.toRickyAndMortyCharacter() } ?: emptyList()
                                if (newList.isEmpty()) {
                                    isLastPage = true
                                } else {
                                    _characterList.value += newList
                                    currentPage++
                                }
                                _loader.value = false
                                _loaderMore.value = false
                                isLoadingMore = false
                            }
                        }
                    }
            }
        }

        fun refreshData() {
            viewModelScope.launch {
                val favoriteIds = favoriteDao.getAllFavorites().map { it.characterId }
                _favorites.value = favoriteIds.toSet()
            }
        }
    }
