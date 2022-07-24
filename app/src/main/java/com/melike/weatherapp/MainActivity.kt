package com.melike.weatherapp

import android.Manifest
import android.content.pm.LauncherActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    val CITY: String = "dhaka,bd"
    val API: String = "670e093254bec9705c554fce24598b3e"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }

    inner class weatherTask() : AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errortext).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try {
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }
            catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated ad: "+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = (main.getInt("temp")).toString() +"°C"
                val tempMin = "Min Temp: "+(main.getInt("temp_min")).toString()+"°C"
                val tempMax = "Min Temp: "+(main.getInt("temp_max")).toString()+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name")+", "+sys.getString("country")



                findViewById<TextView>(R.id.adress).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE


                if(findViewById<TextView>(R.id.status).text == "Sunny"){
                    val relativeLayout = findViewById<View>(R.id.relativeLayout) as RelativeLayout
                    relativeLayout.setBackgroundResource(R.drawable.sunny)
                }
                else if(findViewById<TextView>(R.id.status).text == "Cloudy"){
                    val relativeLayout = findViewById<View>(R.id.relativeLayout) as RelativeLayout
                    relativeLayout.setBackgroundResource(R.drawable.cloudy)
                }
                else if(findViewById<TextView>(R.id.status).text == "Snowy"){
                    val relativeLayout = findViewById<View>(R.id.relativeLayout) as RelativeLayout
                    relativeLayout.setBackgroundResource(R.drawable.snowy)
                }
                else if(findViewById<TextView>(R.id.status).text == "Rainy"){
                    val relativeLayout = findViewById<View>(R.id.relativeLayout) as RelativeLayout
                    relativeLayout.setBackgroundResource(R.drawable.rainy)
                }
                else if(findViewById<TextView>(R.id.status).text == "Haze"){
                    val relativeLayout = findViewById<View>(R.id.relativeLayout) as RelativeLayout
                    relativeLayout.setBackgroundResource(R.drawable.haze)
                }



            }
            catch (e: Exception){
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
            }


        }

    }


}