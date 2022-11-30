package com.example.projectlfg

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.text.Layout
import android.text.TextUtils
import android.widget.*
import com.example.projectlfg.Util.popUp
import com.example.projectlfg.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    // To be used possibly with screen updates and login
    private var loginMade = true

    private lateinit var emailTextEdit: EditText
    private lateinit var passwordTextEdit: EditText
    private lateinit var logoView: ImageView

    private lateinit var loginButton: Button
    private lateinit var registerButton: TextView

    private lateinit var binding: ActivityMainBinding

    //authentication
    private lateinit var authenticator: FirebaseAuth

    //database
    private lateinit var database: FirebaseDatabase

    //ref
    private lateinit var myref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // get view
        emailTextEdit = binding.emailtextview
        passwordTextEdit = binding.passwordtextview
        loginButton = binding.LoginButton
        registerButton = binding.registerlink

        // firebase init ()
        authenticator = FirebaseAuth.getInstance()
        database = Firebase.database;
        myref = database.reference;

        // UI init
        logoView = findViewById(R.id.mainMenuLogo)
        logoView.setImageResource(R.drawable.logo)

        loginButton.setOnClickListener {
            if (!TextUtils.isEmpty(emailTextEdit.text.toString()) && !TextUtils.isEmpty(
                    passwordTextEdit.text.toString()
                )
            ) {
                logIn(emailTextEdit.text.toString(), passwordTextEdit.text.toString())
            } else {
                popUp(this, "please fill in user information")
            }
        }

        registerButton.setOnClickListener {
            // Open register activity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun logIn(email: String, password: String) {
        authenticator.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                // load main menu

                // set currentUser information
                myref.child("users").child(authenticator.currentUser!!.uid).get().addOnSuccessListener {
                    currentUser = UserInformation(it.child("name").value.toString(), it.child("email").value.toString(), it.child("uid").value.toString(), it.child("uid").value.toString())
                    popUp(this, "Welcome back, " + it.child("name").value.toString())
                    //println(authenticator.currentUser!!.uid)
                    //println(currentUser!!.name)
                    //println(currentUser!!.email)
                }.addOnFailureListener {

                }
                val startMenu = Intent(this,MainMenuActivity::class.java)
                startActivity(startMenu)
                finish();
            } else {
                popUp(this, "log in fail, please try again")
            }
        }
    }

    companion object{

        //authentication
        lateinit var authenticator: FirebaseAuth

        //database
        lateinit var database: FirebaseDatabase

        //ref
        lateinit var myref : DatabaseReference

        var currentUser: UserInformation? = UserInformation()
    }

}