import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.ui.components.HomeHeader
import com.example.ynspo.ui.screen.home.HomeViewModel
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.SelectedColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
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

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(photos.value) { photo ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SelectedColor),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((150..250).random().dp)
                        .clickable {
                            navController.navigate("pinDetail/${photo.urls.small}")
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = photo.urls.small),
                        contentDescription = photo.description ?: "Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
