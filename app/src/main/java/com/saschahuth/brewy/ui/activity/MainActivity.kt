package com.saschahuth.brewy.ui.activity

import android.os.Bundle
import android.util.Log
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.brewerydb.Api
import com.saschahuth.brewy.domain.brewerydb.model.Brewery
import com.saschahuth.brewy.domain.brewerydb.model.Result
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

        Api.create().getBrewery("IEFRaK").enqueue(object : Callback<Result<Brewery>> {
            override fun onResponse(p0: Call<Result<Brewery>>?, p1: Response<Result<Brewery>>?) {
                Log.d("Test", p1?.body()?.data?.website)
            }

            override fun onFailure(p0: Call<Result<Brewery>>?, p1: Throwable?) {
                Log.d("TestError", p1.toString())
            }
        })
    }
}