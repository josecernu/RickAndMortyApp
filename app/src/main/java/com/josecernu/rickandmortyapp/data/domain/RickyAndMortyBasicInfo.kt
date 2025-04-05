package com.josecernu.rickandmortyapp.data.domain

data class RickyAndMortyBasicInfo(
    val id: String,
    val name: String,
    val originName: String,
    val gender: String,
    val image: String,
    val isFavorite: Boolean = false,
)
