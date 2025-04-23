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

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Explore", color = DetailColor, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("For you", color = SelectedColor, fontSize = 16.sp)
    }
}
