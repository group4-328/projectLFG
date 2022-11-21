package com.example.projectlfg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.projectlfg.event_map.MapsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    // To be used possibly with screen updates and login
    private var loginMade = true;

    // Buttons
    private lateinit var viewJoinedEventsButton: Button
    private lateinit var findNewEventButton: Button
    private lateinit var viewUserProfileButton: Button
    private lateinit var viewContactsButton: Button


    // Code for fragment implementation for gps map
    // Thinking to use activity instead
    // can easily change back later
    /*
    // Fragments to be put into an array
    private lateinit var mapsFragment: MapsFragment
    // fragment array
    private lateinit var fragments: ArrayList<Fragment>
    // tab layout and view pager setup
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    // Fragment state adapter
    private lateinit var myFragmentStateAdapter: MyFragmentStateAdapter
    // Items for tab creation
    private var tabLabels = arrayOf("Event Map")
    // Tab config strategy and layout mediator
    private lateinit var tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy
    private lateinit var tabLayoutMediator: TabLayoutMediator
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utilities.checkPermissions(this)
        initButtons()

        findNewEventButton.setOnClickListener(View.OnClickListener {
            // temp
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        })

        // Code I used to view fragment
        /*
        // temp way to display map fragment
        mapsFragment = MapsFragment()
        fragments = ArrayList()
        fragments.add(mapsFragment)
        tabLayout = findViewById(R.id.tab_layout)
        viewPager2 = findViewById(R.id.view_pager_2)
        myFragmentStateAdapter = MyFragmentStateAdapter(this, fragments)
        viewPager2.adapter = myFragmentStateAdapter
        tabConfigurationStrategy =
            TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.text = tabLabels[position]
            }
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2, tabConfigurationStrategy)
        tabLayoutMediator.attach()
        */
    }

    override fun onDestroy() {
        super.onDestroy()

        // Used with fragment test of 
        //tabLayoutMediator.detach()
    }

    fun initButtons() {
        viewJoinedEventsButton = findViewById(R.id.view_joined_events_button)
        findNewEventButton = findViewById(R.id.find_new_events_button)
        viewUserProfileButton = findViewById(R.id.view_profile_settings_button)
        viewContactsButton = findViewById(R.id.view_contacts_button)
    }
}