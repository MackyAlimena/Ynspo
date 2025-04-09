package com.example.ynspo.ui.screen.home


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ynspo.ui.components.ImageCard
import com.example.ynspo.ui.theme.SolidPink80

@Composable
fun HomeScreen() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(inspirationList) { item ->
            ImageCard(
                imageRes = item.imageRes,
                title = item.title,
                containerColor = SolidPink80
            )
        }
    }
}
