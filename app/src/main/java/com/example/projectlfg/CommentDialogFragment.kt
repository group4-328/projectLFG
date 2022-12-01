package com.example.projectlfg

import CommentInformation
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.projectlfg.databinding.ActivityLeaveCommentBinding
import com.google.firebase.auth.FirebaseAuth

class CommentDialogFragment:DialogFragment() , DialogInterface.OnClickListener{

    private lateinit var titlespinner: Spinner;
    private lateinit var GoAgain:Spinner;
    private lateinit var comments:EditText;
    private lateinit var savebutton: Button;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.activity_leave_comment,container,false);
        titlespinner = view.findViewById(R.id.TitleSpinner)
        GoAgain = view.findViewById(R.id.GoAgainSpinner)
        val arrayadapter= ArrayAdapter.createFromResource(requireContext(),R.array.TitleSpinner,android.R.layout.simple_spinner_item)
        titlespinner.adapter = arrayadapter

        val arrayadapter2= ArrayAdapter.createFromResource(requireContext(),R.array.GoAgain,android.R.layout.simple_spinner_item)
        GoAgain.adapter = arrayadapter2

        comments = view.findViewById(R.id.comment_area)

        savebutton = view.findViewById(R.id.savebutton)
        savebutton.setOnClickListener {
            val curruser = FirebaseAuth.getInstance().currentUser!!.uid
//            val comment = CommentInformation(creatorid = curruser, date = ,
//                comments = comments.text.toString(),rating=0.0f)
            dismiss()
        }

        return view;
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(requireActivity())

        return builder.create();
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {

    }

}