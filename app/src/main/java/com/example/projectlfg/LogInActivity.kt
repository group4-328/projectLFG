package com.example.projectlfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class LogInActivity : AppCompatActivity() {
    private lateinit var view: View;
    private lateinit var emailTextEdit:EditText;
    private lateinit var passwordTextEdit:EditText;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        view = layoutInflater.inflate(R.layout.activity_log_in,null);
        emailTextEdit = view.findViewById(R.id.emailtextview)
        passwordTextEdit = view.findViewById(R.id.passwordtextview);

        emailTextEdit.setOnClickListener {
            
        }
        passwordTextEdit.setOnClickListener {

        }
    }
}