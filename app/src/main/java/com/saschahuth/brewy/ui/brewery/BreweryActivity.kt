package com.saschahuth.brewy.ui.brewery

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.saschahuth.brewy.BaseActivity
import com.saschahuth.brewy.BrewyApp
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.BreweryDbService
import com.saschahuth.brewy.domain.model.Location
import com.saschahuth.brewy.domain.model.LocationParcel
import com.saschahuth.brewy.ui.beer.BeerAdapter
import com.saschahuth.brewy.util.logDebug
import kotlinx.android.synthetic.main.activity_brewery.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class BreweryActivity : BaseActivity() {

    var location: Location? = null

    @Inject lateinit var breweryDbService: BreweryDbService

    val beerAdapter: BeerAdapter by lazy { BeerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BrewyApp.appComponent.inject(this)

        setContentView(R.layout.activity_brewery)
        window.statusBarColor = 0x33000000;

        Slidr.attach(this, SlidrConfig.Builder().build())

        location = intent?.getParcelableExtra<LocationParcel>("location")?.contents
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        appBarLayout.alpha = 0F

        if (location != null) {
            beerAdapter.breweryDetailsView.bind(location)

            beersRecyclerView.layoutManager = LinearLayoutManager(beersRecyclerView.context)
            beersRecyclerView.adapter = beerAdapter

            location?.brewery?.id?.apply {
                breweryDbService
                        .getBeersForBrewery(this)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({
                            it?.data?.apply {
                                beerAdapter.addAll(this)
                            }
                        }, {
                            it.printStackTrace()
                        }, {
                            logDebug("onComplete")
                        })
            }
        }
    }
}