package com.example.projectlfg

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectlfg.MainActivity.Companion.authenticator
import com.google.firebase.ktx.Firebase

class MsgAdapter(val context: Context, val msgList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){



    class SentMsgHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val msg = itemView.findViewById<TextView>(R.id.sentMsg)
    }

    class ReceiveMsgHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val msg = itemView.findViewById<TextView>(R.id.receiveMsg)
    }

    // return proper view type to determin sent/receive msg type
    override fun getItemViewType(position: Int): Int {

        val currentTxt = msgList[position]

        // sent msg
        if(authenticator.currentUser!!.uid.equals(currentTxt.sender)){
            return 1

        //  receive msg
        }
        return 2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        lateinit var view: View

        // if it's a sent msg
        if(viewType == 1){
            view = LayoutInflater.from(context).inflate(R.layout.sentmsg, parent, false)
            return SentMsgHolder(view)
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.receivemsg, parent, false)
            return ReceiveMsgHolder(view)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentTxt = msgList[position]

        // if it's a sent msg
        if(holder.javaClass == SentMsgHolder::class.java){
            val viewHolder = holder as SentMsgHolder
            holder.msg.text = currentTxt.msg

        // if it's a receive msg
        }else{
            val viewHolder = holder as ReceiveMsgHolder
            holder.msg.text = currentTxt.msg
        }
    }


    override fun getItemCount(): Int {
        return msgList.size
    }
}