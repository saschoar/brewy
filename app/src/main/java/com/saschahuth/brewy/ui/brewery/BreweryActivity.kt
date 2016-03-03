package com.saschahuth.brewy.ui.brewery

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.saschahuth.brewy.BaseActivity
import com.saschahuth.brewy.BrewyApp
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.BreweryDbService
import com.saschahuth.brewy.domain.model.Location
import com.saschahuth.brewy.domain.model.LocationParcel
import com.saschahuth.brewy.ui.beer.BeerAdapter
import com.saschahuth.brewy.util.getFormattedAddress
import com.saschahuth.brewy.util.logDebug
import kotlinx.android.synthetic.main.activity_brewery.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import uk.co.chrisjenx.calligraphy.TypefaceUtils
import javax.inject.Inject

class BreweryActivity : BaseActivity() {

    var location: Location? = null
    var tempToggle = false;

    @Inject lateinit var breweryDbService: BreweryDbService
    val beerAdapter: BeerAdapter by lazy { BeerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BrewyApp.appComponent.inject(this)

        setContentView(R.layout.activity_brewery)
        //collapsingToolbar.systemUiVisibility = collapsingToolbar.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        window.statusBarColor = 0x33000000;

        Slidr.attach(this, SlidrConfig.Builder().sensitivity(0.5f).build())

        val headlineTypeface = TypefaceUtils.load(assets, getString(R.string.fontPathHeadline))

        location = intent?.getParcelableExtra<LocationParcel>("location")?.contents
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (location != null) {
            collapsingToolbar.title = location?.brewery?.name
            collapsingToolbar.setCollapsedTitleTypeface(headlineTypeface)
            collapsingToolbar.setExpandedTitleTypeface(headlineTypeface)

            //TODO remove
            collapsingToolbar.setOnClickListener {
                if (tempToggle) {
                    beersRecyclerView.visibility = View.VISIBLE
                    infoScrollView.visibility = View.GONE
                } else {
                    beersRecyclerView.visibility = View.GONE
                    infoScrollView.visibility = View.VISIBLE
                }
                tempToggle = tempToggle.not()
            }

            description.text = location?.brewery?.description
            address.text = location?.getFormattedAddress("\n")

            /* with(ColorMatrix()) {
                 setSaturation(0F)
                 backdrop.colorFilter = ColorMatrixColorFilter(this)
             }

             val address = Uri.encode(location?.streetAddress + ", " + location?.postalCode + " " + location?.locality)

             val backdropWidth: Int = resources.displayMetrics.widthPixels
             val backdropHeight: Int = dimenToPixels(R.dimen.detailHeaderHeight)

             //val url = "https://maps.googleapis.com/maps/api/streetview?size=${backdropWidth}x${backdropHeight}&location=$address&fov=80&key=${BuildConfig.GOOGLE_MAPS_API_KEY}"
             Glide.with(this).load(url).bitmapTransform(BlurTransformation(this, 8)).into(backdrop)*/

            val uriString = location?.brewery?.images?.squareMedium
            if (uriString != null) {
                Glide.with(this).load(uriString).into(image)
            } else {
                Glide.clear(image)
                image.setImageResource(R.color.imagePlaceholder)
            }

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