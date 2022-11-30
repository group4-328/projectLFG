package com.example.projectlfg

import DBEventsInformation
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.projectlfg.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.FirebaseDatabase

interface SetMarkers{
    fun SetMarkersOnMap(mList:List<DBEventsInformation>);
}

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    companion object {
        val latLngKey = "lat_lng_key"
        val NAME = "NAME"
        val STARTINGDATE = "STARTDINGDATE"
        val LOCATION = "LOCATION"
    }

    private lateinit var createEventButton: Button

    // Set location variables to use if a event address/latlng not given
    private lateinit var locationManager: LocationManager

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var givenLocation = false
    private var MarkerHashMap = HashMap<String,DBEventsInformation>();

    fun getAllMarkers(setMarkers: SetMarkers){
        val db = FirebaseDatabase.getInstance().reference.child("events1")
        var eventsarr = ArrayList<DBEventsInformation>();
        db.get().addOnSuccessListener {
            if(it.value != null){
                val data = it.value as HashMap<String,*>
                for((key,value) in data){
                    val childvalues= data.get(key) as HashMap<String,*>
                    val name:String = childvalues.get("name") as String
                    val startingdate = childvalues.get("startingdate") as String
                    val location = childvalues.get("location") as String
                    val attendants = childvalues.get("attendess")  as Long;
                    val creator = childvalues.get("creator") as String
                    val info = childvalues.get("information") as String
                    val latlng = childvalues.get("latLng") as HashMap<String,Float>;
                    val longtitude = latlng.get("longitude") as Double;
                    val latitude = latlng.get("latitude") as Double;
                    val tmplatlng = LatLng(latitude,longtitude);
                    eventsarr.add(DBEventsInformation(
                        name =name, startingdate = startingdate, attendess = attendants
                        ,
                        location = location, latLng = tmplatlng, information =  info,
                        creator =creator,id=key))
                }
                setMarkers.SetMarkersOnMap(eventsarr)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        createEventButton = findViewById(R.id.create_event_map_button)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (!givenLocation) {
            initLocationManager()
        }

        getAllMarkers(object:SetMarkers{
            override fun SetMarkersOnMap(mList:List<DBEventsInformation>) {
                mList.forEach {
                    val tmpmarker = MarkerOptions().position(it.latLng!!).title(it.name).snippet(it.name)
                    MarkerHashMap.put(it.name!!,it);
                    mMap.addMarker(tmpmarker)
                }
            }

        })
        mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener{
            override fun onMarkerClick(p0: Marker): Boolean {
                val name = p0.title
                val info = MarkerHashMap.get(name);
                val intent = Intent(applicationContext,EventInfoActivity::class.java);
//                intent.putExtra(INFO,info);
                intent.putExtra(NAME,info!!.name);
                intent.putExtra(LOCATION,info.location);
                intent.putExtra(STARTINGDATE,info.startingdate);
                intent.putExtra("Attendants",info.attendess);
                intent.putExtra("info",info.information);
                intent.putExtra("key",info.id)
                startActivity(intent);
                return true;
            }

        })

        mMap.setOnMapLongClickListener(this)

        createEventButton.setOnClickListener(View.OnClickListener {
            var createEventDialog = CreateEventDialog()
            createEventDialog.show(supportFragmentManager, "createEvent")
        })
    }


    @SuppressLint("MissingPermission")
    fun initLocationManager() {
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            val provider = locationManager.getBestProvider(criteria,true)
            if (provider != null) {
                val location = locationManager.getLastKnownLocation(provider)
                if (location != null) {
                    onLocationChanged(location)
                }
            }
        }catch (e: Exception) {
            println("Debug: exception initLocationManager")
        }
    }

    //override
    fun onLocationChanged(location: Location)  {
        val lat = location.latitude
        val lng = location.longitude
        val latlng = LatLng(lat, lng)

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 13f)
        mMap.animateCamera(cameraUpdate)
    }

    // call create new event dialog
    override fun onMapLongClick(latLng: LatLng) {
        println("Debug: lat=${latLng.latitude}, lng=${latLng.longitude}")
        var bundle = Bundle()
        bundle.putParcelable(latLngKey, latLng)
        var createEventDialog = CreateEventDialog()
        createEventDialog.arguments = bundle
        createEventDialog.show(supportFragmentManager, "createEvent")
    }



}