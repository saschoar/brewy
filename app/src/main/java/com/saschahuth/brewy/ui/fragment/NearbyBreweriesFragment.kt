package com.saschahuth.brewy.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.brewerydb.Api
import com.saschahuth.brewy.domain.brewerydb.DISTANCE_UNIT_MILES
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.saschahuth.brewy.domain.brewerydb.model.LocationParcel
import com.saschahuth.brewy.domain.brewerydb.model.ResultPage
import com.saschahuth.brewy.ui.activity.LocationDetailsActivity
import com.saschahuth.brewy.ui.adapter.ItemAdapter
import com.saschahuth.brewy.util.hasLocationPermission
import com.saschahuth.brewy.util.logDebug
import com.saschahuth.brewy.util.requestLocationPermission
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.fragment_nearby_breweries.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyUtils

class NearbyBreweriesFragment : Fragment() {

    private val itemAdapter: ItemAdapter by lazy { ItemAdapter(activity, peekHeaderHeight) }
    private val peekFilterBarHeight: Int by lazy { activity.resources.getDimensionPixelSize(R.dimen.peekFilterBarHeight) }
    private val peekHeaderHeight: Int by lazy { activity.resources.getDimensionPixelSize(R.dimen.peekHeaderHeight) }
    private val peekTotalHeight: Int by lazy { activity.resources.getDimensionPixelSize(R.dimen.peekTotalHeight) }

    private val PERMISSIONS_LOCATION = 0

    private var isSortingByName: Boolean = false

