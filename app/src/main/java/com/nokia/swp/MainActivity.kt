package com.nokia.swp

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private val DEVICE_PREFIX = "SWP"
    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private val bTItems: HashMap<String, Short> = LinkedHashMap()
    private val REQUEST_ENABLE_BLUETOOTH = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigationSetup()
        radarBG()
        run()
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

    private fun run() {
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter == null) {
            toast("This device does not support Bluetooth")
            finish()
        }
        if(!m_bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }
    }

    private fun discoverDevices() {
        if (m_bluetoothAdapter!!.isEnabled) {
            scanDevices()
        }
    }

    private fun plot(name: String, signal: Short) {
        Log.i("Bluetooth Device:", name + "(" + signal + ")" )
    }

    private val mFoundReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val signal = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, java.lang.Short.MIN_VALUE)
                if (device.name != null && device.name.startsWith(DEVICE_PREFIX)) {
                    plot(device.name, signal)
                }
            }
        }
    }

    private fun scanDevices(){
        if (m_bluetoothAdapter!!.isDiscovering) {
            // Bluetooth is already in mode discovery mode, we cancel to restart it again
            m_bluetoothAdapter!!.cancelDiscovery()
        }
        val bool = m_bluetoothAdapter?.startDiscovery()
        Log.i("", bool.toString())
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mFoundReceiver, filter)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (m_bluetoothAdapter!!.isEnabled) {
                    toast("Bluetooth has been enabled")
                    discoverDevices()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                toast("Bluetooth enabling has been cancelled")
            }
        }

    }
}
