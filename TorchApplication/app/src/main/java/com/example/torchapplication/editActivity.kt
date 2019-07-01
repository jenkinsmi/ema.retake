package com.example.torchapplication

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.editactivity.*

// Second activity to edit sensor value upper and lower boundaries

class editActivity : AppCompatActivity(), SensorEventListener {


    lateinit var lightSensor : Sensor
    var query : SQLH? = null
    var cLux : Float = 0f

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.editactivity)



            val sMgr = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            lightSensor = sMgr.getDefaultSensor(Sensor.TYPE_LIGHT)
            sMgr.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI)

            query = SQLH(this)


// Setting upper and lower boundaries

            etLoff.setText(query!!.OnSettings().toString())
            etLon.setText(query!!.OffSettings().toString())


            btnUpdate.setOnClickListener {

                query!!.insertRecord(etLoff.text.toString().toLong(), etLon.text.toString().toLong())

                val ActivityIntent = Intent(this, MainActivity::class.java)
                startActivity(ActivityIntent)

            }






        }












    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    override fun onSensorChanged(p0: SensorEvent?) {

        if(p0?.sensor == lightSensor)
        {
            cLux = p0.values[0]


            etLux.setText(cLux.toString())

        }


    }





}