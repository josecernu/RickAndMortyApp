package com.josecernu.rickandmortyapp.data.mapper

import com.josecernu.rickandmorty.GetCharacterDetailQuery
import com.josecernu.rickandmorty.RickAndMortyQuery
import com.josecernu.rickandmortyapp.data.domain.RickyAndMortyBasicInfo
import com.josecernu.rickandmortyapp.data.domain.RickyAndMortyDetailInfo

fun RickAndMortyQuery.Result.toRickyAndMortyCharacter(): RickyAndMortyBasicInfo {
    return RickyAndMortyBasicInfo(
        id = id.orEmpty(),
        name = name.orEmpty(),
        originName = origin?.name.orEmpty(),
        gender = gender.orEmpty(),
        image = image.orEmpty(),
    )
}

fun GetCharacterDetailQuery.Character.toRickyAndMortyDetail(): RickyAndMortyDetailInfo {
    return RickyAndMortyDetailInfo(
        basicInfo =
            RickyAndMortyBasicInfo(
                id = id.orEmpty(),
                name = name.orEmpty(),
                originName = origin?.name.orEmpty(),
                gender = gender.orEmpty(),
                image = image.orEmpty(),
            ),
        status = status.orEmpty(),
        specie = species.orEmpty(),
        locationName = location?.name.orEmpty(),
        creationDate = created.orEmpty(),
    )
}
