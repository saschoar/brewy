package com.saschahuth.brewy.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.brewerydb.Api
import com.saschahuth.brewy.domain.brewerydb.DISTANCE_UNIT_MILES
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.saschahuth.brewy.domain.brewerydb.model.ResultPage
import com.saschahuth.brewy.ui.adapter.ItemAdapter
import com.saschahuth.brewy.util.logDebug
import kotlinx.android.synthetic.main.fragment_beers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val recyclerView: RecyclerView = inflater?.inflate(R.layout.fragment_beers, container, false) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)

        return recyclerView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breweryDbApi = Api.create()

        breweryDbApi
                .getLocationsByGeoPoint(40.024925, -83.0038657, unit = DISTANCE_UNIT_MILES)
                .enqueue(object : Callback<ResultPage<Location>> {

                    override fun onResponse(call: Call<ResultPage<Location>>?, response: Response<ResultPage<Location>>?) {
                        val names = response?.body()?.data?.map { location -> location.name }
                        val filteredItems = response?.body()?.data?.filterNot { location -> location.inPlanning!! || location.isClosed!! }
                        val itemAdapter = ItemAdapter(activity)
                        itemAdapter.addAll(filteredItems!!)
                        recyclerView.adapter = itemAdapter
                        logDebug(names)
                    }

                    override fun onFailure(call: Call<ResultPage<Location>>?, throwable: Throwable?) {
                        //TODO
                    }
                })
    }
}