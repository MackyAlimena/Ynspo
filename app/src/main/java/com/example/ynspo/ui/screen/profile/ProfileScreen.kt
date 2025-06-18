package com.example.ynspo.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.R
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.DetailColor
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.SelectedColor

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = ProfileViewModel()) {
    val userProfile = viewModel.userProfile.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(Dimens.PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(userProfile.value.photoUrl),
            contentDescription = stringResource(R.string.profile_image),
            modifier = Modifier
                .size(Dimens.ProfileImageSize)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingM))

        Text(
            text = userProfile.value.name,
            style = MaterialTheme.typography.headlineSmall,
            color = DetailColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingS))

        Text(
            text = userProfile.value.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = DetailColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingL))

        Text(
            text = stringResource(R.string.profile_hobbies),
            style = MaterialTheme.typography.titleMedium,
            color = DetailColor,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingS))

        userProfile.value.hobbies.forEach { hobby ->
            Text(
                text = "• $hobby",
                style = MaterialTheme.typography.bodyLarge,
                color = SelectedColor,
                modifier = Modifier.padding(vertical = Dimens.PaddingXXS)
            )
        }
    }
}

@Composable
fun ProfileStat(label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$count",
            style = MaterialTheme.typography.titleMedium,
            color = DetailColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = DetailColor
        )
    }
}
