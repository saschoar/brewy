package com.saschahuth.brewy.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saschahuth.brewy.R

class BeersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val recyclerView: RecyclerView = inflater?.inflate(R.layout.fragment_beers, container, false) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)

        return recyclerView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}