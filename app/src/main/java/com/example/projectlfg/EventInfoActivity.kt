package com.example.projectlfg

import CommentInformation
import EventsInformation
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.projectlfg.databinding.ActivityEventInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

interface SetButton{
    fun IfExistsCancelbutton(check:Boolean,id:String);
}

class EventInfoActivity:AppCompatActivity() {

    private lateinit var listview: ListView

    private lateinit var  EventName:EditText;
    private lateinit var DateAndTime:EditText;
    private lateinit var EndDateAndTime:EditText;
    private lateinit var Attendees:EditText;
    private lateinit var Location:EditText;

    private lateinit var CommentButton:Button;
    private lateinit var SignUpButton: Button;
    private lateinit var myratingbar:RatingBar;
    private lateinit var totalratingbar:RatingBar
    private lateinit var db:DatabaseReference

    private  var EventsIsAdded = false;
    private lateinit var CommentView: ListView;
    private lateinit var curruserid:String;


    private lateinit var binding:ActivityEventInfoBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventInfoBinding.inflate(layoutInflater);
        val view = binding.root;
        setContentView(view);
        curruserid = FirebaseAuth.getInstance().currentUser!!.uid.toString()


        EventName = binding.EventName;
        DateAndTime = binding.DateAndTime;
        EndDateAndTime = binding.enddate;
        Attendees = binding.Attendees;
        Location = binding.Location;
        SignUpButton = binding.SignUpEvent;
        myratingbar = binding.myratingbar;
        totalratingbar = binding.totalratingbar
        CommentButton = binding.CommentEvent

        exists();
        getMyRatings()
        getTotalRatings()

        myratingbar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val key = intent.getStringExtra("key")
            val db = FirebaseDatabase.getInstance().reference.child("events1").child(key!!).child("ratings")
            val curruser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
            db.child(curruser).setValue(rating);
        }


        CommentButton.setOnClickListener {
            val createcommentdialog = CommentDialogFragment();
            createcommentdialog.show(supportFragmentManager,"create comment")
        }


        CommentView = view.findViewById(R.id.commentslistview)
        var tmplist= ArrayList<CommentInformation>()
        tmplist.add(CommentInformation(creator = "dd", date ="dd", comments = "dd", creatorid = "dd",rating=0.0f))
        val adapter = CommentAdapter(tmplist);
        CommentView.adapter = adapter


        db = FirebaseDatabase.getInstance().reference;

        EventName.setText(intent.getStringExtra(MapsActivity.NAME));
        DateAndTime.setText(intent.getStringExtra(MapsActivity.STARTINGDATE))
        EndDateAndTime.setText(intent.getStringExtra(MapsActivity.STARTINGDATE))
        Attendees.setText(intent.getLongExtra("Attendants",0).toString())
        Location.setText(intent.getStringExtra("LOCATION"));
    }

    fun getMyRatings(){
        GlobalScope.launch {
            val keystr = intent.getStringExtra("key")
            val tmpdb = FirebaseDatabase.getInstance().reference.child("events1").child(keystr!!).child("ratings").child(curruserid)
            tmpdb.get().addOnSuccessListener {
                if(it.value != null){
                    val data = it.value as Long;
                    myratingbar.rating = data.toFloat();
                }
            }
        }
    }

    fun getTotalRatings(){
        GlobalScope.launch{
            val keystr = intent.getStringExtra("key");
            val tmpdb = FirebaseDatabase.getInstance().reference.child("events1").child(keystr!!).child("ratings")
            tmpdb.get().addOnSuccessListener {
                if(it.value!=null){
                    val data = it.value as HashMap<String,Long>;
                    var total  = 0.0;
                    var people = 0;
                    for((key,value) in data){
                        val num = value.toFloat();
                        total = total+num;
                        people+=1;
                    }
                    totalratingbar.rating = (total/people).toFloat();
                }
            }
        }
    }

    fun exists(){

        GlobalScope.launch{

            val checklistener = object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists() && snapshot.hasChild("people")){
                        val data = snapshot.child("people")

                        if(data.hasChild(curruserid)){
                            SignUpButton.setText("Remove")
                            val keystr = intent.getStringExtra("key")
                            SignUpButton.setOnClickListener {
                                FirebaseDatabase.getInstance().reference.child("events1").child(keystr!!).
                                child("people").child(curruserid).removeValue()
                                val userevents =FirebaseDatabase.getInstance().reference.child("users").
                                child(curruserid).child("events").child(keystr!!).removeValue()
                            }
                        }else{
                            SignUpButton.setText("Sign Up")
                            SignUpButton.setOnClickListener {
                                addToDatabase()
                            }
                        }
                    }else{
                        SignUpButton.setText("Sign Up")
                        SignUpButton.setOnClickListener {
                            addToDatabase()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            }
            val tmpdb = FirebaseDatabase.getInstance().reference
            val keystr = intent.getStringExtra("key")
            tmpdb.child("events1").child(keystr!!).addValueEventListener(checklistener);
        }
    }

    fun addToDatabase(){
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
            val keystr = intent.getStringExtra("key")
            db.child("users").child(userid).child("events").child(keystr!!).setValue(intent.getStringExtra("key"));
            db.child("events1").child(keystr!!).child("people").child(curruserid).setValue(curruserid)
        }
    }

}

class CommentAdapter(mlist:ArrayList<CommentInformation>):BaseAdapter(){
    private lateinit var commentlist:ArrayList<CommentInformation>;
    init{
        commentlist = mlist;
    }
    override fun getCount(): Int {
        return commentlist.size;
    }

    override fun getItem(position: Int): Any {
        return commentlist.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.user_comment_view,parent,false);
        return view;
    }

}