package com.example.projectlfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectlfg.MainActivity.Companion.authenticator
import com.example.projectlfg.ContactAdapter
import com.example.projectlfg.databinding.ActivityContactListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var binding: ActivityContactListBinding

class ContactListActivity : AppCompatActivity() {

    private lateinit var contactList: ArrayList<UserInformation>
    private lateinit var adapter: ContactAdapter
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initilize firebase-related content
        databaseRef = Firebase.database.reference
        contactList = arrayListOf()


        adapter = ContactAdapter(this, contactList)
        val contactView: RecyclerView = binding.userContactList

        contactView.layoutManager = LinearLayoutManager(this)
        contactView.adapter = adapter

        databaseRef.child("users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                contactList.clear()
                println("adding users..")

                // adding users in friend list to contact list view
                for(users in snapshot.children){

                    val name = users.child("name").value.toString()
                    val email = users.child("email").value.toString()

                    println(name + " " + email)

                    contactList.add(UserInformation(name, email, ""))
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                println(error)
            }

        })

    }
}