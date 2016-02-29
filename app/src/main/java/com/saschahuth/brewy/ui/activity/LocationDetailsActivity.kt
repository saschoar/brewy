package com.saschahuth.brewy.ui.activity

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import com.saschahuth.brewy.BuildConfig
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.saschahuth.brewy.domain.brewerydb.model.LocationParcel
import com.saschahuth.brewy.util.RoundedCornersTransformation
import com.saschahuth.brewy.util.dimenToPixels
import com.saschahuth.brewy.util.getFormattedAddress
import kotlinx.android.synthetic.main.activity_location_details.*
import uk.co.chrisjenx.calligraphy.TypefaceUtils

class LocationDetailsActivity : BaseActivity() {

    var location: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_location_details)

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

            val matrix = ColorMatrix();
            matrix.setSaturation(0F);

            val filter = ColorMatrixColorFilter(matrix);
            backdrop.colorFilter = filter;

            val address = Uri.encode(location?.streetAddress + ", " + location?.postalCode + " " + location?.locality)
            val url = "https://maps.googleapis.com/maps/api/streetview?size=400x400&location=$address&fov=80&key=${BuildConfig.GOOGLE_MAPS_API_KEY}"
            Glide.with(this).load(url).into(backdrop)

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