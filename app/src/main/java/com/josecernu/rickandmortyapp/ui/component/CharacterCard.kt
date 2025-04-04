package com.josecernu.rickandmortyapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.josecernu.rickandmortyapp.R
import com.josecernu.rickandmortyapp.ui.component.skeleton.shimmerEffect
import com.josecernu.rickandmortyapp.ui.theme.RickAndMortyAppTheme

@Composable
fun CharacterCard(
    name: String,
    origin: String,
    gender: String,
    pictureUrl: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberAsyncImagePainter(pictureUrl),
            contentDescription = "Image of $name",
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(64.dp)
                    .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(text = name, style = MaterialTheme.typography.titleMedium)
            Text(text = "${stringResource(R.string.character_origin)}: $origin", style = MaterialTheme.typography.bodyMedium)
            Text(text = "${stringResource(R.string.character_gender)}: $gender", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun CharacterCardSkeleton()  {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .shimmerEffect(),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(MaterialTheme.typography.titleMedium.fontSize.value.dp)
                        .shimmerEffect(),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.4f)
                        .height(MaterialTheme.typography.bodyMedium.fontSize.value.dp)
                        .shimmerEffect(),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .height(MaterialTheme.typography.bodyMedium.fontSize.value.dp)
                        .shimmerEffect(),
            )
        }
    }
}

@Preview
@Composable
fun CharacterCardPreview() {
    RickAndMortyAppTheme {
        CharacterCard(
            name = "Rick Sanchez",
            origin = "Earth (C-137)",
            gender = "Male",
            pictureUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        ) {}
    }
}

@Preview
@Composable
fun CharacterCardSkeletonPreview()  {
    CharacterCardSkeleton()
}
