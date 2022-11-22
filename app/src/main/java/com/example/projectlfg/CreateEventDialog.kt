package com.example.projectlfg

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class CreateEventDialog: DialogFragment(), DialogInterface.OnClickListener {

    private val dialogTitleKey = "DialogTitle"
    private val dialogKey = "Dialog"

    private lateinit var nameEditText: EditText
    private lateinit var dateText: TextView
    private lateinit var timeText: TextView
    private lateinit var locationEditText: EditText
    private lateinit var informationEditText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog: Dialog
        lateinit var view: View

        // build dialog
        var dialogTitle = arguments?.getString(dialogTitleKey)
        if (dialogTitle == null) { dialogTitle = "Create New Event" }
        val builder = AlertDialog.Builder(requireActivity())

        view = requireActivity().layoutInflater.inflate(R.layout.dialog_create_event, null)
        initUIElements(view)

        // handle inputs


        builder.setView(view)
        builder.setTitle(dialogTitle)
        builder.setPositiveButton("ok", this)
        builder.setNegativeButton("cancel", this)
        dialog = builder.create()

        return dialog
    }

    fun initUIElements(view: View) {
        nameEditText = view.findViewById(R.id.event_name_editText)
        dateText = view.findViewById(R.id.date_text)
        timeText = view.findViewById(R.id.time_text)
        locationEditText = view.findViewById(R.id.location_editText)
        informationEditText = view.findViewById(R.id.information_editText)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        // If ok, need to save info into database
        if (which == Dialog.BUTTON_POSITIVE) {
            // gather all enter information and save

            dismiss()
        }
        // If cancel, just dismiss the dialog
        else {
            dismiss()
        }
    }
}