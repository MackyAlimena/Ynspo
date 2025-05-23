import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ynspo.ui.boards.BoardsViewModel
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.SelectedColor
@Composable
fun BoardsScreen(
    navController: NavController,
    boardsViewModel: BoardsViewModel = hiltViewModel()
) {
    val boards = boardsViewModel.boards.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
    ) {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(boards.value) { board ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SelectedColor.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("boardDetail/${board.id}") }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            board.photos.take(3).forEach { photo ->
                                AsyncImage(
                                    model = photo.urls.small,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(board.name, style = MaterialTheme.typography.titleMedium)
                        Text("${board.photos.size} pins", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}