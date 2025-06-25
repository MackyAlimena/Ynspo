package com.example.ynspo.ui.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear

import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ynspo.ui.screen.home.HomeTab
import com.example.ynspo.ui.theme.Dimens
import com.example.ynspo.ui.theme.YnspoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernHomeHeader(
    currentTab: HomeTab,
    onTabChange: (HomeTab) -> Unit,
    onSearch: (String) -> Unit,
    searchHistory: List<String>,
    searchSuggestions: List<String>,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingL)
    ) {
        // Tabs Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.PaddingM),
            horizontalArrangement = Arrangement.Center
        ) {
            // For You Tab
            TabButton(
                text = "For You",
                selected = currentTab == HomeTab.FOR_YOU,
                onClick = { onTabChange(HomeTab.FOR_YOU) },
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(Dimens.PaddingS))
            
            // Explore Tab
            TabButton(
                text = "Explore",
                selected = currentTab == HomeTab.EXPLORE,
                onClick = { onTabChange(HomeTab.EXPLORE) },
                modifier = Modifier.weight(1f)
            )
        }
        
        // Search Bar (solo visible en Explore tab)
        if (currentTab == HomeTab.EXPLORE) {
            Column {
                // Barra de búsqueda
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar inspiraciones...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { 
                                searchText = ""
                                keyboardController?.hide()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Limpiar",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                    ),
                    shape = RoundedCornerShape(Dimens.CornerRadiusM),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchText.isNotBlank()) {
                                onSearch(searchText)
                                keyboardController?.hide()
                            }
                        }
                    ),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(Dimens.PaddingM))
                
                // Historial de búsqueda
                if (searchHistory.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Búsquedas recientes",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Medium
                        )
                        
                        TextButton(onClick = onClearHistory) {
                            Text(
                                "Limpiar",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS),
                        modifier = Modifier.padding(bottom = Dimens.PaddingS)
                    ) {
                        items(searchHistory) { query ->
                            SearchChip(
                                text = query,
                                onClick = { 
                                    searchText = query
                                    onSearch(query)
                                },
                                isHistory = true
                            )
                        }
                    }
                }
                
                // Sugerencias de búsqueda
                Text(
                    text = "Sugerencias populares",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = Dimens.PaddingS)
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingS)
                ) {
                    items(searchSuggestions) { suggestion ->
                        SearchChip(
                            text = suggestion,
                            onClick = { 
                                searchText = suggestion
                                onSearch(suggestion)
                            },
                            isHistory = false
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(Dimens.PaddingL))
            }
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    val textColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    }
    
    Surface(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(Dimens.CornerRadiusL),
        color = backgroundColor,
        tonalElevation = if (selected) Dimens.ElevationS else 0.dp
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                horizontal = Dimens.PaddingL,
                vertical = Dimens.PaddingM
            )
        )
    }
}

@Composable
private fun SearchChip(
    text: String,
    onClick: () -> Unit,
    isHistory: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(Dimens.CornerRadiusM),
        color = if (isHistory) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        tonalElevation = Dimens.ElevationXS,
        border = if (!isHistory) {
            androidx.compose.foundation.BorderStroke(
                Dimens.StrokeWidthThin, 
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        } else null
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = Dimens.PaddingM,
                vertical = Dimens.PaddingS
            )
        ) {

            
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isHistory) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ModernHomeHeaderPreview() {
    YnspoTheme {
        ModernHomeHeader(
            currentTab = HomeTab.EXPLORE,
            onTabChange = {},
            onSearch = {},
            searchHistory = listOf("DIY", "Decoración", "Arte"),
            searchSuggestions = listOf("Manualidades", "Vintage", "Minimalista"),
            onClearHistory = {}
        )
    }
}
