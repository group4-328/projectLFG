package com.example.projectlfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectlfg.MainActivity.Companion.currentUser
import com.example.projectlfg.MainActivity.Companion.myref
import com.example.projectlfg.Util.CHAT_INDIVIDUAL
import com.example.projectlfg.databinding.ActivityChatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: MsgAdapter
    private lateinit var chatView:RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button

    private lateinit var receiverUID: String
    private lateinit var sendRoomID: String
    private lateinit var receiveRoomID: String

    private lateinit var binding:ActivityChatBinding
    private lateinit var msgList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageBox = binding.chatRoomInput
        sendButton = binding.chatRoomSend
        msgList = arrayListOf()

        chatView = binding.chatRoomMsgs
        chatView.layoutManager = LinearLayoutManager(this)
        adapter = MsgAdapter(this, msgList)
        chatView.adapter = adapter


        val type = intent.getStringExtra("type")
        val name = intent.getStringExtra("name")
        binding.chatRoomName.text = name

        // Individual chat
        if(type == CHAT_INDIVIDUAL){
            receiverUID = intent.getStringExtra("receiver")!!

            sendRoomID = currentUser!!.uid + receiverUID
            receiveRoomID = receiverUID + currentUser!!.uid

            myref.child("chatroom").child(receiveRoomID).child("msgs")
                .addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        msgList.clear()

                        for(msg in snapshot.children){

                            val text = msg.child("msg").getValue()
                            val sender = msg.child("sender").getValue()

                            msgList.add(Message(text.toString(), sender.toString()))
                        }

                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println(error)
                    }
                })

            sendButton.setOnClickListener {

                val message = Message(messageBox.text.toString(), currentUser!!.uid.toString())

                myref.child("chatroom").child(sendRoomID).child("msgs").push()
                    .setValue(message).addOnCompleteListener {
                        myref.child("chatroom").child(receiveRoomID).child("msgs").push()
                            .setValue(message)
                    }

                messageBox.setText("")
                messageBox.hint = ""
            }

        // group chat
        }else{

            receiveRoomID = intent.getStringExtra("eventID").toString()

            myref.child("events1").child(receiveRoomID).child("chat")
                .addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        msgList.clear()

                        for(msg in snapshot.children){

                            val text = msg.child("msg").getValue()
                            val sender = msg.child("sender").getValue()

                            msgList.add(Message(text.toString(), sender.toString()))
                        }

                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println(error)
                    }
                })


            sendButton.setOnClickListener {

                val message = Message(messageBox.text.toString(), currentUser!!.uid.toString())


                myref.child("events1").child(receiveRoomID).child("chat").push()
                    .setValue(message)

                messageBox.setText("")
                messageBox.hint = ""
            }
        }
    }
}