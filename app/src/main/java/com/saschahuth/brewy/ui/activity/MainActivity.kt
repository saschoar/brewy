package com.saschahuth.brewy.ui.activity

import android.os.Bundle
import android.util.Log
import com.saschahuth.brewy.R
import com.saschahuth.brewy.domain.brewerydb.Api
import com.saschahuth.brewy.util.action
import com.saschahuth.brewy.util.snack
import com.saschahuth.brewy.util.toast
import kotlinx.android.synthetic.main.activity_main.*

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

        Thread({ Log.d("Test", Api.create().getBrewery("IEFRaK").execute().body().data.name) }).start()
    }
}