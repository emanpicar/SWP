package com.nokia.swp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button

class FenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fence)

        navigationSetup()
    }

    fun navigationSetup() {
        val bluetoothBtn = findViewById<Button>(R.id.bluetooth_btn)
        bluetoothBtn.setOnClickListener{
            val intent = Intent(this@FenceActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
