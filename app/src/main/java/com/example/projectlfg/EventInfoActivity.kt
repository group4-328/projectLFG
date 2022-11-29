package com.example.projectlfg

import EventsInformation
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.projectlfg.databinding.ActivityEventInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*

class EventInfoActivity:AppCompatActivity() {


    private lateinit var  EventName:EditText;
    private lateinit var DateAndTime:EditText;
    private lateinit var EndDateAndTime:EditText;
    private lateinit var Attendees:EditText;
    private lateinit var Location:EditText;

    private lateinit var SignUpButton: Button;

    private lateinit var db:DatabaseReference

    private lateinit var binding:ActivityEventInfoBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventInfoBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);

        EventName = binding.EventName;
        DateAndTime = binding.DateAndTime;
        EndDateAndTime = binding.enddate;
        Attendees = binding.Attendees;
        Location = binding.Location;
        SignUpButton = binding.SignUpEvent;

        SignUpButton.setOnClickListener {
//            addCalendarEvent(view);
            addToDatabase("tmpevent")
        }

        db = FirebaseDatabase.getInstance().reference;


    }

    fun addToDatabase(eventid:String){
        val curruser = FirebaseAuth.getInstance().currentUser;
        if(curruser != null){
            val userid = curruser.uid;
            val eventname = EventName.text.toString()
            val startingdate = DateAndTime.text.toString()
            val enddate = EndDateAndTime.text.toString()
            val attendess :Int=  Attendees.text.toString().toInt();
            val locationstr = Location.text.toString();

            val eventinfo = EventsInformation(name=eventname,startingdate=startingdate, endtime = enddate, attendess = attendess,location=locationstr)

            db.child("users").child(userid).child("events").push().setValue(eventinfo);
        }
    }

    fun addCalendarEvent(view: View){



    }
}