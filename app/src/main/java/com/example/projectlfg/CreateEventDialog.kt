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
import com.example.projectlfg.`object`.EventInformation
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class CreateEventDialog: DialogFragment(), DialogInterface.OnClickListener, OnDateSetListener, OnTimeSetListener {

    companion object {
        val latLngKey = "lat_lng_key"
        val dialogTitleKey = "DialogTitle"
        val dialogKey = "Dialog"
    }

    private lateinit var myref : DatabaseReference;

    private lateinit var nameEditText: EditText
    private lateinit var startDateText: TextView
    private lateinit var startTimeText: TextView
    private lateinit var endDateText: TextView
    private lateinit var endTimeText: TextView
    private lateinit var locationEditText: EditText
    private lateinit var capacityEditText: EditText
    private lateinit var informationEditText: EditText
    private lateinit var attendantsnumber:EditText;

    private lateinit var startDatePickerDialog: DatePickerDialog
    private lateinit var startTimePickerDialog: TimePickerDialog
    private lateinit var endDatePickerDialog: DatePickerDialog
    private lateinit var endTimePickerDialog: TimePickerDialog

    private var address: Address? = null

    private var latLng: LatLng? = null

    private var startTime = Calendar.getInstance()
    private var endTime = Calendar.getInstance()


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

        setStartDateTimeText()
        setEndDateTimeText()

        startDateText.setOnClickListener(View.OnClickListener {

            startDatePickerDialog.show()
        })
        startTimeText.setOnClickListener(View.OnClickListener {

            startTimePickerDialog.show()
        })
        endDateText.setOnClickListener(View.OnClickListener {

            endDatePickerDialog.show()
        })
        endTimeText.setOnClickListener(View.OnClickListener {

            endTimePickerDialog.show()
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

    fun setStartDateTimeText() {
        startDateText.setText(
            "${startTime.get(Calendar.YEAR)}/${startTime.get(Calendar.MONTH) + 1}/${startTime.get(Calendar.DAY_OF_MONTH)}"
        )
        var hrString = ""
        if (startTime.get(Calendar.HOUR_OF_DAY) < 10) { hrString += "0" }
        hrString += "${startTime.get(Calendar.HOUR_OF_DAY)}"
        var minString = ""
        if (startTime.get(Calendar.MINUTE) < 10) { minString += "0" }
        minString += "${startTime.get(Calendar.MINUTE)}"
        startTimeText.setText("$hrString:$minString:00")
    }

    fun setEndDateTimeText() {
        endDateText.setText(
            "${endTime.get(Calendar.YEAR)}/${endTime.get(Calendar.MONTH) + 1}/${endTime.get(Calendar.DAY_OF_MONTH)}"
        )
        var hrString = ""
        if (endTime.get(Calendar.HOUR_OF_DAY) < 10) { hrString += "0" }
        hrString += "${endTime.get(Calendar.HOUR_OF_DAY)}"
        var minString = ""
        if (endTime.get(Calendar.MINUTE) < 10) { minString += "0" }
        minString += "${endTime.get(Calendar.MINUTE)}"
        endTimeText.setText("$hrString:$minString:00")
    }

    fun InsertNewEvent(){
        val db = FirebaseDatabase.getInstance().reference
        val eventinfo = EventsInformation(name = nameEditText.text.toString(),
            startingdate = startDateText.text.toString(), endtime=endDateText.text.toString(),
            attendess = attendantsnumber.text.toString().toLong(),
            location =  locationEditText.text.toString())
        val randomid = UUID.randomUUID()
        db.child("events").child(randomid.toString()).setValue(eventinfo)
        db.child("events").child(randomid.toString()).child("information").setValue(informationEditText.text.toString());

    }

    fun initUIElements(view: View) {
        nameEditText = view.findViewById(R.id.event_name_editText)
        startDateText = view.findViewById(R.id.start_date_text)
        startTimeText = view.findViewById(R.id.start_time_text)
        endDateText = view.findViewById(R.id.end_date_text)
        endTimeText = view.findViewById(R.id.end_time_text)
        locationEditText = view.findViewById(R.id.location_editText)
        informationEditText = view.findViewById(R.id.information_editText)

        startDatePickerDialog = DatePickerDialog(
            requireActivity(), this,
            startTime.get(Calendar.YEAR),
            startTime.get(Calendar.MONTH),
            startTime.get(Calendar.DAY_OF_MONTH)
        )
        startTimePickerDialog = TimePickerDialog(
            requireActivity(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                startTime.set(Calendar.MINUTE, minute)
                var hrString = ""
                if (hourOfDay < 10) { hrString += "0" }
                hrString += "$hourOfDay"
                var minString = ""
                if (minute < 10) { minString += "0" }
                minString += "$minute"
                startTimeText.setText("$hrString:$minString:00")
            },
            startTime.get(Calendar.HOUR_OF_DAY),
            startTime.get(Calendar.MINUTE),
            false
        )
        endDatePickerDialog = DatePickerDialog(
            requireActivity(), this,
            endTime.get(Calendar.YEAR),
            endTime.get(Calendar.MONTH),
            endTime.get(Calendar.DAY_OF_MONTH)
        )
        endTimePickerDialog = TimePickerDialog(
            requireActivity(),
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                var hrString = ""
                if (hourOfDay < 10) { hrString += "0" }
                hrString += "$hourOfDay"
                var minString = ""
                if (minute < 10) { minString += "0" }
                minString += "$minute"
                endTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                endTime.set(Calendar.MINUTE, minute)
                endTimeText.setText("$hrString:$minString:00")
            },
            endTime.get(Calendar.HOUR_OF_DAY),
            endTime.get(Calendar.MINUTE),
            false
        )
        attendantsnumber = view.findViewById(R.id.attendants_edittext)
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        // If ok, need to save info into database
        if (which == Dialog.BUTTON_POSITIVE) {
            // gather all enter information and save
            if (nameEditText.text.toString() == "" ||
                locationEditText.text.toString() == "" ||
                attendantsnumber.text.toString() == "") {
                    // need to enter mandatory items
            }
            else {
                val curruser = FirebaseAuth.getInstance().currentUser
                val uid = UUID.randomUUID().toString()
                var addressString = ""
                // save to database here
                if (latLng == null) {
                    latLng = getLatLngFromAddress(locationEditText.text.toString())
                }
                if (latLng != null) {
                    address = getAddressFromLatLng(latLng!!)

                    for (i in 0..address?.maxAddressLineIndex!!) {
                        addressString += address?.getAddressLine(i)
                    }
                }
                var eventInformation = latLng?.let { it1 ->
                    DBEventsInformation(
                        nameEditText.text.toString(),
                        getStringFromTime(startTime),
                        attendantsnumber.text.toString().toLong(),
                        addressString,
                        it1,
                        informationEditText.text.toString(),
                        curruser!!.uid,
                        uid
                    )
                }

                if (eventInformation != null) {
                    writeToDatabase(eventInformation)
                }
/*
=======
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

 */
                dismiss()
            }

        }
        // If cancel, just dismiss the dialog
        else {
            dismiss()
        }
    }

    fun getStringFromTime(time: Calendar): String {
        var string = "${time.get(Calendar.DAY_OF_MONTH)}/" +
                "${time.get(Calendar.DAY_OF_MONTH)+1}" +
                "/${time.get(Calendar.YEAR)}" +
                " ${time.get(Calendar.HOUR_OF_DAY)}:" +
                "${time.get(Calendar.MINUTE)}:00"
        return string
    }

    fun writeToDatabase(eventInfo: DBEventsInformation) {
        //val uniqueid = UUID.randomUUID();
        //myref = FirebaseDatabase.getInstance().reference

        val db = FirebaseDatabase.getInstance().reference.child("events1").child(eventInfo.id)
        db.setValue(eventInfo)
        //myref.child("events").push().setValue(eventInfo)
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
        if (view == startDatePickerDialog.datePicker) {
            startTime.set(Calendar.YEAR, year)
            startTime.set(Calendar.MONTH, month)
            startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            startDateText.setText(
                "${startTime.get(Calendar.YEAR)}/${startTime.get(Calendar.MONTH) + 1}/${startTime.get(Calendar.DAY_OF_MONTH)}"
            )
        }
        else {
            endTime.set(Calendar.YEAR, year)
            endTime.set(Calendar.MONTH, month)
            endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            endDateText.setText(
                "${endTime.get(Calendar.YEAR)}/${endTime.get(Calendar.MONTH) + 1}/${endTime.get(Calendar.DAY_OF_MONTH)}"
            )
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        TODO("Not yet implemented")
    }

}