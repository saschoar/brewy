package com.saschahuth.brewy.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.squareup.picasso.Picasso

/**
 * Created by sascha on 17.02.16.
 */

class ItemAdapter(list: List<Location>?, context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    val list = list

    val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): ItemAdapter.ViewHolder? {
        val view = layoutInflater.inflate(R.layout.item_brewery, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list.size
    }

    override fun onBindViewHolder(viewHolder: ItemAdapter.ViewHolder?, position: Int) {
        val location: Location? = list?.get(position)

        if (location != null) {
            viewHolder?.title?.text = location.brewery.name + if (location.name == "Main Brewery") "" else " (" + location.name + ")" //TODO just for testing
            viewHolder?.address?.text = location.streetAddress + ", " + location.postalCode + " " + location.locality
            viewHolder?.distance?.text = distanceBetween(
                    40.024925,
                    -83.0038657,
                    location.latitude.toDouble(),
                    location.longitude.toDouble())
                    .toInt().toString() + " m away" //TODO just for testing
            val uriString: String? = location.brewery?.images?.squareMedium
            if (uriString != null) {
                Picasso.with(viewHolder?.image?.context).load(uriString).into(viewHolder?.image)
            } else {
                Picasso.with(viewHolder?.image?.context).cancelRequest(viewHolder?.image)
                viewHolder?.image?.setBackgroundColor(R.color.textLight)
            }
            viewHolder?.title?.text = location.name
        }
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

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView
        val title: TextView
        val address: TextView
        val distance: TextView

        init {
            image = view.findViewById(R.id.image) as ImageView
            title = view.findViewById(R.id.title) as TextView
            address = view.findViewById(R.id.address) as TextView
            distance = view.findViewById(R.id.distance) as TextView
        }
    }

}