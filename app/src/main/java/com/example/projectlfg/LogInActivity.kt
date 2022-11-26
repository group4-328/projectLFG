package com.example.projectlfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {

    private lateinit var view: View;
    private lateinit var emailTextEdit:EditText;
    private lateinit var passwordTextEdit:EditText;

    private lateinit var loginButton: Button;
    private lateinit var registerButton:Button;

    //authentication
    private lateinit var authenticator: FirebaseAuth

    //database
    private lateinit var database:FirebaseDatabase;

    //ref
    private lateinit var myref : DatabaseReference;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        // initializing views
        view = layoutInflater.inflate(R.layout.activity_log_in,null);
        emailTextEdit = view.findViewById(R.id.emailtextview)
        passwordTextEdit = view.findViewById(R.id.passwordtextview);
        loginButton = view.findViewById(R.id.LoginButton)
        registerButton = view.findViewById(R.id.RegisterButton)

        database  = Firebase.database;
        myref = database.reference;


        loginButton.setOnClickListener {
            if(TextUtils.isEmpty(emailTextEdit.text.toString()) || TextUtils.isEmpty(passwordTextEdit.text.toString())){

            }
        }

        registerButton.setOnClickListener {
            if(TextUtils.isEmpty(emailTextEdit.text.toString()) || TextUtils.isEmpty(passwordTextEdit.text.toString())){
                signUp("tmp",emailTextEdit.text.toString(),passwordTextEdit.text.toString())
            }
        }
    }

    private fun logIn(email: String, password: String){
        authenticator.signInWithEmailAndPassword(email,password).addOnCompleteListener (this){
            if(it.isSuccessful){

            }else{

            }
        }
    }

    private fun signUp(name: String, email: String, password: String){

        // Pre-config firebase signup method
        authenticator.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success

                    val user = authenticator.currentUser;
                    val userinfo = UserInformation(name=name,email=email);
                    myref.child("users").child(user!!.uid).setValue(userinfo);

                } else {
                    // If sign in fails


                }
            }
    }
}