package com.saschahuth.brewy.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.saschahuth.brewy.domain.model.Location

/**
 * Created by sascha on 13.02.16.
 */

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

fun Any.logDebug(any: Any?) {
    Log.d(this.javaClass.simpleName, any?.toString())
}

fun Location.distanceTo(latitude: Double = 0.0, longitude: Double = 0.0): Float {
    val location1 = android.location.Location("")
    location1.latitude = this.latitude ?: 0.0
    location1.longitude = this.longitude ?: 0.0
    val location2 = android.location.Location("")
    location2.latitude = latitude
    location2.longitude = longitude
    return location1.distanceTo(location2)
}

fun Location.getFormattedName(): String {
    val breweryName = this.brewery?.name ?: ""
    return when (this.name) {
        "Main Brewery" -> breweryName
        else -> "$breweryName (${this.name})"
    }
}

fun Location.getFormattedAddress(lineSeparator: String = ", ", includeCountry: Boolean = true): String {
    return listOf(
            this.streetAddress ?: "",
            (this.postalCode ?: "" + this.locality ?: "").trim(),
            if (includeCountry) this.countryIsoCode ?: "" else "")
            .filterNot { it.isNullOrEmpty() }
            .joinToString(lineSeparator)
}

fun Activity.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

fun Activity.requestLocationPermission(requestCode: Int) {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
}

fun Context.dimenToPixels(resourceId: Int) = resources.getDimensionPixelSize(resourceId)
fun View.dimenToPixels(resourceId: Int) = context.dimenToPixels(resourceId)

fun List<Location>.removeUnusable() = filterNot { it.inPlanning ?: true || it.isClosed ?: true }