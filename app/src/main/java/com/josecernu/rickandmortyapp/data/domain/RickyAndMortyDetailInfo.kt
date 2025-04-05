package com.josecernu.rickandmortyapp.data.domain

data class RickyAndMortyDetailInfo(
    val basicInfo: RickyAndMortyBasicInfo,
    val status: String,
    val specie: String,
    val locationName: String,
    val creationDate: String,
)
