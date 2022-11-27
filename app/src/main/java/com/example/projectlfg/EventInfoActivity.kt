package com.example.projectlfg

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projectlfg.databinding.ActivityEventInfoBinding
import java.util.*

class EventInfoActivity:AppCompatActivity() {


    private lateinit var  EventName:EditText;
    private lateinit var DateAndTime:EditText;
    private lateinit var Attendees:EditText;
    private lateinit var Location:EditText;

    private lateinit var SignUpButton: Button;

    private lateinit var binding:ActivityEventInfoBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventInfoBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);

        EventName = binding.EventName;
        DateAndTime = binding.DateAndTime;
        Attendees = binding.Attendees;
        Location = binding.Location;
        SignUpButton = binding.SignUpEvent;

        SignUpButton.setOnClickListener {
            addCalendarEvent(view);
        }

    }

    fun addCalendarEvent(view: View){



    }
}