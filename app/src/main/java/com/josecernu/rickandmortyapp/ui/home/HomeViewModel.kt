package com.josecernu.rickandmortyapp.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.josecernu.rickandmorty.RickAndMortyQuery
import com.josecernu.rickandmortyapp.data.NetworkProcess
import com.josecernu.rickandmortyapp.data.Repository
import com.josecernu.rickandmortyapp.data.mapper.toRickyAndMortyCharacter
import com.josecernu.rickandmortyapp.data.model.RickyAndMortyCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(private val repository: Repository) : ViewModel() {
        private companion object {
            const val TAG = "HomeViewModel"
        }

        val charactersList = MutableSharedFlow<List<RickyAndMortyCharacter?>>()
        val loader = MutableSharedFlow<Boolean>()

        init {
            getCharacters()
        }

        private fun getCharacters() {
            CoroutineScope(Dispatchers.IO).launch {
                loader.emit(true)
                repository.makeApiCall(RickAndMortyQuery()).collect { it ->
                    when (it) {
                        is NetworkProcess.Success -> {
                            Log.d(TAG, "Success")
                            loader.emit(false)
                            val list = it.data?.characters?.results?.map { it?.toRickyAndMortyCharacter() }
                            charactersList.emit(list ?: emptyList())
                        }
                        is NetworkProcess.Failure -> {
                            Log.d(TAG, "ErrorCode: ${it.errorCode} Message: ${it.message} Data: ${it.data}")
                        }

                        is NetworkProcess.Loading -> {
                            Log.d(TAG, "Loading")
                        }
                    }
                }
            }
        }
    }
