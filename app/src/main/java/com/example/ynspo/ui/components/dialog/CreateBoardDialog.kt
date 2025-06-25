package com.example.ynspo.ui.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ynspo.ui.theme.Dimens

@Composable
fun CreateBoardDialog(
    onDismiss: () -> Unit,
    onCreateBoard: (String) -> Unit
) {
    var boardName by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(Dimens.CornerRadiusM),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = Dimens.ElevationL
        ) {
            Column(
                modifier = Modifier
                    .padding(Dimens.PaddingXL)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crear Nuevo Board",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(Dimens.PaddingL))
                
                OutlinedTextField(
                    value = boardName,
                    onValueChange = { 
                        boardName = it
                        showError = false
                    },
                    label = { Text("Nombre del board") },
                    placeholder = { Text("Ej: Ideas de decoración") },
                    isError = showError,
                    supportingText = if (showError) {
                        { Text("El nombre no puede estar vacío") }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                
                Spacer(modifier = Modifier.height(Dimens.PaddingL))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancelar")
                    }
                    
                    Spacer(modifier = Modifier.width(Dimens.PaddingM))
                    
                    Button(
                        onClick = {
                            if (boardName.trim().isNotEmpty()) {
                                onCreateBoard(boardName.trim())
                                onDismiss()
                            } else {
                                showError = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Crear")
                    }
                }
            }
        }
    }
} 