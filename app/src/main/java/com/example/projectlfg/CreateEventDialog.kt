package com.example.projectlfg

import DBEventsInformation
import EventsInformation
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.DialogInterface
import android.icu.util.Calendar
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CreateEventDialog: DialogFragment(), DialogInterface.OnClickListener, OnDateSetListener, OnTimeSetListener {

    companion object {
        val latLngKey = "lat_lng_key"
        val dialogTitleKey = "DialogTitle"
        val dialogKey = "Dialog"
    }

    private lateinit var nameEditText: EditText
    private lateinit var dateText: TextView
    private lateinit var timeText: TextView
    private lateinit var locationEditText: EditText
    private lateinit var informationEditText: EditText
    private lateinit var attendantsnumber:EditText;

    private var address: Address? = null

    private var latLng: LatLng? = null
    private val calendar = Calendar.getInstance()

    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var min = 0
    private var sec = 0


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var dialog: Dialog
        lateinit var view: View

        // build dialog
        var dialogTitle = arguments?.getString(dialogTitleKey)
        latLng = arguments?.getParcelable(latLngKey)
        if (dialogTitle == null) { dialogTitle = "Create New Event" }
        val builder = AlertDialog.Builder(requireActivity())

        view = requireActivity().layoutInflater.inflate(R.layout.dialog_create_event, null)



        initUIElements(view)
        onDateSet(
            null,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        onTimeSet(
            null,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )

        dateText.setOnClickListener(View.OnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireActivity(), this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        })
        timeText.setOnClickListener(View.OnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireActivity(), this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePickerDialog.show()
        })

        // handle inputs
        setAddressText()

        builder.setView(view)
        builder.setTitle(dialogTitle)
        builder.setPositiveButton("ok",this)
        builder.setNegativeButton("cancel", this)
        dialog = builder.create()

        return dialog
    }

    fun InsertNewEvent(){
        val db = FirebaseDatabase.getInstance().reference
        val eventinfo = EventsInformation(name = nameEditText.text.toString(),
            startingdate = dateText.text.toString(), endtime=dateText.text.toString(),
            attendess = attendantsnumber.text.toString().toLong(),
            location =  locationEditText.text.toString())
        val randomid = UUID.randomUUID()
        db.child("events").child(randomid.toString()).setValue(eventinfo)
        db.child("events").child(randomid.toString()).child("information").setValue(informationEditText.text.toString());

    }

    fun initUIElements(view: View) {
        nameEditText = view.findViewById(R.id.event_name_editText)
        dateText = view.findViewById(R.id.date_text)
        timeText = view.findViewById(R.id.time_text)
        locationEditText = view.findViewById(R.id.location_editText)
        informationEditText = view.findViewById(R.id.information_editText)
        attendantsnumber = view.findViewById(R.id.attendants_edittext)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        // If ok, need to save info into database
        if (which == Dialog.BUTTON_POSITIVE) {
            // gather all enter information and save
            if (nameEditText.text.toString() == "" || locationEditText.text.toString() == "") {

            }
            else {
                // save to database here
//                var string = ""
//                string += "Debug: Name=${nameEditText.text}\nLat=${latLng?.latitude} Lng=${latLng?.longitude}\n"
//                string += "Address="
                if (latLng == null) {
                    latLng = getLatLngFromAddress(locationEditText.text.toString())
                }
//                if (latLng != null) {
//                    address = getAddressFromLatLng(latLng!!)
//                    for (i in 0..address?.maxAddressLineIndex!!) {
//                        string += address?.getAddressLine(i)
//                    }
//                }
//                string += "\nDate=$day/$month/$year Time=$hour:$min:00\n"
//                if (informationEditText.text != null) {
//                    string += "Info: ${informationEditText.text}"
//                }
//
//                println("Debug: latlng from address: lat=${latLng?.latitude}lng=${latLng?.longitude}")
//                println(string)
//                dismiss()
                if(latLng == null){
                    return;
                }

                val curruser = FirebaseAuth.getInstance().currentUser
                val dateformat = "${day}/${month}/${year} ${hour}:${min}:00"
                val uid = UUID.randomUUID().toString()

                var newinfo = DBEventsInformation(
                    name =nameEditText.text.toString(), startingdate = dateformat ,
                                                attendess = attendantsnumber.text.toString().toLong(),
                    location =locationEditText.text.toString(),
                                                latLng = latLng!!, information = informationEditText.text.toString(),
                                            creator = curruser!!.uid,id=uid)
                val db = FirebaseDatabase.getInstance().reference.child("events1").child(uid)
                db.setValue(newinfo);
                dismiss()
            }

        }
        // If cancel, just dismiss the dialog
        else {
            dismiss()
        }
    }

    fun setAddressText() {
        if (latLng != null) {
            address = getAddressFromLatLng(latLng!!)
            var addressString = ""
            for (i in 0 .. address!!.maxAddressLineIndex) {
                addressString += address?.getAddressLine(i)
            }
            locationEditText.setText(addressString)
        }
    }

    fun getAddressFromLatLng(latLng: LatLng): Address? {
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        val address = addresses?.get(0)
        return address
    }

    fun getLatLngFromAddress(addressString: String): LatLng? {
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        val addresses = geocoder.getFromLocationName(addressString, 1)
        val address = addresses?.get(0)
        if (address != null) {
            val lat = address.latitude
            val lng = address.longitude
            val latLng = LatLng(lat, lng)
            return latLng
        }
        return null
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month + 1
        this.day = dayOfMonth
        dateText.setText("${this.day}/${this.month}/${this.year}")
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        this.hour = hourOfDay
        this.min = minute
        var hr = ""
        if (this.hour < 10) { hr += "0" }
        hr += this.hour
        var min = ""
        if (this.min < 10) { min += "0" }
        min += this.min
        timeText.setText("${hr}:${min}:00")
    }
}