package com.example.mydicodingeventapp.data.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mydicodingeventapp.data.ui.SettingPreferences
import kotlinx.coroutines.launch

class SettingFragmentViewModel(private val pref: SettingPreferences): ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }

}
    fun getNotificationSettings(): LiveData<Boolean> {
        return pref.getNotificationSetting().asLiveData()
    }

    fun saveNotificationSetting(isNotificationEnabled: Boolean) {
        viewModelScope.launch {
            pref.saveNotificationSetting(isNotificationEnabled)
        }


    }

}