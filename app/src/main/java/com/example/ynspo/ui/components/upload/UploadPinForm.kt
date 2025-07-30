package com.example.ynspo.ui.components.upload

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.ynspo.R
import com.example.ynspo.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadPinForm(
    description: String,
    hashtags: String,
    keywords: String,
    onDescriptionChange: (String) -> Unit,
    onHashtagsChange: (String) -> Unit,
    onKeywordsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Descripción
        Text(
            text = stringResource(R.string.upload_description_label),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = Dimens.PaddingS)
        )
        
        TextField(
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = { Text(stringResource(R.string.upload_description_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.PaddingL),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
            ),
            shape = MaterialTheme.shapes.medium,
            maxLines = 3
        )
        
        // Hashtags
        Text(
            text = stringResource(R.string.upload_hashtags_label),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = Dimens.PaddingS)
        )
        
        TextField(
            value = hashtags,
            onValueChange = onHashtagsChange,
            placeholder = { Text(stringResource(R.string.upload_hashtags_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Dimens.PaddingL),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
            ),
            shape = MaterialTheme.shapes.medium
        )
        
        // Palabras clave
        Text(
            text = stringResource(R.string.upload_keywords_label),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = Dimens.PaddingS)
        )
        
        TextField(
            value = keywords,
            onValueChange = onKeywordsChange,
            placeholder = { Text(stringResource(R.string.upload_keywords_hint)) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
            ),
            shape = MaterialTheme.shapes.medium
        )
    }
} 