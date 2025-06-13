package com.example.ynspo.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.data.repository.Board
import com.example.ynspo.ui.profile.UserProfile
import com.example.ynspo.ui.theme.BackgroundColor
import com.example.ynspo.ui.theme.YnspoTheme
import com.example.ynspo.ui.theme.Dimens


val previewPhotos = listOf(
    UnsplashPhoto(
        id = "1",
        description = "Sample Photo 1",
        urls = UnsplashPhotoUrls(
            small = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5",
            regular = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5",
            full = "https://images.unsplash.com/photo-1499951360447-b19be8fe80f5"
        )
    ),
    UnsplashPhoto(
        id = "2", 
        description = "Sample Photo 2",
        urls = UnsplashPhotoUrls(
            small = "https://images.unsplash.com/photo-1523567830207-96731740fa71",
            regular = "https://images.unsplash.com/photo-1523567830207-96731740fa71",
            full = "https://images.unsplash.com/photo-1523567830207-96731740fa71"
        )
    ),
    UnsplashPhoto(
        id = "3",
        description = "Sample Photo 3",
        urls = UnsplashPhotoUrls(
            small = "https://images.unsplash.com/photo-1581235720704-06d3acfcb611",
            regular = "https://images.unsplash.com/photo-1581235720704-06d3acfcb611",
            full = "https://images.unsplash.com/photo-1581235720704-06d3acfcb611"
        )
    ),
    UnsplashPhoto(
        id = "4",
        description = "Sample Photo 4",
        urls = UnsplashPhotoUrls(
            small = "https://images.unsplash.com/photo-1516214104703-d870798883c5",
            regular = "https://images.unsplash.com/photo-1516214104703-d870798883c5",
            full = "https://images.unsplash.com/photo-1516214104703-d870798883c5"
        )
    )
)

val previewBoards = listOf(
    Board(
        id = 1,
        name = "Handmade Decor",
        photos = previewPhotos.take(2).toMutableList()
    ),
    Board(
        id = 2,
        name = "Knitting Ideas",
        photos = previewPhotos.takeLast(2).toMutableList()
    ),
    Board(
        id = 3,
        name = "Summer Vibes",
        photos = previewPhotos.toMutableList()
    )
)

val previewUserProfile = UserProfile(
    name = "Juan Cherry Blossom",
    photoUrl = "https://i.pravatar.cc/150?img=3",
    bio = "Lover of crafts, nature & code :computer::herb:",
    hobbies = listOf("Painting", "Knitting", "Embroidery", "Photography")
)

@Preview(showBackground = true)
@Composable
fun DesignSystemPreview() {
    YnspoTheme {
        Surface(
            color = BackgroundColor,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(Dimens.PaddingL),
                verticalArrangement = Arrangement.spacedBy(Dimens.PaddingL)
            ) {
                // Typography
                Text(
                    text = "Typography",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Text(
                    text = "Headline Large",
                    style = MaterialTheme.typography.headlineLarge
                )
                
                Text(
                    text = "Headline Medium",
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Text(
                    text = "Headline Small",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Text(
                    text = "Title Large",
                    style = MaterialTheme.typography.titleLarge
                )
                
                Text(
                    text = "Body Large",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                Text(
                    text = "Body Medium",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                // Colors
                Text(
                    text = "Colors",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = Dimens.PaddingXL)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                ) {
                    ColorSwatch(color = DetailColor, name = "Detail Color")
                    ColorSwatch(color = SelectedColor, name = "Selected Color")
                    ColorSwatch(color = BackgroundColor, name = "Background Color")
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(color: Color, name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.PaddingXXL)
                .background(color = color)
        )
        
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = Dimens.PaddingXS)
        )
    }
}

@Composable
private fun Text(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Text(
        text = text,
        style = style,
        modifier = modifier
    )
}
