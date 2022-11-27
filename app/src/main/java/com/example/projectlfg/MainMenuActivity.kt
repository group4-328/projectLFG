package com.example.projectlfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.projectlfg.Util.popUp

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
        findActivityImageButton.setOnClickListener(this)

        scheduledActivityImageButton    = findViewById(R.id.scheduleActivityImageButton)
        scheduledActivityImageButton.setOnClickListener(this)

        contactListImageButton          = findViewById(R.id.contactListImageButton)
        contactListImageButton.setOnClickListener(this)

        configImageButton               = findViewById(R.id.configImageButton)
        configImageButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v == contactListImageButton){

        }
    }


}