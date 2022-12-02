package com.example.projectlfg

import CommentInformation
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.projectlfg.databinding.ActivityCommentListviewBinding
import com.example.projectlfg.databinding.ActivityEventInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EventCommentsActivity:AppCompatActivity() {
    private lateinit var binding:ActivityCommentListviewBinding
    private lateinit var CommentView:ListView;
    private var EventId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentListviewBinding.inflate(layoutInflater)
        val view = binding.root;
        setContentView(view);
        CommentView = binding.commentslistview;

        EventId = intent.getStringExtra("key")!!;

        getMyComments()
    }
    fun GetFromUserDb(creatorid:String,commentInformation: CommentInformation,listener: OnGetDataListener){
        val userdb = FirebaseDatabase.getInstance().reference.child("users").child(creatorid)
        userdb.get().addOnSuccessListener {
            if(it.value != null){
                val data= it.value as HashMap<String,*>;
                val name = data.get("name") as String;
                val imguri = data.get("imageuri") as String;
                listener.onSuccess(commentInformation, name = name,imguri=imguri);
            }
        };
    }



    fun getMyComments(){
        GlobalScope.launch {

            val checklistener = object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var commentsarr = ArrayList<CommentInformation>();
                    val tmpsnapshot = snapshot;

                    if(snapshot.exists() && snapshot.hasChild("comments") ){

                        val tmp = snapshot.child("comments").value as HashMap<String,*>
                        val tmpsize = tmp.size
                        var counter = 0 ;
                        for((key,value) in tmp){
                            val tmpvalue = value as HashMap<String,*>
                            val creatorid = tmpvalue.get("creatorid") as String;
                            val datestr= tmpvalue.get("date") as String
                            val rating = tmpvalue.get("rating") as Long
                            val title = tmpvalue.get("titletext") as String
                            val comment = tmpvalue.get("comments") as String
                            val goagainstr= tmpvalue.get("goagainstr") as String;

                            var tmpcomment = CommentInformation(comments = comment ,date=datestr,
                                rating= rating.toFloat(), titletext = title, goagainstr = goagainstr)

                            GetFromUserDb(creatorid,tmpcomment,object:OnGetDataListener{
                                override fun onSuccess(
                                    commentInformation: CommentInformation,
                                    name: String,
                                    imguri: String
                                ) {
                                    commentInformation.creator = name;
                                    commentInformation.imguri=imguri;
                                    commentsarr.add(commentInformation);

                                    val adapter = CommentAdapter(commentsarr);
                                    CommentView.adapter = adapter;
                                }
                            })

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }

            }

            val keystr = intent.getStringExtra("key")
            FirebaseDatabase.getInstance().reference.child("events1")
                .child(keystr!!).addValueEventListener(checklistener)
        }
    }
}


class CommentAdapter(mlist:ArrayList<CommentInformation>): BaseAdapter(){
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
        view.findViewById<TextView>(R.id.comment_date_time).setText(commentlist[position].date)
        view.findViewById<TextView>(R.id.comment_user).setText(commentlist[position].creator)
        view.findViewById<TextView>(R.id.comment_text).setText(commentlist[position].comments)
        view.findViewById<RatingBar>(R.id.fixedratingbar).rating = commentlist.get(position).rating
        view.findViewById<TextView>(R.id.titletextview).setText("Review: ${commentlist.get(position).titletext}   Would Go Again? ${commentlist.get(position).goagainstr}")
        view.findViewById<ImageView>(R.id.cryingcatimg).setImageURI(commentlist.get(position).imguri.toUri())
        return view;
    }

}