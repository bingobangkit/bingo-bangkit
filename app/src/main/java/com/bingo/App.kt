package com.bingo

import android.app.Application
import com.bingo.gobin.R
import com.mapbox.mapboxsdk.Mapbox

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Mapbox.getInstance(this, getString(R.string.access_token))
    }
}