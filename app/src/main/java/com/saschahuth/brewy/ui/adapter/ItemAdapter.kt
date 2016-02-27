package com.saschahuth.brewy.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
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

    val VIEW_TYPE_HEADER = 0
    val VIEW_TYPE_ITEM = 1

    val header: View by lazy {
        val view = View(context)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)
        view
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val view = if (viewType == VIEW_TYPE_HEADER) header else LocationItemView(context)
        return object : RecyclerView.ViewHolder(view) {

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            val location: Location? = list[position - 1]
            if (location != null) {
                (viewHolder?.itemView as LocationItemView).bind(location)
            }
        }
    }

    fun add(toAdd: Location) {
        list.add(toAdd)
        notifyDataSetChanged()
    }

    fun addAll(toAdd: List<Location>) {
        list.addAll(toAdd)
        notifyDataSetChanged()
    }

    fun findById(id: String): Location? {
        try {
            val count = list.size
            for (i in 0..count) {
                val location = list[i]
                if (location.id == id) return location
            }
        } catch(e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
        return null
    }

    fun sortByName() {
        list.sortBy {
            it.brewery?.name?.toLowerCase()
        }
    }

    fun sortByDistance() {
        list.sortBy {
            it.distanceTo(40.024925, -83.0038657)
        }
    }

    fun setOnHeaderTouchListener(onTouchListener: View.OnTouchListener?) {
        header.setOnTouchListener(onTouchListener)
    }

}