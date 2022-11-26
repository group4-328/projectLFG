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

<<<<<<< Updated upstream
=======
    private lateinit var loginButton: Button;
    private lateinit var registerButton:Button;

    //authentication
>>>>>>> Stashed changes
    private lateinit var authenticator: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // initializing views
        view = layoutInflater.inflate(R.layout.activity_log_in,null);
<<<<<<< Updated upstream
        //emailTextEdit = view.findViewById(R.id.emailtextview)
        //passwordTextEdit = view.findViewById(R.id.passwordtextview);
=======
        emailTextEdit = view.findViewById(R.id.emailtextview)
        passwordTextEdit = view.findViewById(R.id.passwordtextview);
        loginButton = view.findViewById(R.id.LoginButton)
        registerButton = view.findViewById(R.id.RegisterButton)

        database  = Firebase.database;
        myref = database.reference;
>>>>>>> Stashed changes

        emailTextEdit.setOnClickListener {

<<<<<<< Updated upstream
=======
        loginButton.setOnClickListener {
            if(TextUtils.isEmpty(emailTextEdit.text.toString()) || TextUtils.isEmpty(passwordTextEdit.text.toString())){

            }
>>>>>>> Stashed changes
        }
        passwordTextEdit.setOnClickListener {

<<<<<<< Updated upstream
=======
        registerButton.setOnClickListener {
            if(TextUtils.isEmpty(emailTextEdit.text.toString()) || TextUtils.isEmpty(passwordTextEdit.text.toString())){
                signUp("tmp",emailTextEdit.text.toString(),passwordTextEdit.text.toString())
            }
>>>>>>> Stashed changes
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