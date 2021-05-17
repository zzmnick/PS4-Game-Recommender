package com.example.gamerecommender

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {

    private lateinit var imageUploadOne: ImageView

    private lateinit var uploadButton: Button


    private lateinit var cameraButtonOne: Button

    private lateinit var image1: Bitmap

    private lateinit var apiService:ApiService
    private lateinit var textView: TextView



    private val RESULT_CAMERA_IMAGE_ONE = 4

    private var a = 0
    private var uploaded = 0
    private var results = ""


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageUploadOne = findViewById(R.id.imageToUpload_1)


        uploadButton = findViewById(R.id.buttonUploadImage)



        cameraButtonOne = findViewById(R.id.camera_One)
        textView = findViewById(R.id.textView)

        val client = OkHttpClient.Builder().build()
        resetApp()
        apiService =
            Retrofit.Builder().baseUrl("http://34.67.190.198:3000").client(client).build().create(
                ApiService::class.java
            )


        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)



        cameraButtonOne.setOnClickListener{
            if(uploaded != 3) {
                if (cameraIntent.resolveActivity(packageManager) != null) {
                    startActivityForResult(cameraIntent, RESULT_CAMERA_IMAGE_ONE)
                } else {
                    Toast.makeText(
                        this,
                        "There is no app that supports this action",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }


        uploadButton.setOnClickListener{
            if(uploaded != 3){
            if(a==1){
                if(uploaded == 2){
                    val placeholder = "Loading results..."
                    textView.setText(placeholder)
                }
                multipartImageUpload(image1)
                imageUploadOne.setImageBitmap(null)
                a=0
            } else Toast.makeText(applicationContext,"Click an image using the camera!",Toast.LENGTH_LONG).show()
        }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null) {
            val bundle :Bundle?
            val camPhoto : Bitmap
            when(requestCode) {
                RESULT_CAMERA_IMAGE_ONE -> {
                    bundle = data.extras
                    if (bundle != null) {
                        camPhoto = bundle.get("data") as Bitmap
                        val bytes = ByteArrayOutputStream()
                        camPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                        image1 = camPhoto
                        a = 1
                        imageUploadOne.setImageBitmap(camPhoto)
                    } else {
                        Toast.makeText(this, "Unable to upload camera photo", Toast.LENGTH_LONG).show()
                    }
                }

                else -> {
                    Toast.makeText(this, "Unable to successfully upload image to imageView", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun multipartImageUpload(image:Bitmap) {
        try {
            val filesDir: File = applicationContext.filesDir
            val file = File(filesDir, "image" + ".png")
            val bos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapdata = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            val body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile)
            val name = RequestBody.create(MediaType.parse("text/plain"), "upload")
            val req: Call<ResponseBody?>? = apiService.postImage(body, name)
            if (req != null) {
                req.enqueue(object : Callback<ResponseBody?> {
                    @RequiresApi(Build.VERSION_CODES.KITKAT)
                    override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {
                        uploaded++
                        val toast:String
                        if(uploaded == 3) {
                            toast = "Upload Successful! Results loading..."
                        } else toast = "Upload Successful! Upload another image!"
                        Toast.makeText(
                            applicationContext,
                            toast,
                            Toast.LENGTH_SHORT
                        ).show()
                        val place:String = "Uploaded "+uploaded+"/3"
                        textView.setText(place)
                        val thread = Thread {
                            try {
                                getResults()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        if(uploaded == 3) {
                            thread.start()
                            thread.join()
                            if(results.contains("###ERROR###")){
                                Toast.makeText(applicationContext, "Something went wrong! Try again with a better image...",Toast.LENGTH_LONG).show()
                                resetApp()
                            }else {
                                val intent: Intent =
                                    Intent(applicationContext, Results1::class.java)
                                intent.putExtra("Results", results)
                                startActivity(intent)
                                uploaded = 0
                                resetApp()
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
                        Toast.makeText(applicationContext, "Request failed", Toast.LENGTH_SHORT).show()
                        t.printStackTrace()
                    }
                })
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getResults(){
        val url = "http://34.67.190.198:3000/compute/"
        val myurl = URL(url)
        val con:HttpURLConnection =  myurl.openConnection() as HttpURLConnection
        try {
            con.setRequestMethod("GET")
            var content: StringBuilder
            BufferedReader(
                InputStreamReader(con.getInputStream())
            ).use { `in` ->
                var line: String?
                content = StringBuilder()
                while (`in`.readLine().also { line = it } != null) {
                    content.append(line)
                    content.append(System.lineSeparator())
                }
            }
            results = content.toString()
        } finally {
            con.disconnect()
        }
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun resetApp(){
        a = 0
        uploaded = 0
        val place = "Uploaded: 0/3"
        textView.setText(place)
        imageUploadOne.setImageBitmap(null)
        results = ""
        val thread = Thread {
            try {
                clearUploads()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun clearUploads(){
        val url = "http://34.67.190.198:3000/clear/"
        val myurl = URL(url)
        val con:HttpURLConnection =  myurl.openConnection() as HttpURLConnection
        try {
            con.setRequestMethod("GET")
            var content: StringBuilder
            BufferedReader(
                InputStreamReader(con.getInputStream())
            ).use { `in` ->
                var line: String?
                content = StringBuilder()
                while (`in`.readLine().also { line = it } != null) {
                    content.append(line)
                    content.append(System.lineSeparator())
                }
            }
        } finally {
            con.disconnect()
        }
    }
}
