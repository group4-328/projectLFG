package com.example.projectlfg

import android.content.Intent
import android.os.Bundle
import androidx.preference.*

class SettingPreferenceFragment: PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    lateinit var UserPref:Preference;
    lateinit var History:Preference;
    companion object{
        val USERINFO="UserInfo"
        val HISTORYINFO = "HistoryInfo"
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        return true;
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.user_preference,rootKey);
        UserPref = findPreference<Preference>(USERINFO)!!
        History = findPreference<Preference>(HISTORYINFO)!!


        UserPref.onPreferenceClickListener = object :Preference.OnPreferenceClickListener{
            override fun onPreferenceClick(preference: Preference): Boolean {
                val intent = Intent(activity,UserInfoActivity::class.java);
                startActivity(intent);
                return true;
            }
        }

        History.onPreferenceClickListener = object:Preference.OnPreferenceClickListener{
            override fun onPreferenceClick(preference: Preference): Boolean {
                val intent = Intent(activity,UserHistoryActivity::class.java);
                startActivity(intent);
                return true;
            }

        }
    }
}