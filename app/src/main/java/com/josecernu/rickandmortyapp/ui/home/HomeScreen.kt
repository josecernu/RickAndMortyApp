
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.josecernu.rickandmortyapp.navigation.Destination
import com.josecernu.rickandmortyapp.ui.home.HomeViewModel
import kotlinx.coroutines.flow.collect

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    LaunchedEffect(Unit) {
        viewModel.charactersList.collect {
            if (it.isNotEmpty()) {
                Log.d("ASDF", "List of characters")
            } else {
                Log.d("ASDF", "Empty State")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = {
            navController.navigate(Destination.Detail.route + "/123")
        }) {
            Text(text = "Go to Detail")
        }
    }
}
