package com.example.bluetoothkotlin

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private var btPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun scanBt(view: View) {
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "BT接続が許可されていません", Toast.LENGTH_LONG).show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                blueToothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                blueToothPermissionLauncher.launch(android.Manifest.permission.BLUETOOTH)
            }
        }
    }

    private val blueToothPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
            btPermission = true

            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btActivityResultLauncher.launch(enableBtIntent)
            } else {
                btScan()
            }
        } else {
            btPermission = false
        }
    }

    private val btActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            btScan()
        }
    }

    private fun btScan() = Log.d("###BT接続", "接続可能");
}