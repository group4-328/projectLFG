package com.example.projectlfg

import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.Button

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.projectlfg.databinding.ActivityMapsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    companion object {
        val latLngKey = "lat_lng_key"
    }

    private lateinit var authenticator: FirebaseAuth;
    private lateinit var myref : DatabaseReference;
    private lateinit var storage: FirebaseStorage;
    private lateinit var storageRef : StorageReference

    private lateinit var createEventButton: Button

    // Set location variables to use if a event address/latlng not given
    private lateinit var locationManager: LocationManager

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private var givenLocation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        storage = Firebase.storage;
        storageRef = storage.reference
        authenticator = FirebaseAuth.getInstance()
        myref = Firebase.database.reference

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

        mMap.setOnMapLongClickListener(this)

        createEventButton.setOnClickListener(View.OnClickListener {
            var createEventDialog = CreateEventDialog()
            createEventDialog.show(supportFragmentManager, "createEvent")
        })
    }

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