package com.saschahuth.brewy.ui.beer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.model.Beer
import kotlinx.android.synthetic.main.view_beer.view.*

/**
 * Created by sascha on 24.02.16.
 */
class BeerView : RelativeLayout {

    var boundBeer: Beer? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setBackgroundResource(R.color.defaultBackground)
        LayoutInflater.from(context).inflate(R.layout.view_beer, this, true)

        setOnClickListener {
            openBeerActivity()
        }
    }

    fun bind(beer: Beer) {
        boundBeer = beer
        title.text = beer.name
        beerStyle.text = beer.style?.name

        val uriString = beer.labels?.medium
        if (uriString != null) {
            Glide.with(context).load(uriString).into(image)
        } else {
            Glide.clear(image);
            image.setImageResource(R.color.imagePlaceholder)
        }
    }

    fun openBeerActivity() {
        /*if (boundBeer != null) {
            val beerParcel = BeerParcel.wrap(boundBeer)
            val intent = Intent(context, BreweryActivity::class.java)
            intent.putExtra("beer", beerParcel)
            context.startActivity(intent)
        }*/
    }
}