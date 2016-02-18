package com.saschahuth.brewy.ui.activity

import android.os.Bundle
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.constants.Style
import com.mapbox.mapboxsdk.geometry.LatLng
import com.saschahuth.brewy.BuildConfig
import com.saschahuth.brewy.domain.brewerydb.Api
import com.saschahuth.brewy.domain.brewerydb.DISTANCE_UNIT_MILES
import com.saschahuth.brewy.domain.brewerydb.model.Brewery
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.saschahuth.brewy.domain.brewerydb.model.Result
import com.saschahuth.brewy.domain.brewerydb.model.ResultPage
import com.saschahuth.brewy.ui.adapter.LocationAdapter
import com.saschahuth.brewy.util.logDebug
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    private val locationAdapter: LocationAdapter by lazy { LocationAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.saschahuth.brewy.R.layout.activity_main)
        //setSupportActionBar(toolbar)

        mapView.setAccessToken(BuildConfig.MAPBOX_API_KEY)
        mapView.setStyleUrl(Style.MAPBOX_STREETS)
        mapView.isMyLocationEnabled = true
        mapView.setOnMyLocationChangeListener { location -> mapView.centerCoordinate = LatLng(location.latitude, location.longitude) }
        mapView.zoom = 11.toDouble()
        mapView.onCreate(savedInstanceState)

        listView.adapter = locationAdapter

        val breweryDbApi = Api.create()

        breweryDbApi
                .getBrewery("IEFRaK")
                .enqueue(object : Callback<Result<Brewery>> {
                    override fun onResponse(call: Call<Result<Brewery>>?, response: Response<Result<Brewery>>?) {
                        logDebug(response?.body()?.data?.website)
                    }

                    override fun onFailure(call: Call<Result<Brewery>>?, throwable: Throwable?) {
                        //TODO
                    }
                })

        breweryDbApi
                .getLocations(postalCode = 43202)
                .enqueue(object : Callback<ResultPage<Location>> {

                    override fun onResponse(call: Call<ResultPage<Location>>?, response: Response<ResultPage<Location>>?) {
                        val flatMapName = response?.body()?.data?.flatMap { location -> listOf(location.name) }
                        logDebug(flatMapName.toString())
                    }

                    override fun onFailure(call: Call<ResultPage<Location>>?, throwable: Throwable?) {
                        //TODO
                    }
                })

        breweryDbApi
                .getLocationsByGeoPoint(40.024925, -83.0038657, unit = DISTANCE_UNIT_MILES)
                .enqueue(object : Callback<ResultPage<Location>> {

                    override fun onResponse(call: Call<ResultPage<Location>>?, response: Response<ResultPage<Location>>?) {
                        val names = response?.body()?.data?.map { location -> location.name }
                        locationAdapter.addAll(response?.body()?.data)
                        mapView.addMarkers(
                                response?.body()?.data!!.map {
                                    location ->
                                    MarkerOptions()
                                            .position(LatLng(location.latitude.toDouble(), location.longitude.toDouble()))
                                            .title(location.brewery.name)
                                            .snippet(location.streetAddress)
                                })
                        logDebug(names)
                    }

                    override fun onFailure(call: Call<ResultPage<Location>>?, throwable: Throwable?) {
                        //TODO
                    }
                })
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState!!)
    }
}