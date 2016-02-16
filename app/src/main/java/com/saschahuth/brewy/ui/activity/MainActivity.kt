package com.saschahuth.brewy.ui.activity

import android.os.Bundle
import android.util.Log
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.brewerydb.Api
import com.saschahuth.brewy.domain.brewerydb.model.Brewery
import com.saschahuth.brewy.domain.brewerydb.model.Location
import com.saschahuth.brewy.domain.brewerydb.model.Result
import com.saschahuth.brewy.domain.brewerydb.model.ResultPage
import com.saschahuth.brewy.util.action
import com.saschahuth.brewy.util.snack
import com.saschahuth.brewy.util.toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        textView.text = "hey"
        textView.setOnClickListener({
            textView.snack(textView.width.toString()) {
                action("Action") { toast("Action clicked") }
            }
        })

        val breweryDbApi = Api.create()

        breweryDbApi
                .getBrewery("IEFRaK")
                .enqueue(object : Callback<Result<Brewery>> {
                    override fun onResponse(call: Call<Result<Brewery>>?, response: Response<Result<Brewery>>?) {
                        Log.d("id IEFRaK", response?.body()?.data?.website)
                    }

                    override fun onFailure(call: Call<Result<Brewery>>?, throwable: Throwable?) {
                        Log.d("TestError", throwable.toString())
                    }
                })

        breweryDbApi
                .getLocations(postalCode = 43202)
                .enqueue(object : Callback<ResultPage<Location>> {

                    override fun onResponse(call: Call<ResultPage<Location>>?, response: Response<ResultPage<Location>>?) {
                        val flatMapName = response?.body()?.data?.flatMap { location -> listOf(location.name) }
                        Log.d("Location 43202", flatMapName.toString())
                    }

                    override fun onFailure(call: Call<ResultPage<Location>>?, throwable: Throwable?) {
                        throw UnsupportedOperationException()
                    }
                })

        breweryDbApi
                .getBreweriesByGeoPoint(40.024925, -83.0038657)
                .enqueue(object : Callback<ResultPage<Brewery>> {

                    override fun onResponse(call: Call<ResultPage<Brewery>>?, response: Response<ResultPage<Brewery>>?) {
                        val names = response?.body()?.data?.map { location -> location.name }
                        Log.d("Closest", names.toString())
                    }

                    override fun onFailure(call: Call<ResultPage<Brewery>>?, throwable: Throwable?) {
                        throw UnsupportedOperationException()
                    }
                })
    }
}