package com.josecernu.rickandmortyapp.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.josecernu.rickandmortyapp.R
import com.josecernu.rickandmortyapp.data.domain.RickyAndMortyBasicInfo
import com.josecernu.rickandmortyapp.data.domain.RickyAndMortyDetailInfo
import com.josecernu.rickandmortyapp.data.domain.util.extension.capitalizeDate
import com.josecernu.rickandmortyapp.data.domain.util.extension.toDateFormat
import com.josecernu.rickandmortyapp.ui.component.skeleton.shimmerEffect
import com.josecernu.rickandmortyapp.ui.theme.RickAndMortyAppTheme

@Composable
fun CharacterDetail(
    characterInfo: RickyAndMortyDetailInfo,
    onFavClick: (Boolean) -> Unit,
) {
    var isFavorite by remember { mutableStateOf(characterInfo.basicInfo.isFavorite) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = characterInfo.basicInfo.image,
                contentDescription = "Character picture",
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .height(300.dp)
                        .fillMaxWidth(),
            )

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                    onFavClick(isFavorite)
                },
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape),
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite icon",
                    tint = if (isFavorite) Color.Red else Color.Gray,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            DetailRow(label = stringResource(R.string.character_origin), value = characterInfo.basicInfo.originName)
            DetailRow(label = stringResource(R.string.character_gender), value = characterInfo.basicInfo.gender)
            DetailRow(label = stringResource(R.string.character_status), value = characterInfo.status)
            DetailRow(label = stringResource(R.string.character_specie), value = characterInfo.specie)
            DetailRow(label = stringResource(R.string.character_location), value = characterInfo.locationName)
            DetailRow(
                label = stringResource(R.string.character_creation_date),
                value = characterInfo.creationDate.toDateFormat().capitalizeDate(),
            )
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = value,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun CharacterDetailSkeleton() {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier =
                    Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                        .shimmerEffect(),
            )

            Box(
                modifier =
                    Modifier
                        .padding(16.dp)
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                        .align(Alignment.TopEnd)
                        .shimmerEffect(),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            repeat(6) {
                DetailRowSkeleton()
            }
        }
    }
}

@Composable
fun DetailRowSkeleton() {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        Box(
            modifier =
                Modifier
                    .width(100.dp)
                    .height(MaterialTheme.typography.bodyLarge.fontSize.value.dp)
                    .shimmerEffect(),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(1f)
                    .height(MaterialTheme.typography.bodyLarge.fontSize.value.dp)
                    .shimmerEffect(),
        )
    }
}

@Preview
@Composable
fun CharacterDetailPreview() {
    RickAndMortyAppTheme {
        val itemInfo =
            RickyAndMortyDetailInfo(
                basicInfo =
                    RickyAndMortyBasicInfo(
                        id = "1",
                        name = "Rick Sanchez",
                        originName = "Earth",
                        gender = "Male",
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                        isFavorite = true,
                    ),
                status = "Alive",
                specie = "Human",
                locationName = "Citadel of Ricks",
                creationDate = "2017-11-04T18:48:46.250Z",
            )
        CharacterDetail(
            characterInfo = itemInfo,
        ) { }
    }
}

@Preview
@Composable
fun CharacterDetailSkeletonPreview() {
    CharacterDetailSkeleton()
}
