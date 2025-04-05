package com.josecernu.rickandmortyapp.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.josecernu.rickandmorty.GetCharacterDetailQuery
import com.josecernu.rickandmortyapp.data.NetworkProcess
import com.josecernu.rickandmortyapp.data.Repository
import com.josecernu.rickandmortyapp.data.domain.RickyAndMortyDetailInfo
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
    constructor(private val repository: Repository) : ViewModel() {
        private companion object {
            const val TAG = "CharacterDetailViewModel"
        }

        private val _characterInfo = MutableStateFlow<RickyAndMortyDetailInfo?>(null)
        val characterInfo: StateFlow<RickyAndMortyDetailInfo?> = _characterInfo

        private val _loader = MutableStateFlow(true)
        val loader: StateFlow<Boolean> = _loader
        val isError = MutableSharedFlow<Boolean>()

        init {
            getCharacterDetail()
        }

        private fun getCharacterDetail() {
            viewModelScope.launch(Dispatchers.IO) {
                repository.makeApiCall(GetCharacterDetailQuery("1"))
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
    }
