package com.example.projectlfg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectlfg.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {

    private lateinit var adapter: MsgAdapter
    private lateinit var chatView:RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button

    private lateinit var binding:ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        binding.chatRoomName.text = name

        chatView = binding.chatRoomMsgs
        adapter = MsgAdapter(this, arrayListOf())
        chatView.adapter = adapter
    }
}