package com.josecernu.rickandmortyapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.josecernu.rickandmortyapp.R
import com.josecernu.rickandmortyapp.ui.theme.RickAndMortyAppTheme
import java.util.Locale

@Composable
@SuppressWarnings("kotlin:S100", "kotlin:S107")
fun ErrorOrEmptyState(
    title: String = stringResource(R.string.error_title).uppercase(Locale.getDefault()),
    description: String = stringResource(R.string.error_description).uppercase(Locale.getDefault()),
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = description, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Preview
@Composable
fun ErrorStatePreview() {
    RickAndMortyAppTheme {
        ErrorOrEmptyState(
            title = "Title of the error state",
            description = "description of the error state",
        )
    }
}
