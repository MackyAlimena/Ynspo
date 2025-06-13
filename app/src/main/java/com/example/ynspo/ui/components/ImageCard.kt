package com.example.ynspo.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.example.ynspo.ui.theme.Dimens

@Composable
fun ImageCard(
    imageRes: Int,
    title: String,
    containerColor: Color,
    shape: Shape = RoundedCornerShape(Dimens.PaddingL)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = shape
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.ImageHeightM)
            )
            Spacer(modifier = Modifier.height(Dimens.PaddingS))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(Dimens.PaddingS),
                fontSize = 14.sp
            )
        }
    }
}
