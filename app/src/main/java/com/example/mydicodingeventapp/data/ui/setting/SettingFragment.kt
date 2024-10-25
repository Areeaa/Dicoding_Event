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
import com.example.mydicodingeventapp.R
import com.example.mydicodingeventapp.data.ui.DarkViewModelFactory
import com.example.mydicodingeventapp.data.ui.SettingPreferences
import com.example.mydicodingeventapp.data.ui.ViewModelFactory
import com.example.mydicodingeventapp.data.ui.dataStore
import com.example.mydicodingeventapp.databinding.FragmentHomeBinding
import com.example.mydicodingeventapp.databinding.FragmentSettingBinding
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingFragment : Fragment() {

    private lateinit var viewModel: SettingFragmentViewModel
    private var _binding: FragmentSettingBinding? = null
    private lateinit var switchTheme: SwitchMaterial

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //viewmodel
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, DarkViewModelFactory(pref)).get(SettingFragmentViewModel::class.java)

        viewModel.getThemeSettings().observe(viewLifecycleOwner){isDarkModeActive: Boolean->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }


        switchTheme = binding.switchTheme

        switchTheme.setOnCheckedChangeListener{ _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
        return root
    }


}


