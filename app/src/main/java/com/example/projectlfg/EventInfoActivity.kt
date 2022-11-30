package com.example.projectlfg

import DBEventsInformation
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
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

interface SetButton{
    fun IfExistsCancelbutton(check:Boolean,id:String);
}

class EventInfoActivity:AppCompatActivity() {


    private lateinit var  EventName:EditText;
    private lateinit var DateAndTime:EditText;
    private lateinit var EndDateAndTime:EditText;
    private lateinit var Attendees:EditText;
    private lateinit var Location:EditText;

    private lateinit var SignUpButton: Button;

    private lateinit var db:DatabaseReference

    private  var EventsIsAdded = false;

    private lateinit var binding:ActivityEventInfoBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventInfoBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);
        val currid = FirebaseAuth.getInstance().currentUser!!.uid.toString()


        EventName = binding.EventName;
        DateAndTime = binding.DateAndTime;
        EndDateAndTime = binding.enddate;
        Attendees = binding.Attendees;
        Location = binding.Location;
        SignUpButton = binding.SignUpEvent;

        exists(object:SetButton{
            override fun IfExistsCancelbutton(check: Boolean,id:String) {
                if(!check){
                    SignUpButton.setText("Sign Up")
                    SignUpButton.setOnClickListener {
                        addToDatabase("tmpevent")
                        SignUpButton.setText("Delete")
                        SignUpButton.setOnClickListener {
//                        addToDatabase("tmpevent")
                            SignUpButton.setText("Sign Up")
                            val db = FirebaseDatabase.getInstance().reference.child("users").child(currid).child("events").child(id).removeValue()
                        }
                    }



                }else{
                    SignUpButton.setText("Delete")
                    SignUpButton.setOnClickListener {
//                        addToDatabase("tmpevent")
                        SignUpButton.setText("Sign Up")
                        val db = FirebaseDatabase.getInstance().reference.child("users").child(currid).child("events").child(id).removeValue()
                        DeleteFromDb()

                    }
                }
            }

        })

        db = FirebaseDatabase.getInstance().reference;

//        val info = intent.getSerializableExtra(MapsActivity.INFO) as DBEventsInformation;
//        EventName.setText(info.name)
        EventName.setText(intent.getStringExtra(MapsActivity.NAME));
        DateAndTime.setText(intent.getStringExtra(MapsActivity.STARTINGDATE))
        EndDateAndTime.setText(intent.getStringExtra(MapsActivity.STARTINGDATE))
        Attendees.setText(intent.getLongExtra("Attendants",0).toString())

    }

    fun exists(setButton: SetButton){
        val currid = FirebaseAuth.getInstance().currentUser!!.uid;
        val db = FirebaseDatabase.getInstance().reference.child("users").child(currid).child("events")
        db.get().addOnSuccessListener {
            var tmpcheck = false;
            if(it.value != null){
                val data = it.value as HashMap<String,String>
                if(data != null){
                    for((key,value) in data){
                        val info = data.get(key);
                        if(info == intent.getStringExtra("key")){
                            tmpcheck = true;
                            setButton.IfExistsCancelbutton(true,key);
                            break;
                        }
                    }
                    if(!tmpcheck){
                        setButton.IfExistsCancelbutton(false,"");
                    }
                }
            }else{
                setButton.IfExistsCancelbutton(false,"");
            }

        }


    }


    fun DeleteFromDb(){
        SignUpButton.setText("Sign Up")
        SignUpButton.setOnClickListener {
            addToDatabase("tmpevent")
        }
    }


    fun addToDatabase(eventid:String){
        val curruser = FirebaseAuth.getInstance().currentUser;
        if(curruser != null){
            val userid = curruser.uid;
            val eventname = EventName.text.toString()
            val startingdate = DateAndTime.text.toString()
            val enddate = EndDateAndTime.text.toString()
            val attendess =  Attendees.text.toString().toLong();
            val locationstr = Location.text.toString();
            val eventinfo = EventsInformation(name=eventname,startingdate=startingdate,
                endtime = enddate, attendess = attendess,location=locationstr)
            val randomid = UUID.randomUUID().toString()
            db.child("users").child(userid).child("events").child(randomid).setValue(intent.getStringExtra("key"));
            SignUpButton.setOnClickListener {
                SignUpButton.setText("Delete")
                val db = FirebaseDatabase.getInstance().reference.child("users").child(userid).child("events").child(randomid).removeValue()
            }
        }
    }

    fun addCalendarEvent(view: View){



    }
}