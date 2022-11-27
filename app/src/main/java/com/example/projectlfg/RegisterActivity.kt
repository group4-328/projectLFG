package com.example.projectlfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Unknown file name conflict with 'activity_register' possibly due to git
        setContentView(R.layout.activity_register2)
    }

    /*
    * if(nameLayout.visibility != View.GONE) {
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
    * */
}