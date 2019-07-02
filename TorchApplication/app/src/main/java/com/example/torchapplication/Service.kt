package com.example.torchapplication

import android.content.Intent
import android.os.IBinder
import android.app.Service
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

// Service to run app features in background

class Service : Service(), SensorEventListener{


    var L : Boolean = false


    lateinit var lightSensor : Sensor
    lateinit var camMgr: CameraManager
    lateinit var camID: String

    var query : SQLH? = null
    //lateinit var locMgr:LocationManager

    var onVal : Float = 10f
    var offVal : Float = 30f

    override fun onBind(intent: Intent):IBinder?
    {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {



        query = SQLH(this)
        val sMgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sMgr.getDefaultSensor(Sensor.TYPE_LIGHT)
        sMgr.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI)
        camMgr = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        camID = camMgr.getCameraIdList()[0]

        light(false)
        return START_NOT_STICKY
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    override fun onSensorChanged(p0: SensorEvent?) {

        if(p0?.sensor == lightSensor)
        {
////
            if(!L)
            {
                if (p0?.values[0] <= query!!.OnSettings() ) {
                    Log.d("LIGHT ON", "Reading is" + p0.values[0].toString())
                    light(true)
                    L = true
                }
            }
            else if(L)
            {
                if (p0?.values[0] >= query!!.OffSettings()) {
                    Log.d("LIGHT OFF", "Reading is" + p0.values[0].toString())
                    light(false)
                    L = false
                }
            }
            else
            {
                //error
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
    }

    fun light(v : Boolean)
    {
        camMgr.setTorchMode(camID, v)
    }
}








