package com.saschahuth.brewy.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.saschahuth.brewy.R
import com.saschahuth.brewy.ui.fragment.BeersFragment
import com.saschahuth.brewy.ui.fragment.NearbyBreweriesFragment
import com.saschahuth.brewy.util.applyKerning
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MainContentFragmentAdapter(supportFragmentManager)
        adapter.addFragment(NearbyBreweriesFragment(), "Nearby")
        adapter.addFragment(BeersFragment(), "Beers")
        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)
    }

    inner class MainContentFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
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
          //  val title: String = fragmentTitles[position]
          //  val typefaceSpan: CalligraphyTypefaceSpan = CalligraphyTypefaceSpan(TypefaceUtils.load(this@MainActivity.assets, getString(R.string.fontPathHeadline)))
           // val s: SpannableStringBuilder = SpannableStringBuilder()
           // s.append(title)
           // s.setSpan(typefaceSpan, 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            return fragmentTitles[position].applyKerning(10f)
        }
    }
}