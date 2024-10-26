package com.example.mydicodingeventapp.data.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mydicodingeventapp.data.notification.NotificationWorker
import com.example.mydicodingeventapp.data.ui.SettingPreferences
import com.example.mydicodingeventapp.data.ui.SettingViewModelFactory
import com.example.mydicodingeventapp.data.ui.dataStore
import com.example.mydicodingeventapp.databinding.FragmentSettingBinding
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.concurrent.TimeUnit


class SettingFragment : Fragment() {

    private lateinit var viewModel: SettingFragmentViewModel
    private var _binding: FragmentSettingBinding? = null
    private lateinit var switchTheme: SwitchMaterial
    private lateinit var switchNotification: SwitchMaterial


    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //viewmodel
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingFragmentViewModel::class.java]

        viewModel.getThemeSettings().observe(viewLifecycleOwner){isDarkModeActive: Boolean->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        setupThemeSwitch()


        switchTheme = binding.switchTheme

        switchTheme.setOnCheckedChangeListener{ _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }


        return root
    }

    private fun setupThemeSwitch() {
        switchNotification = binding.switchNotification

        // Observe notification settings
        viewModel.getNotificationSettings().observe(viewLifecycleOwner) { isNotificationEnabled ->
            switchNotification.isChecked = isNotificationEnabled
        }

        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveNotificationSetting(isChecked)
            if (isChecked) {
                startWorker()
            } else {
                cancelWorker()
            }
        }
    }

    private fun startWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            1, TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )
    }

    private fun cancelWorker() {
        WorkManager.getInstance(requireContext()).cancelUniqueWork(WORK_NAME)
    }

    companion object {
        private const val WORK_NAME = "notificationWorker"
    }

}





