package com.example.projectlfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var emailTextEdit: EditText
    private lateinit var passwordTextEdit: EditText
    private lateinit var nameLayout: View
    private lateinit var nameTextEdit: EditText
    private lateinit var logoView: ImageView

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    //authentication
    private lateinit var authenticator: FirebaseAuth

    //database
    private lateinit var database: FirebaseDatabase

    //ref
    private lateinit var myref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get view
        emailTextEdit = findViewById(R.id.emailtextview)
        passwordTextEdit = findViewById(R.id.passwordtextview);
        loginButton = findViewById(R.id.LoginButton)
        registerButton = findViewById(R.id.RegisterButton)
        nameLayout = findViewById(R.id.layout0)
        nameTextEdit = findViewById(R.id.nametextview)

        // firebase init
        authenticator = FirebaseAuth.getInstance()
        database  = Firebase.database;
        myref = database.reference;

        // UI init
        logoView = findViewById(R.id.mainMenuLogo)
        logoView.setImageResource(R.drawable.logo)
        nameLayout.visibility = View.GONE

        loginButton.setOnClickListener {
            if(!TextUtils.isEmpty(emailTextEdit.text.toString()) && !TextUtils.isEmpty(passwordTextEdit.text.toString())){
                logIn(emailTextEdit.text.toString(), passwordTextEdit.text.toString())
            }else{
                popUp("please fill in the information")
            }
        }

        registerButton.setOnClickListener {
            if(nameLayout.visibility != View.GONE) {
                if (!TextUtils.isEmpty(emailTextEdit.text.toString()) && !TextUtils.isEmpty(passwordTextEdit.text.toString()
                    )
                ) {
                    signUp(nameTextEdit.toString(), emailTextEdit.text.toString(), passwordTextEdit.text.toString())
                } else {
                    popUp("not enough information")
                }
            }else{
                nameLayout.visibility = View.VISIBLE
                popUp("Please fill out your name")
            }
        }
    }

    private fun logIn(email: String, password: String){
        authenticator.signInWithEmailAndPassword(email,password).addOnCompleteListener (this){
            if(it.isSuccessful){
                // load main menu
                val startMenu = Intent(this,MainMenuActivity::class.java)
                startActivity(startMenu)
            }else{
                popUp("sign up fail, please try again")
            }
        }
    }


    private fun signUp(name: String, email: String, password: String){
        // Pre-config firebase signup method
        authenticator.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success

                    // to be edit
                    val user = authenticator.currentUser;
                    val userinfo = UserInformation(name=name,email=email)
                    myref.child("users").child(user!!.uid).setValue(userinfo)


                    logIn(email, password)
                } else {
                    // If sign in fails
                    popUp("sign up fail, please try again")
                }
            }
    }

    private fun popUp(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}