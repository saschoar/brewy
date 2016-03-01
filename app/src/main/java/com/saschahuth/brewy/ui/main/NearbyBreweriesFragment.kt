package com.saschahuth.brewy.ui.main

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.saschahuth.brewy.domain.BreweryDbServiceProvider
import com.saschahuth.brewy.ui.adapter.ItemAdapter
import com.saschahuth.brewy.util.hasLocationPermission
import com.saschahuth.brewy.util.requestLocationPermission
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.fragment_nearby_breweries.*
import uk.co.chrisjenx.calligraphy.CalligraphyUtils
import javax.inject.Inject

class NearbyBreweriesFragment : Fragment() {

    @Inject lateinit var breweryDbServiceProvider: BreweryDbServiceProvider

    private val itemAdapter: ItemAdapter by lazy { ItemAdapter(activity, peekHeaderHeight) }
    private val peekFilterBarHeight: Int by lazy { activity.resources.getDimensionPixelSize(R.dimen.peekFilterBarHeight) }
    private val peekHeaderHeight: Int by lazy { activity.resources.getDimensionPixelSize(R.dimen.peekHeaderHeight) }
    private val peekTotalHeight: Int by lazy { activity.resources.getDimensionPixelSize(R.dimen.peekTotalHeight) }

    private val PERMISSIONS_LOCATION = 0

    var mapView: MapView? = null

    val MAP_VIEW_SAVED_STATE = "mapViewSaveState"

    var selectedMarker: Marker? = null

    var markerIcon: BitmapDescriptor? = null
    var selectedMarkerIcon: BitmapDescriptor? = null

    var recyclerViewScrolled = 0

    var canScrollVertically = true

    companion object {
        @JvmStatic fun newInstance() = NearbyBreweriesFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        MainComponent.Initializer.init(activity as MainActivity).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_nearby_breweries, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view?.findViewById(R.id.mapView) as MapView

        // see https://code.google.com/p/gmaps-api-issues/issues/detail?id=6237#c9
        val mapViewSavedState = savedInstanceState?.getBundle(MAP_VIEW_SAVED_STATE)
        mapView?.onCreate(mapViewSavedState)

        setupMap()
        setupFilterBar()
        setupSlidingUi()

        if (!activity.hasLocationPermission()) {
            activity.requestLocationPermission(PERMISSIONS_LOCATION)
        } else {
            mapView?.getMapAsync {
                it.isMyLocationEnabled = true
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.024925, -83.0038657), 14F))
            }
        }

        recyclerView.adapter = itemAdapter

        breweryDbServiceProvider
                .getLocationsByGeoPoint()
                .subscribe {
                    var data = it?.data

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
    }

    fun selectMarker(marker: Marker?) {
        selectedMarker?.setIcon(markerIcon)
        selectedMarker?.hideInfoWindow()

        selectedMarker = marker

        val location = if (marker != null) itemAdapter.findById(marker.title) else null
        if (marker != null && location != null) {
            marker.setIcon(selectedMarkerIcon)
            marker.showInfoWindow()
            markerLocationView.bind(location)
            markerLocationView.visibility = View.VISIBLE
            markerLocationView.post { mapToolbar.translationY = markerLocationView.measuredHeight.toFloat() }
        } else {
            markerLocationView.visibility = View.GONE
            mapToolbar.translationY = 0f
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)
        selectedMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_selected)
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
        val mapViewSavedState = Bundle(outState)
        mapView?.onSaveInstanceState(mapViewSavedState)
        outState?.putBundle(MAP_VIEW_SAVED_STATE, mapViewSavedState)
        super.onSaveInstanceState(outState)
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

    fun setupMap() {
        mapView?.getMapAsync {
            it.uiSettings.isMyLocationButtonEnabled = false
            it.setOnMarkerClickListener {
                selectMarker(it)
                true
            }
            it.setOnMapClickListener {
                selectMarker(null)
            }
            it.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                override fun getInfoContents(p0: Marker?): View? {
                    return null
                }

                override fun getInfoWindow(p0: Marker?): View? {
                    return LayoutInflater.from(activity).inflate(R.layout.fake_info_window, null)
                }

            })
        }

        layers.setOnClickListener {
            mapView?.getMapAsync {
                it.mapType = if (it.mapType == GoogleMap.MAP_TYPE_NORMAL) GoogleMap.MAP_TYPE_HYBRID else GoogleMap.MAP_TYPE_NORMAL
            }
        }

        myLocation.setOnClickListener {
            mapView?.getMapAsync {
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(40.024925, -83.0038657), 14F))
            }
        }

    }

    fun setupFilterBar() {
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
    }

    fun setupSlidingUi() {

        recyclerView.layoutManager = object : LinearLayoutManager(recyclerView.context) {
            override fun canScrollVertically(): Boolean {
                return super.canScrollVertically() && canScrollVertically
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerViewScrolled -= dy
                filterBar.translationY = Math.max(0, peekHeaderHeight + recyclerViewScrolled).toFloat()
                slidingLayout.panelHeight = Math.max(peekFilterBarHeight, peekTotalHeight + recyclerViewScrolled)
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
                filterBar.isClickable = false
                sortSwitchWrapper.visibility = View.VISIBLE
                headerHelper.visibility = View.GONE
            }

            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                super.onPanelSlide(panel, slideOffset)
                if (!filterBar.isClickable) {
                    sortSwitchWrapper.visibility = View.GONE
                }
            }

            override fun onPanelCollapsed(panel: View?) {
                super.onPanelCollapsed(panel)
                canScrollVertically = false
                filterBar.isClickable = true
                headerHelper.visibility = View.VISIBLE
                sortSwitchWrapper.visibility = View.GONE
            }
        })
    }
}