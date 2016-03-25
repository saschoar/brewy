package com.saschahuth.brewy.ui.beer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.saschahuth.brewy.domain.model.Beer
import java.util.*

/**
 * Created by Sascha Huth on 2016-03-03.
 */

class BeerAdapter(context: Context, headerHeight: Int = 0) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list = ArrayList<Beer>()
    val context = context

    val VIEW_TYPE_HEADER = 0
    val VIEW_TYPE_ITEM = 1

    val breweryDetailsView: BreweryDetailsView by lazy { BreweryDetailsView(context) }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val view = if (viewType == VIEW_TYPE_HEADER) breweryDetailsView else BeerView(context)
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
            val beer: Beer? = list[position - 1]
            if (beer != null) {
                (viewHolder?.itemView as BeerView).bind(beer)
            }
        }
    }

    fun addAll(toAdd: List<Beer>) {
        list.addAll(toAdd)
        notifyDataSetChanged()
    }

    fun sortByName() {
        list.sortBy {
            it.name?.toLowerCase()
        }
        notifyDataSetChanged()
    }

}