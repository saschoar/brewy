package com.saschahuth.brewy.ui.brewery

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.saschahuth.brewy.BaseActivity
import com.saschahuth.brewy.BuildConfig
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.model.Location
import com.saschahuth.brewy.domain.model.LocationParcel
import com.saschahuth.brewy.ui.transformation.BlurTransformation
import com.saschahuth.brewy.ui.transformation.RoundedCornersTransformation
import com.saschahuth.brewy.util.dimenToPixels
import com.saschahuth.brewy.util.getFormattedAddress
import kotlinx.android.synthetic.main.activity_brewery.*
import uk.co.chrisjenx.calligraphy.TypefaceUtils

class BreweryActivity : BaseActivity() {

    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_brewery)

        Slidr.attach(this, SlidrConfig.Builder().sensitivity(0.5f).build())

        val headlineTypeface = TypefaceUtils.load(assets, getString(R.string.fontPathHeadline))

        location = intent?.getParcelableExtra<LocationParcel>("location")?.contents
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (location != null) {
            collapsingToolbar.title = location?.brewery?.name
            collapsingToolbar.setCollapsedTitleTypeface(headlineTypeface)
            collapsingToolbar.setExpandedTitleTypeface(headlineTypeface)

            description.text = location?.brewery?.description
            address.text = location?.getFormattedAddress("\n")

            with(ColorMatrix()) {
                setSaturation(0F)
                backdrop.colorFilter = ColorMatrixColorFilter(this)
            }

            val address = Uri.encode(location?.streetAddress + ", " + location?.postalCode + " " + location?.locality)

            val backdropWidth: Int = resources.displayMetrics.widthPixels
            val backdropHeight: Int = dimenToPixels(R.dimen.detailHeaderHeight)

            val url = "https://maps.googleapis.com/maps/api/streetview?size=${backdropWidth}x${backdropHeight}&location=$address&fov=80&key=${BuildConfig.GOOGLE_MAPS_API_KEY}"
            Glide.with(this).load(url).bitmapTransform(BlurTransformation(this, 8)).into(backdrop)

            val uriString = location?.brewery?.images?.squareMedium
            if (uriString != null) {
                Glide.with(this).load(uriString).bitmapTransform(RoundedCornersTransformation(this, dimenToPixels(R.dimen.margin), 0)).into(image)
            } else {
                Glide.clear(image)
                image.setImageResource(R.color.imagePlaceholder)
            }
        }
    }
}