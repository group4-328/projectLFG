package com.example.projectlfg

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ContactAdapter(val context: Context, val userList: ArrayList<UserInformation>): RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val contactName: TextView = itemView.findViewById<TextView>(R.id.contactName)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.contactlistlayout, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        val currentContact = userList[position]
        holder.contactName.text = currentContact.name

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentContact.name)
            context.startActivity(intent)
        }
    }



    override fun getItemCount(): Int {
        return userList.size
    }
}