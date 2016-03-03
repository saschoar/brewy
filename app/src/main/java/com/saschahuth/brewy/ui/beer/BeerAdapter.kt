package com.saschahuth.brewy.ui.beer

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
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

    val header: View by lazy {
        View(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, headerHeight)
            isFocusable = false
            isFocusableInTouchMode = false
            isClickable = false
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        val view = if (viewType == VIEW_TYPE_HEADER) header else BeerView(context)
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

    fun findById(id: String): Beer? {
        try {
            val count = list.size
            for (i in 0..count) {
                val beer = list[i]
                if (beer.id == id) return beer
            }
        } catch(e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
        return null
    }

    fun sortByName() {
        list.sortBy {
            it.name?.toLowerCase()
        }
        notifyDataSetChanged()
    }

}