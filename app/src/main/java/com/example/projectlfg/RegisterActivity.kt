package com.example.projectlfg

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding


    private lateinit var NameEditText: EditText;
    private lateinit var EmailEditText: EditText;
    private lateinit var PasswordEditText: EditText;
    private lateinit var RegisterButton: Button;
    private lateinit var UserImageView : ImageView;

    private lateinit var authenticator: FirebaseAuth;
    private lateinit var myref : DatabaseReference;
    private lateinit var storage: FirebaseStorage;
    private lateinit var storageRef : StorageReference

    private var imageUri: Uri?=null;
    private lateinit var ImageGalleryIntent : ActivityResultLauncher<Intent>


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

    companion object{
        val PICK_IMAGE = 100;
    }
}