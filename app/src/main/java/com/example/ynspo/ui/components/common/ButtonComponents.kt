package com.example.ynspo.ui.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.ynspo.ui.theme.DetailColor
import com.example.ynspo.ui.theme.SelectedColor
import com.example.ynspo.ui.theme.YnspoTheme
import androidx.compose.ui.unit.dp
import com.example.ynspo.ui.theme.Dimens



@Composable
fun YnspoPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = DetailColor,
            contentColor = Color.White,
            disabledContainerColor = DetailColor.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun YnspoSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = SelectedColor,
            contentColor = DetailColor,
            disabledContainerColor = SelectedColor.copy(alpha = 0.5f),
            disabledContentColor = DetailColor.copy(alpha = 0.5f)
        ),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun YnspoPrimaryButtonPreview() {
    YnspoTheme {
        YnspoPrimaryButton(
            text = "Primary Button",
            onClick = { },
            modifier = Modifier.padding(Dimens.PaddingL)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun YnspoSecondaryButtonPreview() {
    YnspoTheme {
        YnspoSecondaryButton(
            text = "Secondary Button",
            onClick = { },
            modifier = Modifier.padding(Dimens.PaddingL)
        )
    }
}
