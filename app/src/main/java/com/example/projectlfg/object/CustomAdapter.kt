package com.example.projectlfg.`object`

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectlfg.R
import com.example.projectlfg.UserInformation

class CustomAdapter(val context: Context, val userList: ArrayList<UserInformation>): RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val contactName = itemView.findViewById<TextView>(R.id.contactName)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.contactlistlayout, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val currentContact = userList[position]
        holder.contactName.text = currentContact.name

    }



    override fun getItemCount(): Int {
        return userList.size
    }
}