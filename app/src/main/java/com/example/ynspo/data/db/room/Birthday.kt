package com.example.ynspo.data.db.room

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayList() {
    val viewModel = hiltViewModel<BirthdayViewModel>()
    val friendList by viewModel.friendList.collectAsState(listOf())

    var showAlertDialog by remember {
        mutableStateOf(false)
    }

    fun showAlertDialog() {
        showAlertDialog = true
    }

    if(showAlertDialog) {
        BasicAlertDialog(onDismissRequest = { showAlertDialog = false }) {
            Card {
                Text(
                    text = "Your friend data is not complete",
                    modifier = Modifier.padding(10.dp),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            Text(
                "My birthday list:",
                fontSize = 34.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            AddFriendForms(
                showAlertDialog = ::showAlertDialog,
                addFriend = viewModel::addFriend
            )
        }
        items(friendList) { friend ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    friend.name,
                    fontSize = 24.sp,
                )
                Row {
                    if(friend.confirmed) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                viewModel.unconfirmFriend(friend)
                            }
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                viewModel.confirmFriend(friend)
                            }
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        modifier = Modifier.clickable {
                            viewModel.deleteFriend(friend)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AddFriendForms(
    showAlertDialog: () -> Unit,
    addFriend: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var friendName by remember {
        mutableStateOf("")
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TextField(
            value = friendName,
            onValueChange = { friendName = it },
            label = {
                Text(text = "Name")
            },
        )

        Button(onClick = {
            if(friendName.isNotEmpty()) {
                addFriend(friendName)
                friendName = ""
            } else {
                showAlertDialog()
            }
        },
            modifier = modifier,
        ) {
            Text(text = "Save friend")
        }
    }
}