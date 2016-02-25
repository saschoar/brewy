package com.saschahuth.brewy.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.saschahuth.brewy.ui.view.LocationItemView
import com.saschahuth.brewy.util.distanceTo

/**
 * Created by sascha on 17.02.16.
 */

class LocationAdapter(context: Context) : ArrayAdapter<Location>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View = (convertView ?: LocationItemView(context))
        (view as LocationItemView).bind(getItem(position))
        return view
    }

    fun findById(id: String): Location? {
        try {
            for (i in 0..count) {
                if (getItem(i).id == id) return getItem(i)
            }
        } catch(e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
        return null
    }

    fun sortByName() {
        sort {
            location1, location2 ->
            location1.brewery?.name?.compareTo(location2.brewery?.name ?: "", true)!!
        }
    }

    fun sortByDistance() {
        sort {
            location1, location2 ->
            location1.distanceTo(40.024925, -83.0038657).compareTo(
                    location2.distanceTo(40.024925, -83.0038657)
            )
        }
    }
}