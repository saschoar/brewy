package com.saschahuth.brewy.ui.beer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.model.Location
import com.saschahuth.brewy.util.getFormattedAddress
import kotlinx.android.synthetic.main.view_brewery_details.view.*

/**
 * Created by sascha on 24.02.16.
 */
class BreweryDetailsView : RelativeLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setBackgroundResource(R.color.defaultBackground)
        LayoutInflater.from(context).inflate(R.layout.view_brewery_details, this, true)
    }

    fun bind(location: Location?) {
        title.text = location?.brewery?.name
        val descriptionText = location?.brewery?.description

        if (descriptionText.isNullOrEmpty()) {
            description.visibility = GONE
        } else {
            description.visibility = VISIBLE
            description.text = descriptionText
        }
        address.text = location?.getFormattedAddress("\n")

        val uriString = location?.brewery?.images?.squareMedium
        if (uriString != null) {
            Glide.with(context).load(uriString).into(image)
        } else {
            Glide.clear(image)
            image.setImageResource(R.color.imagePlaceholder)
        }
    }
}