package com.example.projectlfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast

class MainMenuActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var findActivityImageButton: ImageView
    private lateinit var scheduledActivityImageButton: ImageView
    private lateinit var contactListImageButton: ImageView
    private lateinit var configImageButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // init image view
        findActivityImageButton         = findViewById(R.id.findActivityImageButton)
        findActivityImageButton.setImageResource(R.drawable.activity)
        findActivityImageButton.setOnClickListener(this)

        scheduledActivityImageButton    = findViewById(R.id.scheduleActivityImageButton)
        scheduledActivityImageButton.setImageResource(R.drawable.schedule)
        scheduledActivityImageButton.setOnClickListener(this)

        contactListImageButton          = findViewById(R.id.contactListImageButton)
        contactListImageButton.setImageResource(R.drawable.contact)
        contactListImageButton.setOnClickListener(this)

        configImageButton               = findViewById(R.id.configImageButton)
        configImageButton.setImageResource(R.drawable.setting)
        configImageButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        popUp("click " + v.toString())
    }

    private fun popUp(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}