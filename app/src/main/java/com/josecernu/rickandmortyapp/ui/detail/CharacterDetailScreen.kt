
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.josecernu.rickandmortyapp.R
import com.josecernu.rickandmortyapp.ui.component.CharacterDetail
import com.josecernu.rickandmortyapp.ui.component.CharacterDetailSkeleton
import com.josecernu.rickandmortyapp.ui.component.ErrorOrEmptyState
import com.josecernu.rickandmortyapp.ui.detail.CharacterDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    viewModel: CharacterDetailViewModel = hiltViewModel(),
    navController: NavController,
    id: String,
) {
    val characterDetail by viewModel.characterInfo.collectAsState()
    val isLoading by viewModel.loader.collectAsState()
    val isError by viewModel.isError.collectAsState(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "${characterDetail?.basicInfo?.name}", style = MaterialTheme.typography.titleMedium)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                CharacterDetailSkeleton()
            } else {
                if (isError) {
                    ErrorOrEmptyState(
                        title = stringResource(R.string.error_title),
                        description = stringResource(R.string.error_description),
                    )
                } else {
                    if (characterDetail != null) {
                        CharacterDetail(
                            characterInfo = characterDetail!!,
                        ) {
                        }
                    } else {
                        ErrorOrEmptyState(
                            title = stringResource(R.string.emtpy_state_title),
                            description = stringResource(R.string.empty_state_description),
                        )
                    }
                }
            }
        }
    }
}
