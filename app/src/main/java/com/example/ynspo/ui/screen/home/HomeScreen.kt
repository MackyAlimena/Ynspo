package com.example.ynspo.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ynspo.R
import com.example.ynspo.model.InspirationCard
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.RusticRed

@Composable
fun HomeScreen(paddingValues: PaddingValues, viewModel: HomeViewModel = viewModel()) {
    val cards by viewModel.cards.collectAsState()

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .background(RusticRed)
    ) {
        Text(
            text = stringResource(id = R.string.home_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(Dimens.PaddingM),
            fontSize = Dimens.TextTitle,
            color = MaterialTheme.colorScheme.onPrimary
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(Dimens.PaddingM),
            verticalArrangement = Arrangement.spacedBy(Dimens.GridSpacing),
            horizontalArrangement = Arrangement.spacedBy(Dimens.GridSpacing)
        ) {
            items(cards) { card ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = card.imageRes),
                        contentDescription = card.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(Dimens.ImageHeightM),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(Dimens.PaddingS),
                        fontSize = Dimens.TextBody
                    )
                }
            }
        }
    }
}
