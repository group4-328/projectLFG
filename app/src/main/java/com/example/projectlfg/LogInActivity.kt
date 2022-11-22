package com.example.projectlfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    private lateinit var view: View;
    private lateinit var emailTextEdit:EditText;
    private lateinit var passwordTextEdit:EditText;

    private lateinit var authenticator: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        view = layoutInflater.inflate(R.layout.activity_log_in,null);
        //emailTextEdit = view.findViewById(R.id.emailtextview)
        //passwordTextEdit = view.findViewById(R.id.passwordtextview);

        emailTextEdit.setOnClickListener {

        }
        passwordTextEdit.setOnClickListener {

        }
    }

    private fun logIn(email: String, password: String){

    }

    private fun signUp(name: String, email: String, password: String){

        // Pre-config firebase signup method
        authenticator.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success

                    

                } else {
                    // If sign in fails


                }
            }
    }
}