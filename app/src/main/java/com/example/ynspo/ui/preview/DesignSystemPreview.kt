package com.example.ynspo.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.ynspo.data.model.Board
import com.example.ynspo.data.model.InspirationItem
import com.example.ynspo.data.model.UnsplashPhoto
import com.example.ynspo.data.model.UnsplashPhotoUrls
import com.example.ynspo.data.model.toInspirationItem
import com.example.ynspo.ui.profile.UserProfile

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
        photos = previewPhotos.take(2).map { it.toInspirationItem() }.toMutableList()
    ),
    Board(
        id = 2,
        name = "Knitting Ideas",
        photos = previewPhotos.takeLast(2).map { it.toInspirationItem() }.toMutableList()
    ),
    Board(
        id = 3,
        name = "Summer Vibes",
        photos = previewPhotos.map { it.toInspirationItem() }.toMutableList()
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(Dimens.PaddingL),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingL)
        ) {
            item {
                Text(
                    text = "Design System Preview",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                ColorsSection()
            }
            
            item {
                TypographySection()
            }
            
            item {
                ComponentsSection()
            }
        }
    }
}

@Composable
private fun ColorsSection() {
    Column {
        Text(
            text = "Colors",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingM))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
        ) {
            ColorSwatch(color = MaterialTheme.colorScheme.primary, name = "Primary")
            ColorSwatch(color = MaterialTheme.colorScheme.secondary, name = "Secondary")
            ColorSwatch(color = MaterialTheme.colorScheme.surface, name = "Surface")
        }
    }
}

@Composable
private fun TypographySection() {
    Column {
        Text(
            text = "Typography",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingM))
        
        Text(
            text = "Headline Large",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Headline Medium",
            style = MaterialTheme.typography.headlineMedium
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
    }
}

@Composable
private fun ComponentsSection() {
    Column {
        Text(
            text = "Components",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingM))
        
        // Button examples
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
        ) {
            Button(onClick = {}) {
                Text("Primary Button")
            }
            OutlinedButton(onClick = {}) {
                Text("Outlined Button")
            }
        }
        
        Spacer(modifier = Modifier.height(Dimens.PaddingM))
        
        // Card example
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = Dimens.ElevationM)
        ) {
            Text(
                text = "This is a sample card",
                modifier = Modifier.padding(Dimens.PaddingL),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ColorSwatch(
    color: Color,
    name: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.IconSizeL)
                .background(
                    color = color,
                    shape = RoundedCornerShape(Dimens.CornerRadiusS)
                )
        )
        
        Spacer(modifier = Modifier.height(Dimens.PaddingXS))
        
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
