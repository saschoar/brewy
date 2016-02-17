package com.saschahuth.brewy.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by sascha on 17.02.16.
 */

class ItemAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val layoutInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    enum class ViewType {
        BEER, BREWERY, LOCATION, CATEGORY
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        throw UnsupportedOperationException()
    }

    override fun getItemCount(): Int {
        throw UnsupportedOperationException()
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder?, viewType: Int) {
        throw UnsupportedOperationException()
    }

}

class BreweryViewHolder