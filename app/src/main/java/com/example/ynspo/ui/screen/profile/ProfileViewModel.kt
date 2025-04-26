package com.example.ynspo.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class UserProfile(
    val name: String,
    val photoUrl: String,
    val bio: String,
    val hobbies: List<String>,
)

class ProfileViewModel : ViewModel() {

    private val _userProfile = MutableStateFlow(
        UserProfile(
            name = "Macky :cherry_blossom:",
            photoUrl = "https://i.pravatar.cc/150?img=3",
            bio = "Lover of crafts, nature & code :computer::herb:",
            hobbies = listOf("Painting", "Knitting", "Embroidery", "Photography")
        )
    )

    val userProfile: StateFlow<UserProfile> = _userProfile
}