    var mapView: MapView? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_nearby_breweries, container, false)
    }

    var selectedMarker: Marker? = null

    var markerIcon: BitmapDescriptor? = null
    var selectedMarkerIcon: BitmapDescriptor? = null

    var recyclerViewScrolled = 0

    var canScrollVertically = true

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = object : LinearLayoutManager(recyclerView.context) {
            override fun canScrollVertically(): Boolean {
                return super.canScrollVertically() && canScrollVertically
            }
        }

        filterBar.setOnTouchListener {
            view, motionEvent ->
            if (slidingLayout.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                slidingLayout.isTouchEnabled = true
            }
            false
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerViewScrolled -= dy
                filterBar.translationY = Math.max(0, peekHeaderHeight + recyclerViewScrolled).toFloat()
                slidingLayout.panelHeight = if (recyclerViewScrolled == 0) peekTotalHeight else peekFilterBarHeight
            }
        })

        itemAdapter.header.setOnClickListener {
            slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }

        filterBar.setOnClickListener {
            slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
        }
        filterBar.isClickable = false

        headerHelper.setOnTouchListener {
            view, motionEvent ->
            mapWrapper.dispatchTouchEvent(motionEvent)
        }

        slidingLayout.setPanelSlideListener(object : SlidingUpPanelLayout.SimplePanelSlideListener() {
            override fun onPanelExpanded(panel: View?) {
                super.onPanelExpanded(panel)
                selectMarker(null)
                canScrollVertically = true
                slidingLayout.isTouchEnabled = true
                filterBar.isClickable = false
                sortSwitchWrapper.visibility = View.VISIBLE
            }

            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                super.onPanelSlide(panel, slideOffset)
                logDebug(slideOffset)

                if (slidingLayout.panelHeight == peekFilterBarHeight) {
                    //TODO find better range
                    filterBar.translationY = Math.max(0, peekHeaderHeight + recyclerViewScrolled).toFloat()
                }
                headerHelper.visibility = View.GONE
            }

            override fun onPanelCollapsed(panel: View?) {
                super.onPanelCollapsed(panel)
                slidingLayout.isTouchEnabled = true
                canScrollVertically = false
                filterBar.isClickable = true
                headerHelper.visibility = View.VISIBLE
                sortSwitchWrapper.visibility = View.GONE

                if (slidingLayout.panelHeight == peekFilterBarHeight) {
                    filterBar.translationY = 0f
                }
            }
        })

        mapView = view?.findViewById(R.id.mapView) as MapView
        mapView?.onCreate(savedInstanceState)

        mapView?.getMapAsync {
            it.uiSettings.isMyLocationButtonEnabled = false
            it.setOnMarkerClickListener {
                selectMarker(it)
                true
            }
            it.setOnMapClickListener {
                selectMarker(null)
            }
        }

        markerLocationView.setOnClickListener {
            openDetailsActivity(markerLocationView.boundLocation)
        }

        myLocation.setOnClickListener {
            mapView?.getMapAsync {
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.024925, -83.0038657), 14F))
            }
        }

        layers.setOnClickListener {
            mapView?.getMapAsync {
                it.mapType = if (it.mapType == GoogleMap.MAP_TYPE_NORMAL) GoogleMap.MAP_TYPE_HYBRID else GoogleMap.MAP_TYPE_NORMAL
            }
        }

        if (!activity.hasLocationPermission()) {
            activity.requestLocationPermission(PERMISSIONS_LOCATION)
        } else {
            mapView?.getMapAsync {
                it.isMyLocationEnabled = true
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.024925, -83.0038657), 14F))
            }
        }

        recyclerView.adapter = itemAdapter

        sortSwitch.setOnCheckedChangeListener {
            button, b ->
            CalligraphyUtils.applyFontToTextView(activity, nameLabel, getString(if (b) R.string.fontPathBold else R.string.fontPathRegular))
            CalligraphyUtils.applyFontToTextView(activity, distanceLabel, getString(if (b) R.string.fontPathRegular else R.string.fontPathBold))
            if (b) {
                itemAdapter.sortByName()
            } else {
                itemAdapter.sortByDistance()
            }
        }

        nameLabel.setOnTouchListener {
            view, motionEvent ->
            sortSwitch.dispatchTouchEvent(motionEvent)
        }
        distanceLabel.setOnTouchListener {
            view, motionEvent ->
            sortSwitch.dispatchTouchEvent(motionEvent)
        }

        val breweryDbApi = Api.create()

        breweryDbApi
                .getLocationsByGeoPoint(40.024925, -83.0038657, unit = DISTANCE_UNIT_MILES)
                .enqueue(object : Callback<ResultPage<Location>> {

                    override fun onResponse(call: Call<ResultPage<Location>>?, response: Response<ResultPage<Location>>?) {
                        var data = response?.body()?.data

                        if (data != null) {
                            itemAdapter.addAll(data.filterNot {
                                it.inPlanning ?: true || it.isClosed ?: true
                            })
                            mapView?.getMapAsync {
                                mapView ->
                                data.filterNot {
                                    it.inPlanning ?: true || it.isClosed ?: true
                                }.forEach {
                                    mapView?.addMarker(MarkerOptions()
                                            .position(LatLng(it.latitude?.toDouble() ?: 0.0, it.longitude?.toDouble() ?: 0.0))
                                            .title(it.id)
                                            .icon(markerIcon))
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResultPage<Location>>?, throwable: Throwable?) {
                        //TODO
                    }
                })
    }

    fun selectMarker(marker: Marker?) {
        selectedMarker?.setIcon(markerIcon)
        selectedMarker = marker

        val location = if (marker != null) itemAdapter.findById(marker.title) else null
        if (marker != null && location != null) {
            marker.setIcon(selectedMarkerIcon)
            markerLocationView.bind(location)
            markerLocationView.visibility = View.VISIBLE
        } else {
            markerLocationView.visibility = View.GONE
        }
    }

    fun openDetailsActivity(location: Location?) {
        val locationParcel = LocationParcel.wrap(location)
        val intent = Intent(activity, LocationDetailsActivity::class.java)
        intent.putExtra("location", locationParcel)
        startActivity(intent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        markerIcon = getBitmapDescriptor(R.drawable.ic_marker_36dp)
        selectedMarkerIcon = getBitmapDescriptor(R.drawable.ic_marker_selected_48dp)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapView?.getMapAsync {
                        it.isMyLocationEnabled = true
                    }
                }
            }
        }
    }

    //TODO better caching of result
    fun getBitmapDescriptor(id: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, id)
        val h = vectorDrawable.intrinsicHeight
        val w = vectorDrawable.intrinsicWidth
        vectorDrawable.setBounds(0, 0, w.toInt(), h.toInt())
        val bm = Bitmap.createBitmap(w.toInt(), h.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }
}