package com.saschahuth.brewy

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        textView.text = "hey"
        textView.setOnClickListener({ toast(textView.width.toString()) })
        Model.User("","").email = ""
    }
}