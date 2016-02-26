package com.saschahuth.brewy.ui.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.saschahuth.brewy.domain.brewerydb.model.LocationParcel
import com.saschahuth.brewy.ui.activity.LocationDetailsActivity
import com.saschahuth.brewy.util.distanceTo
import com.saschahuth.brewy.util.getFormattedAddress
import com.saschahuth.brewy.util.getFormattedName
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_brewery.view.*

/**
 * Created by sascha on 24.02.16.
 */
class LocationItemView : FrameLayout {

    var boundLocation: Location? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.item_brewery, this, true)
    }

    fun bind(location: Location) {
        boundLocation = location
        title.text = location.getFormattedName()
        address.text = location.getFormattedAddress(includeCountry = false)

        distance.text = "${location.distanceTo(40.024925, -83.0038657).toInt()} m away" //TODO just for testing
        val uriString = location.brewery?.images?.squareMedium
        if (uriString != null) {
            Picasso.with(context).load(uriString).into(image)
        } else {
            Picasso.with(context).cancelRequest(image)
            image.setImageResource(R.color.imagePlaceholder)
        }

        setOnClickListener {
            openDetailsActivity(boundLocation)
        }
    }

    fun openDetailsActivity(location: Location?) {
        val locationParcel = LocationParcel.wrap(location)
        val intent = Intent(context, LocationDetailsActivity::class.java)
        intent.putExtra("location", locationParcel)
        context.startActivity(intent)
    }
}