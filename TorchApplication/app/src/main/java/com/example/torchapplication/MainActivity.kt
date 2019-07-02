package com.example.torchapplication

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.camera2.CameraManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.etLon
import kotlinx.android.synthetic.main.editactivity.*
import kotlinx.android.synthetic.main.editactivity.etLoff
import org.jetbrains.anko.toast

// Main activity
class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var lightSensor: Sensor
    lateinit var camMgr: CameraManager
    lateinit var camID: String

    var query : SQLH? = null
    var cLux : Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


// Permissions code

        if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
        {
            val startIntent = Intent(this, Service::class.java)
            startService(startIntent)

        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
        }



// Setting up light sensor

        val sMgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sMgr.getDefaultSensor(Sensor.TYPE_LIGHT)
        sMgr.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI)

        query = SQLH(this)

        camMgr = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        camID = camMgr.getCameraIdList()[0]

// Set upper and lower boundaries to value

        etLoff.setText(query!!.OffSettings().toString())
        etLon.setText(query!!.OnSettings().toString())


 // Floating action button to send user to editActivity

        fab.setOnClickListener {

            val editActivityIntent = Intent(this, editActivity::class.java)
            startActivity(editActivityIntent)


        }

    }

    override fun onStart() {
        super.onStart()

// Permissions code

        if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
        {
            val startIntent = Intent(this, Service::class.java)
            startService(startIntent)

        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
        }
    }


// Update light sensor reading value on sensor changed

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    override fun onSensorChanged(p0: SensorEvent?) {

        if(p0?.sensor == lightSensor)
        {
            cLux = p0.values[0]

            etCLux.setText(cLux.toString())
        }


    }


// Permissions callback

    override fun onRequestPermissionsResult(requestCode:Int, permissions:Array<String>, grantResults: IntArray) {
        when(requestCode) {
            0 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   toast("Torch Perm allowed")
                    val startIntent = Intent(this, Service::class.java)
                    startService(startIntent)

                } else {
                    toast("Torch Perm denied")

                }
            }
        }

    }
}









