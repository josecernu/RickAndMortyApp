package com.josecernu.rickandmortyapp.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.josecernu.rickandmortyapp.ui.component.CharacterCardSkeleton

@Composable
fun HomeSkeletonScreen() {
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 10.dp)) {
        repeat(10) {
            CharacterCardSkeleton()
        }
    }
}
