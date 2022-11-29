package com.example.projectlfg

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.projectlfg.databinding.ActivityEventHistoryRowBinding
import com.example.projectlfg.databinding.ActivityEventsHistoryBinding

class UserHistoryActivity:AppCompatActivity() {
    private lateinit var binding:ActivityEventsHistoryBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsHistoryBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);

        
    }
}