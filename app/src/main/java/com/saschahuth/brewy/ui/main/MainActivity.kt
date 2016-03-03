package com.saschahuth.brewy.ui.main

import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.saschahuth.brewy.BaseActivity
import com.saschahuth.brewy.BrewyApp
import com.saschahuth.brewy.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MainContentFragmentAdapter(supportFragmentManager)
        adapter.addFragment(NearbyBreweriesFragment(), "Nearby")
        adapter.addFragment(BeersFragment(), "Beers")
        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)

        BrewyApp.appComponent.inject(this)
    }

    class MainContentFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val fragments = ArrayList<Fragment>()
        private val fragmentTitles = ArrayList<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            fragmentTitles.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitles[position]
        }
    }
}