package com.example.projectlfg

import EventsInformation
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.projectlfg.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {

    private lateinit var view: View;
    private lateinit var emailTextEdit:EditText;
    private lateinit var passwordTextEdit:EditText;

    private lateinit var LoginButton: Button;
    private lateinit var RegisterLink:TextView;


    //authentication
    private lateinit var authenticator: FirebaseAuth

    //database
    private lateinit var database:FirebaseDatabase;

    //ref
    private lateinit var myref : DatabaseReference;


    companion object{
        val SHARED_PREF_KEY = "shared pref"
    }

    private lateinit var binding: ActivityLogInBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_log_in)
//        view = layoutInflater.inflate(R.layout.activity_log_in,null);

        binding = ActivityLogInBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view) ;
        emailTextEdit = view.findViewById(R.id.emailtextview)
        passwordTextEdit = view.findViewById(R.id.passwordtextview);
        LoginButton = view.findViewById(R.id.LoginButton)
        RegisterLink = view.findViewById(R.id.registerlink);
        authenticator = FirebaseAuth.getInstance();



        database  = Firebase.database;
        myref = database.reference;


        LoginButton.setOnClickListener {
            if(!TextUtils.isEmpty(emailTextEdit.text.toString()) && !TextUtils.isEmpty(passwordTextEdit.text.toString())){
                logIn(emailTextEdit.text.toString(),passwordTextEdit.text.toString())
            }
        }

        RegisterLink.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java);
            startActivity(intent);
        }
    }

    private fun logIn(email: String, password: String){
        authenticator.signInWithEmailAndPassword(email,password).addOnCompleteListener (this){
            if(it.isSuccessful){
//                val intent = Intent(this,MainMenuActivity::class.java)
                val intent = Intent(this,EventInfoActivity::class.java);
                startActivity((intent))
            }else{

            }
        }
    }


}