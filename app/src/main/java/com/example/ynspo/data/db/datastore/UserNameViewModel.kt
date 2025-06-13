package com.example.ynspo.data.db.datastore

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.austral.learning_android.storage.PreferencesKeys
import com.austral.learning_android.storage.getFromDataStore
import com.austral.learning_android.storage.saveToDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNameViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private var _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    init {
        getNameFromDataStore()
    }

    private fun getNameFromDataStore() {
        viewModelScope.launch {
            getFromDataStore(context, PreferencesKeys.USER_NAME_KEY).collect {
                _userName.value = it ?: ""
            }
        }
    }

    fun saveUserName(name: String) {
        viewModelScope.launch {
            saveToDataStore(context, name, PreferencesKeys.USER_NAME_KEY)
            getNameFromDataStore()
        }
    }

    fun clearUserName() {
        viewModelScope.launch {
            saveToDataStore(context, "", PreferencesKeys.USER_NAME_KEY)
        }
    }
}