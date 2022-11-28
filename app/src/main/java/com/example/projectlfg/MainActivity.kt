package com.example.projectlfg

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // To be used possibly with screen updates and login
    private var loginMade = true

    // Buttons
    private lateinit var viewJoinedEventsButton: Button
    private lateinit var findNewEventButton: Button
    private lateinit var viewUserProfileButton: Button
    private lateinit var viewContactsButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utilities.checkPermissions(this)
        initButtons()

        findNewEventButton.setOnClickListener(View.OnClickListener {
            // temp
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        })
        
    }

    fun initButtons() {
        viewJoinedEventsButton = findViewById(R.id.view_joined_events_button)
        findNewEventButton = findViewById(R.id.find_new_events_button)
        viewUserProfileButton = findViewById(R.id.view_profile_settings_button)
        viewContactsButton = findViewById(R.id.view_contacts_button)
    }
}