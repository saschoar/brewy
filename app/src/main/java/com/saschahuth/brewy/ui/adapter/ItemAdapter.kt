package com.saschahuth.brewy.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.saschahuth.brewy.ui.view.LocationItemView
import com.saschahuth.brewy.util.distanceTo
import java.util.*

/**
 * Created by sascha on 17.02.16.
 */

class ItemAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list = ArrayList<Location>()
    val context = context

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val view = LocationItemView(context)
        return object : RecyclerView.ViewHolder(view) {

        }
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        val location: Location? = list?.get(position)
        if (location != null) {
            (viewHolder?.itemView as LocationItemView).bind(location)
        }
    }

    fun add(toAdd: Location) {
        list?.add(toAdd)
        notifyDataSetChanged()
    }

    fun addAll(toAdd: List<Location>) {
        list?.addAll(toAdd)
        notifyDataSetChanged()
    }

    fun findById(id: String): Location? {
        try {
            val count = list?.size ?: 0
            for (i in 0..count) {
                val location = list?.get(i)
                if (location?.id == id) return location
            }
        } catch(e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
        return null
    }

    fun sortByName() {
        list?.sortBy {
            it.brewery?.name?.toLowerCase()
        }
    }

    fun sortByDistance() {
        list?.sortBy {
            it.distanceTo(40.024925, -83.0038657)
        }
    }

}