package com.saschahuth.brewy.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_brewery.view.*

/**
 * Created by sascha on 17.02.16.
 */

class LocationAdapter(context: Context) : ArrayAdapter<Location>(context, 0) {

    val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View = (convertView ?: layoutInflater.inflate(R.layout.item_brewery, parent, false))
        val item = getItem(position)
        view.title.text = item?.brewery?.name + if (item?.name == "Main Brewery") "" else " (" + item?.name + ")" //TODO just for testing
        view.address.text = item?.streetAddress + ", " + item?.postalCode + " " + item?.locality
        view.distance.text = distanceBetween(
                40.024925,
                -83.0038657,
                item?.latitude!!.toDouble(),
                item?.longitude!!.toDouble())
                .toInt().toString() + " m away" //TODO just for testing
        val uriString = item?.brewery?.images?.squareMedium
        if (uriString != null) {
            Picasso.with(context).load(uriString).into(view.image)
        } else {
            Picasso.with(context).cancelRequest(view.image)
            view.image.setImageResource(R.color.imagePlaceholder)
        }
        return view
    }

    fun distanceBetween(latitude1: Double, longitude1: Double, latitude2: Double, longitude2: Double): Float {
        val location1 = android.location.Location("")
        location1.latitude = latitude1
        location1.longitude = longitude1
        val location2 = android.location.Location("")
        location2.latitude = latitude2
        location2.longitude = longitude2
        return location1.distanceTo(location2)
    }

    fun sortByName() {
        sort {
            location1, location2 ->
            location1.brewery.name.compareTo(location2.brewery.name, true)
        }
    }

    fun sortByDistance() {
        sort {
            location1, location2 ->
            distanceBetween(40.024925, -83.0038657, location1.latitude.toDouble(), location1.longitude.toDouble()).compareTo(
                    distanceBetween(40.024925, -83.0038657, location2.latitude.toDouble(), location2.longitude.toDouble())
            )
        }
    }
}