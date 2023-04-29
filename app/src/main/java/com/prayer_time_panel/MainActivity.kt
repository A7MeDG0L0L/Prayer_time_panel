package com.prayer_time_panel

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.samsung.android.sdk.SsdkUnsupportedException
import com.samsung.android.sdk.look.Slook



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("Sad","message")
        val slook = Slook()
        try {
            slook.initialize(this)
        } catch (e: SsdkUnsupportedException) {
            return
        }
        if (slook.isFeatureEnabled(Slook.COCKTAIL_PANEL)) {
            println("Edge Single Mode Supported")
// The Device supports Edge Single Mode, Edge Single Plus Mode, and Edge Feeds Mode.
        }
        if (slook.isFeatureEnabled(Slook.COCKTAIL_BAR)) {
            println("Edge Immersive Mode feature Supported")
// The Device supports Edge Immersive Mode feature.
        }

    }
}