
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.josecernu.rickandmortyapp.R
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
    val characterList by viewModel.characterList.collectAsState()
    val isLoading by viewModel.loader.collectAsState(true)

    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rick And Morty") })
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                HomeSkeletonScreen()
            } else {
                if (characterList.isEmpty()) {
                    ErrorOrEmptyState(
                        title = stringResource(R.string.emtpy_state_title),
                        description = stringResource(R.string.empty_state_description),
                    )
                } else {
                    LazyColumn(
                        state = scrollState,
                    ) {
                        items(characterList) { item ->
                            CharacterCard(
                                name = item.name,
                                origin = "",
                                gender = item.gender,
                                pictureUrl = item.image,
                            )
                        }
                    }
                }
            }
        }
    }
}
