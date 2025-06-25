package com.example.ynspo.ui.screen.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ynspo.R
import com.example.ynspo.ui.profile.UserProfile
import com.example.ynspo.ui.theme.DetailColor
import com.example.ynspo.ui.theme.SelectedColor
import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.ui.theme.Dimens
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun ProfileHeader(
    userProfile: UserProfile,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile image
        Image(
            painter = rememberAsyncImagePainter(userProfile.photoUrl),
            contentDescription = stringResource(R.string.profile_image),
            modifier = Modifier
                .size(Dimens.ProfileImageSize)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingM))

        // User name
        Text(
            text = userProfile.name,
            style = MaterialTheme.typography.headlineSmall,
            color = DetailColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingS))
        
        // User bio
        Text(
            text = userProfile.bio,
            style = MaterialTheme.typography.bodyMedium,
            color = DetailColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileHobbiesSection(
    hobbies: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingL)
    ) {
        // Hobbies header
        Text(
            text = stringResource(R.string.profile_hobbies),
            style = MaterialTheme.typography.titleMedium,
            color = DetailColor,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingS))
        
        // List of hobbies
        hobbies.forEach { hobby ->
            Text(
                text = "• $hobby",
                style = MaterialTheme.typography.bodyLarge,
                color = SelectedColor,
                modifier = Modifier.padding(vertical = Dimens.PaddingXXS)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    YnspoTheme {
        ProfileHeader(
            userProfile = UserProfile(
                name = "Juan Cherry Blossom",
                photoUrl = "https://i.pravatar.cc/150?img=3",
                bio = "Lover of crafts, nature & code :computer::herb:",
                hobbies = emptyList() // Hobbies shown in a different component
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHobbiesSectionPreview() {
    YnspoTheme {
        ProfileHobbiesSection(
            hobbies = listOf("Painting", "Knitting", "Embroidery", "Photography")
        )
    }
}

@Composable
fun ProfileImageSection(
    imageUrl: String?,
    displayName: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(Dimens.ProfileImageSize)
                .clip(CircleShape)
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingM))
        
        Text(
            text = displayName,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ProfileStatItem(
    label: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondaryContainer
        )
    }
}
