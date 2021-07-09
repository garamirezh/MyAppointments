package com.digitalarray.myappointments.ui

import android.app.Application
import com.google.android.gms.ads.MobileAds

class MyAppointmentsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}