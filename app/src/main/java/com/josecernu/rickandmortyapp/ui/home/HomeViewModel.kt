package com.josecernu.rickandmortyapp.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor() : ViewModel() {
        private val TAG = HomeViewModel::class.java.simpleName
    }
