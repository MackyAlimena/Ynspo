package com.austral.learning_android.data.db.room

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.austral.learning_android.data.Friend
import com.austral.learning_android.storage.LearningAndroidDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(
    @ApplicationContext val context: Context,
) : ViewModel() {
    private val database = LearningAndroidDatabase.getDatabase(context)

    val friendList = database.friendDao().getAllFriends().asFlow()

    fun addFriend(name: String) {
        val friend = Friend(name = name, confirmed = false)
        viewModelScope.launch {
            database.friendDao().insert(friend)
        }
    }

    fun deleteFriend(friend: Friend) {
        viewModelScope.launch {
            database.friendDao().delete(friend)
        }
    }

    fun confirmFriend(friend: Friend) {
        viewModelScope.launch {
            database.friendDao().update(friend.copy(confirmed = true))
        }
    }

    fun unconfirmFriend(friend: Friend) {
        viewModelScope.launch {
            database.friendDao().update(friend.copy(confirmed = false))
        }
    }
}