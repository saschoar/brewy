package com.saschahuth.brewy.ui.activity

import android.os.Bundle
import com.saschahuth.brewy.R
import com.saschahuth.brewy.ui.adapter.LocationAdapter

class BreweryDetailsActivity : BaseActivity() {

    private val locationAdapter: LocationAdapter by lazy { LocationAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brewery_details)
    }
}