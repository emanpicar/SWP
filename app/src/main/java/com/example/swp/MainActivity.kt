package com.example.swp

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.swp.ui.main.SectionsPagerAdapter
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private val bTItems: HashMap<String, Short> = LinkedHashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        run()

        fab.setOnClickListener { discoverDevices()}
    }


    private fun run() {
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter == null) {
            toast("This device does not support BT")
            return
        } else {

        m_bluetoothAdapter!!.enable()
        waitBTEnabling()
        discoverDevices()
        }
    }

    private fun discoverDevices() {
        if (m_bluetoothAdapter!!.isEnabled) {
            scanDevices()
        }
    }

    private fun waitBTEnabling() {
        toast("Starting BT...")
        while(!m_bluetoothAdapter!!.isEnabled) {
            //...
        }
        toast("BT has been enabled")
    }

    private fun plot(name: String, signal: Short) {
        Log.i("Device", name + "(" + signal + ")" )
    }


    private val mFoundReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val signal = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, java.lang.Short.MIN_VALUE)
                if (device.name != null) {
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


}