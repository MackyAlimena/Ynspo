import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ynspo.ui.components.HomeHeader
import com.example.ynspo.ui.components.PinterestGrid
import com.example.ynspo.ui.screen.home.HomeViewModel
import com.example.ynspo.ui.theme.BackgroundColor


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val photos = viewModel.photos.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.search("crafts")
    }

    Column(
        modifier = Modifier
            .background(BackgroundColor)
            .fillMaxSize()
    ) {
        HomeHeader()

        PinterestGrid(
            photos = photos.value,
            onPhotoClick = { photo ->
                sharedViewModel.selectedPhoto = photo
                navController.navigate("pinDetail")
            }
        )
    }
}


