package com.saschahuth.brewy.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.brewerydb.Api
import com.saschahuth.brewy.domain.brewerydb.DISTANCE_UNIT_MILES
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.saschahuth.brewy.domain.brewerydb.model.ResultPage
import com.saschahuth.brewy.ui.adapter.LocationAdapter
import com.saschahuth.brewy.util.logDebug
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    private val locationAdapter: LocationAdapter by lazy { LocationAdapter(this) }

    private val PERMISSIONS_LOCATION = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync({ callback -> callback.setOnMarkerClickListener({ marker -> scrollListToMarker(marker) }) })

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_LOCATION)
        } else {
            mapView.getMapAsync({ callback -> callback.isMyLocationEnabled = true })
        }

        listView.adapter = locationAdapter

        val breweryDbApi = Api.create()

        breweryDbApi
                .getLocationsByGeoPoint(40.024925, -83.0038657, unit = DISTANCE_UNIT_MILES)
                .enqueue(object : Callback<ResultPage<Location>> {

                    override fun onResponse(call: Call<ResultPage<Location>>?, response: Response<ResultPage<Location>>?) {
                        val names = response?.body()?.data?.map { location -> location.name }
                        locationAdapter.addAll(response?.body()?.data?.filterNot { location -> location.inPlanning || location.isClosed })
                        mapView.getMapAsync({ callback ->
                            response?.body()?.data!!.filterNot { location -> location.inPlanning || location.isClosed }.map {
                                location ->
                                callback.addMarker(MarkerOptions()
                                        .position(LatLng(location.latitude.toDouble(), location.longitude.toDouble()))
                                        .title(location.id))
                            }
                        })
                        logDebug(names)
                    }

                    override fun onFailure(call: Call<ResultPage<Location>>?, throwable: Throwable?) {
                        //TODO
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState!!)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapView.getMapAsync({ callback ->
                        callback.isMyLocationEnabled = true
                    })
                }
            }
        }
    }

    fun scrollListToMarker(marker: Marker): Boolean {
        val position: Int = locationAdapter.getPosition(marker.title)
        if (position != -1) {
            listView.smoothScrollToPositionFromTop(position, 0, 0)
            return true
        }
        return false
    }
}