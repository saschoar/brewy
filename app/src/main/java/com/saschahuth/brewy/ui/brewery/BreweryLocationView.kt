package com.saschahuth.brewy.ui.brewery

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.model.Location
import com.saschahuth.brewy.domain.model.LocationParcel
import com.saschahuth.brewy.ui.transformation.RoundedCornersTransformation
import com.saschahuth.brewy.util.dimenToPixels
import com.saschahuth.brewy.util.distanceTo
import com.saschahuth.brewy.util.getFormattedAddress
import com.saschahuth.brewy.util.getFormattedName
import kotlinx.android.synthetic.main.view_brewery_location.view.*

/**
 * Created by sascha on 24.02.16.
 */
class BreweryLocationView : RelativeLayout {

    var boundLocation: Location? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setBackgroundResource(R.color.defaultBackground)
        LayoutInflater.from(context).inflate(R.layout.view_brewery_location, this, true)

        setOnClickListener {
            openDetailsActivity(boundLocation)
        }
    }

    fun bind(location: Location) {
        boundLocation = location
        title.text = location.getFormattedName()
        address.text = location.getFormattedAddress(includeCountry = false)

        distance.text = "${location.distanceTo(40.024925, -83.0038657).toInt()} m away" //TODO just for testing
        val uriString = location.brewery?.images?.squareMedium
        if (uriString != null) {
            Glide.with(context).load(uriString).bitmapTransform(RoundedCornersTransformation(context, dimenToPixels(R.dimen.marginSmall), 0)).into(image)
        } else {
            Glide.clear(image);
            image.setImageResource(R.color.imagePlaceholder)
        }
    }

    fun openDetailsActivity(location: Location?) {
        val locationParcel = LocationParcel.wrap(location)
        val intent = Intent(context, BreweryActivity::class.java)
        intent.putExtra("location", locationParcel)
        context.startActivity(intent)
    }
}