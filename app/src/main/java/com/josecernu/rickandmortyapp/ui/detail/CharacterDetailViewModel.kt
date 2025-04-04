package com.josecernu.rickandmortyapp.ui.detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel
    @Inject
    constructor() : ViewModel() {
        private val TAG = CharacterDetailViewModel::class.java.simpleName
    }
