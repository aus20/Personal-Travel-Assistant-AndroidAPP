package com.travelassistant

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TravelAssistantApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Uygulama başladığında yapılacak ek başlatma işlemleri (varsa) buraya eklenebilir.
    }
}