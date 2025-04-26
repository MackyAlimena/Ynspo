import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
        Text(
            text = "Your Boards",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(boards.value) { board ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = SelectedColor),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("boardDetail/${board.id}")
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = board.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
