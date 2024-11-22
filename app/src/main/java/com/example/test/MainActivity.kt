package com.example.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import okio.IOException
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    var URL = ""
    var okHttpClient: OkHttpClient = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val but = findViewById<Button>(R.id.button)

        val userText = findViewById<EditText>(R.id.editTextMain)

        but.setOnClickListener {
            var text = userText.text.toString().trim()
            if (text == ""){
                text = "Кострома"
            }

            URL = "https://api.openweathermap.org/data/2.5/weather?q=$text&APPID=9f32210c2c69036464b4af63231d7731&units=metric&lang=ru"

            getWether()
        }

    }
    private fun getWether() {

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        runOnUiThread {
            progressBar.visibility = View.VISIBLE
        }

        val wetherText = findViewById<TextView>(R.id.textView)
        val client = OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .build()
        val request: Request = Request.Builder().url(URL).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()
                val temp = (JSONObject(json.toString()).getJSONObject("main").get("temp")).toString()
                val feels = (JSONObject(json.toString()).getJSONObject("main").get("feels_like")).toString()
                val pressure = (JSONObject(json.toString()).getJSONObject("main").get("pressure")).toString()
                val humidity = (JSONObject(json.toString()).getJSONObject("main").get("humidity")).toString()
                val speed = (JSONObject(json.toString()).getJSONObject("wind").get("speed")).toString()
                val gust = (JSONObject(json).getJSONObject("wind").get("gust")).toString()
                val main = (JSONObject(json.toString()).getJSONObject("weather").get("main")).toString()
                
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    val txt = """
                    Темпиратура $temp
                    Ощущается как $feels
                    Давление $pressure
                    """.trimIndent()
                    wetherText.text = txt
                }
            }
        })
    }
}

