package com.example.projectlfg

import EventsInformation
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectlfg.databinding.ActivityEventHistoryRowBinding
import com.example.projectlfg.databinding.ActivityEventsHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

interface FirebaseCallBackList{
    fun onCallBack(list:List<EventsInformation>)
}



class UserHistoryActivity:AppCompatActivity() {
    private lateinit var binding:ActivityEventsHistoryBinding;

    private lateinit var recyclerView: RecyclerView

    fun readFromDB(firebaseCallBackList: FirebaseCallBackList,db:DatabaseReference,curruserid:String){
        db.child("users").child(curruserid).child("events").get().addOnSuccessListener {
            val eventList : ArrayList<EventsInformation> = ArrayList();
            if(it.value == null) {

            }else{
                val listEvents = it.value as HashMap<String, *>
                for((key,value) in listEvents){
                    val tmp = listEvents.get(key) as HashMap<String,*> ;
                    val name = tmp.get("name") as String;
                    val startingdate = tmp.get("startingdate") as String;
                    val endtime = tmp.get("endtime") as String
                    val location = tmp.get("location") as String
                    val attendees = tmp.get("attendess") as Long;
                    eventList.add(EventsInformation(name = name!! , startingdate = startingdate,
                        endtime = endtime, location = location, attendess = attendees))
            }

            }
            firebaseCallBackList.onCallBack(eventList);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsHistoryBinding.inflate(layoutInflater);
        val view = binding.root;

        val curruser = FirebaseAuth.getInstance().currentUser;
        val curruserid = curruser!!.uid;

        recyclerView = view.findViewById(R.id.EventsHistoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val db = FirebaseDatabase.getInstance().reference

        readFromDB(object: FirebaseCallBackList{
            override fun onCallBack(list: List<EventsInformation>) {
                recyclerView.adapter = CustomAdapter(list);
            }
        },db,curruserid);
        setContentView(view);
    }
}
class CustomAdapter( mlist:List<EventsInformation>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
    private lateinit var eventlist:List<EventsInformation>;
    init{
        eventlist = mlist;
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_event_history_row,parent,false);
        return ViewHolder((view));
    }

    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
            val eventobj = eventlist[position]
        holder.eventname.setText(eventobj.name.toString())
        holder.historyinfo.setText("${eventobj.location.toString()} · ${eventobj.startingdate.toString()}")
    }

    override fun getItemCount(): Int {
        return eventlist.size;
    }

    class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val eventname = itemview.findViewById<TextView>(R.id.HistoryEventName)
        val historyinfo = itemview.findViewById<TextView>(R.id.HistoryInfo)
        fun bind(info:EventsInformation){

        }
    }

}