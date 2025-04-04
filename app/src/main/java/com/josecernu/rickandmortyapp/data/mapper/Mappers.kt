package com.josecernu.rickandmortyapp.data.mapper

import com.josecernu.rickandmorty.RickAndMortyQuery
import com.josecernu.rickandmortyapp.data.model.RickyAndMortyCharacter

fun RickAndMortyQuery.Result.toRickyAndMortyCharacter(): RickyAndMortyCharacter {
    return RickyAndMortyCharacter(
        id = id.orEmpty(),
        name = name.orEmpty(),
        gender = gender.orEmpty(),
        status = status.orEmpty(),
        species = species.orEmpty(),
        image = image.orEmpty(),
    )
}
