package com.example.ynspo.data.db.datastore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun UserName() {
    val viewModel = hiltViewModel<UserNameViewModel>()
    val userName by viewModel.userName.collectAsStateWithLifecycle()

    val input = remember {
        mutableStateOf("")
    }

    if(userName.isBlank()) {
        Column {
            TextField(
                value = input.value,
                onValueChange = {
                    input.value = it
                }
            )
            Button(
                onClick = {
                    viewModel.saveUserName(input.value)
                    input.value = ""
                },
                enabled = input.value.isNotBlank()
            ) {
                Text("Save name")
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                userName,
                fontSize = 24.sp,
            )
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "",
                modifier = Modifier.clickable {
                    viewModel.clearUserName()
                }
            )
        }
    }
}