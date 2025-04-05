
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.josecernu.rickandmortyapp.R
import com.josecernu.rickandmortyapp.data.domain.RickyAndMortyBasicInfo
import com.josecernu.rickandmortyapp.navigation.Destination
import com.josecernu.rickandmortyapp.ui.component.CharacterCard
import com.josecernu.rickandmortyapp.ui.component.ErrorOrEmptyState
import com.josecernu.rickandmortyapp.ui.home.HomeSkeletonScreen
import com.josecernu.rickandmortyapp.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val currentEntry = navController.currentBackStackEntryAsState()

    LaunchedEffect(currentEntry.value) {
        viewModel.refreshData()
    }

    val characterList by viewModel.characterList.collectAsState()
    val isLoading by viewModel.loader.collectAsState()
    val isLoadingMore by viewModel.loaderMore.collectAsState()
    val isError by viewModel.isError.collectAsState(false)
    val favorites by viewModel.favorites.collectAsState()

    val listState = rememberLazyListState()

    val shouldLoadMore =
        remember {
            derivedStateOf {
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val totalItemsCount = listState.layoutInfo.totalItemsCount
                lastVisibleItemIndex != null && lastVisibleItemIndex >= totalItemsCount - 5
            }
        }

    LaunchedEffect(shouldLoadMore.value) {
        viewModel.getCharacters()
        if (shouldLoadMore.value) {
            viewModel.getCharacters()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                HomeSkeletonScreen()
            } else {
                if (isError) {
                    ErrorOrEmptyState(
                        title = stringResource(R.string.error_title),
                        description = stringResource(R.string.error_description),
                    )
                } else {
                    if (characterList.isEmpty()) {
                        ErrorOrEmptyState(
                            title = stringResource(R.string.emtpy_state_title),
                            description = stringResource(R.string.empty_state_description),
                        )
                    } else {
                        CharacterListScreen(
                            characterList,
                            navController,
                            listState,
                            isLoadingMore,
                            favorites,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    characterList: List<RickyAndMortyBasicInfo>,
    navController: NavHostController,
    listState: LazyListState,
    showLoader: Boolean = false,
    favorites: Set<String>,
) {
    val options = listOf("All", "Male", "Female", "Unknown")
    var expanded by remember { mutableStateOf(false) }
    var optionSelected by remember { mutableStateOf("All") }

    val filteredList =
        characterList.filter {
            optionSelected == "All" || it.gender.equals(optionSelected, ignoreCase = true)
        }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                value = optionSelected,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.filter_by_gender)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier =
                    Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender) },
                        onClick = {
                            optionSelected = gender
                            expanded = false
                        },
                    )
                }
            }
        }
        LazyColumn(
            state = listState,
        ) {
            items(filteredList) { item ->
                CharacterCard(
                    name = item.name,
                    origin = item.originName,
                    gender = item.gender,
                    pictureUrl = item.image,
                    isFavorite = favorites.contains(item.id),
                ) {
                    navController.navigate(Destination.Detail.route + "/${item.id}")
                }
            }
            if (showLoader) {
                item {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
