package com.example.ynspo.ui.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ynspo.R
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.DetailColor
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.YnspoTheme


@Composable
fun HomeHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingL),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Explore",
            style = MaterialTheme.typography.headlineMedium,
            color = DetailColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = Dimens.PaddingL)
        )

        Surface(
            shape = RoundedCornerShape(Dimens.PaddingS),
            color = BackgroundColor,            border = androidx.compose.foundation.BorderStroke(Dimens.PaddingXXS * 0.5f, DetailColor),
            modifier = Modifier.padding(vertical = Dimens.PaddingXS)
        ) {
            Text(
                text = stringResource(id = R.string.home_title),
                style = MaterialTheme.typography.bodyLarge,
                color = DetailColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = Dimens.PaddingM, vertical = Dimens.PaddingXS)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeHeaderPreview() {
    YnspoTheme {
        Surface(
            color = BackgroundColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            HomeHeader()
        }
    }
}
