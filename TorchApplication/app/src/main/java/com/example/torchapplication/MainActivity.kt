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


class MainActivity : AppCompatActivity(), SensorEventListener {

    lateinit var lightSensor: Sensor
    lateinit var camMgr: CameraManager
    lateinit var camID: String

    var query : SQLH? = null
    var cLux : Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)








        val sMgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sMgr.getDefaultSensor(Sensor.TYPE_LIGHT)
        sMgr.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI)

        query = SQLH(this)

        camMgr = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        camID = camMgr.getCameraIdList()[0]


        etLoff.setText(query!!.OnSettings().toString())
        etLon.setText(query!!.OffSettings().toString())







        fab.setOnClickListener {

            val editActivityIntent = Intent(this, editActivity::class.java)
            startActivity(editActivityIntent)


        }
//////goto new activity


    }

    override fun onStart() {
        super.onStart()

        if(checkSelfPermission(Manifest.permission.FLASHLIGHT)== PackageManager.PERMISSION_GRANTED)
        {
            val startIntent = Intent(this, Service::class.java)
            startService(startIntent)

        } else {
            requestPermissions(arrayOf(Manifest.permission.FLASHLIGHT), 0)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    override fun onSensorChanged(p0: SensorEvent?) {

        if(p0?.sensor == lightSensor)
        {
            cLux = p0.values[0]

            etCLux.setText(cLux.toString())
        }


    }








    override fun onRequestPermissionsResult(requestCode:Int, permissions:Array<String>, grantResults: IntArray) {
        when(requestCode) {
            0 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   toast("Torch Perm allowed")

                } else {
                    toast("Torch Perm denied")

                }
            }
        }

    }
}









