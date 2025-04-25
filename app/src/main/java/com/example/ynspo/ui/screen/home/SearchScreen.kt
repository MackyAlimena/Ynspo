package com.example.ynspo.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ynspo.ui.theme.BackgroundColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.ui.home.HomeViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.ynspo.ui.theme.SelectedColor

@Composable
fun SearchScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val photos = viewModel.photos.collectAsState()
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }

    val invalidTerms = listOf("fishing", "cars", "sports", "weapons")

    Column(modifier = Modifier
        .fillMaxSize()
        .background(BackgroundColor)
        .padding(16.dp)) {

        // Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search crafts...") },
            trailingIcon = {
                IconButton(onClick = {
                    if (invalidTerms.any { query.contains(it, ignoreCase = true) }) {
                        Toast.makeText(context, "That is not related to handcrafting ;)", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.search(query)
                    }
                }) {
                    Icon(Icons.Default.Search , contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Suggested Tags
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Embroidery", "Knitting", "Origami", "Painting").forEach { tag ->
                Button(onClick = {
                    query = tag
                    viewModel.search(tag)
                }) {
                    Text(tag)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Results or Message
        if (photos.value.isNotEmpty()) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                items(photos.value) { photo ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SelectedColor),
                        modifier = Modifier.height((150..250).random().dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(model = photo.urls.small),
                            contentDescription = photo.description ?: "",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
