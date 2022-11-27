package com.example.projectlfg

import android.os.Bundle
import androidx.preference.*

class SettingPreferenceFragment: PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {




    override fun onPreferenceClick(preference: Preference): Boolean {
        return true;
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.user_preference,rootKey);
    }
}