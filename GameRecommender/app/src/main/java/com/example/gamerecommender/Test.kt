package com.example.gamerecommender

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class Test : AppCompatActivity() {

    private lateinit var textTest : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_display)

        // val formbody = FormBody.Builder().add("", "").add("", "").build()

        val url = URL("http://www.android.com/")
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val `in`: InputStream = BufferedInputStream(urlConnection.inputStream)
            `in`.read()
        } finally {
            urlConnection.disconnect()
        }

        val client = OkHttpClient();
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call, e: IOException) {
                //call.cancel()

                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
                runOnUiThread {
                    val responseText = findViewById<TextView>(R.id.textView)
                    responseText.text = "Failed to Connect to Server"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    val responseText = findViewById<TextView>(R.id.textView)
                    try {
                        responseText.text = response.body!!.string()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }
}

/*okHttpClient.newCall(request).enqueue(object : Callback {
    @SuppressLint("SetTextI18n")
    override fun onFailure(call: Call, e: IOException) {
        call.cancel()

        // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
        runOnUiThread {
            val responseText = findViewById<TextView>(R.id.textView)
            responseText.text = "Failed to Connect to Server"
        }
    }

    @Throws(IOException::class)
    override fun onResponse(call: Call, response: Response) {
        runOnUiThread {
            val responseText = findViewById<TextView>(R.id.textView)
            try {
                responseText.text = response.body!!.string()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


    }
})*/

/*client.newCall(request).enqueue(object : Callback {
      override fun onFailure(call: Call, e: IOException) {
        e.printStackTrace()
      }

      override fun onResponse(call: Call, response: Response) {
        response.use {
          if (!response.isSuccessful) throw IOException("Unexpected code $response")

          textTest = findViewById(R.id.textView)
          textTest.text = response.body!!.string()
        }
      }
    })*/



/*var postBodyImage: RequestBody = Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("image", "androidFlask.jpg", create(MediaType.parse("image/*jpg"), byteArray))
        .build()
*/