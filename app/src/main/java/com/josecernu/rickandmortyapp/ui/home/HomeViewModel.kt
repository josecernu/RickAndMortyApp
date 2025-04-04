package com.josecernu.rickandmortyapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josecernu.rickandmorty.RickAndMortyQuery
import com.josecernu.rickandmortyapp.data.NetworkProcess
import com.josecernu.rickandmortyapp.data.Repository
import com.josecernu.rickandmortyapp.data.mapper.toRickyAndMortyCharacter
import com.josecernu.rickandmortyapp.data.model.RickyAndMortyCharacter
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
    constructor(private val repository: Repository) : ViewModel() {
        private companion object {
            const val TAG = "HomeViewModel"
        }

        private val _characterList = MutableStateFlow<List<RickyAndMortyCharacter>>(emptyList())
        val characterList: StateFlow<List<RickyAndMortyCharacter>> = _characterList

        private val _loader = MutableStateFlow(true)
        val loader: StateFlow<Boolean> = _loader
        val isError = MutableSharedFlow<Boolean>()

        init {
            getCharacters()
        }

        private fun getCharacters() {
            viewModelScope.launch(Dispatchers.IO) {
                repository.makeApiCall(RickAndMortyQuery())
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
                                val list = result.data?.characters?.results?.mapNotNull { it?.toRickyAndMortyCharacter() } ?: emptyList()
                                _characterList.value = list
                                _loader.value = false
                            }
                        }
                    }
            }
        }
    }
