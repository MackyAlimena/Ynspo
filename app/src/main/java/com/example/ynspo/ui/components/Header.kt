package com.example.ynspo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ynspo.ui.theme.DetailColor
import com.example.ynspo.ui.theme.SelectedColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.ynspo.ui.theme.BackgroundColor

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            "Explore",
            color = DetailColor,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 16.dp)
        )

        Surface(
            shape = MaterialTheme.shapes.small,
            border = BorderStroke(1.dp, DetailColor),
            color = BackgroundColor
        ) {
            Text(
                "For you",
                color = DetailColor,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}