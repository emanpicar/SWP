package com.nokia.swp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.WebView
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationSetup()
        radarBG()
    }

    fun navigationSetup() {
        val fenceBtn = findViewById<Button>(R.id.fence_btn)
        fenceBtn.setOnClickListener{
           val intent = Intent(this@MainActivity, FenceActivity::class.java)
            startActivity(intent)
        }
    }

    fun radarBG() {
        val content = findViewById<WebView>(R.id.blutooth_radar)
        content.settings.javaScriptEnabled = true
        content.settings.loadWithOverviewMode = true
        content.settings.useWideViewPort = true
        content.loadUrl("file:android_asset/radar.gif")
    }
}
