package com.example.gamerecommender

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*


class MainActivity : AppCompatActivity() {

    private lateinit var imageUploadOne: ImageView
    private lateinit var imageUploadTwo: ImageView
    private lateinit var imageUploadThree: ImageView

    private lateinit var uploadButton: Button

    private lateinit var selectButtonOne: Button
    private lateinit var selectButtonTwo: Button
    private lateinit var selectButtonThree: Button

    private lateinit var cameraButtonOne: Button
    private lateinit var cameraButtonTwo: Button
    private lateinit var cameraButtonThree: Button
    private lateinit var image1: Bitmap
    private lateinit var image2: Bitmap
    private lateinit var image3: Bitmap
    private lateinit var apiService:ApiService


    private val RESULT_LOAD_IMAGE_ONE = 1
    private val RESULT_LOAD_IMAGE_TWO = 2
    private val RESULT_LOAD_IMAGE_THREE = 3
    private val RESULT_CAMERA_IMAGE_ONE = 4
    private val RESULT_CAMERA_IMAGE_TWO = 5
    private val RESULT_CAMERA_IMAGE_THREE = 6
    private var a = 0
    private var b = 0
    private var c = 0

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageUploadOne = findViewById(R.id.imageToUpload_1)
        imageUploadTwo = findViewById(R.id.imageToUpload_2)
        imageUploadThree = findViewById(R.id.imageToUpload_3)

        uploadButton = findViewById(R.id.buttonUploadImage)

        selectButtonOne = findViewById(R.id.gallery_One)
        selectButtonTwo = findViewById(R.id.gallery_Two)
        selectButtonThree = findViewById(R.id.gallery_Three)

        cameraButtonOne = findViewById(R.id.camera_One)
        cameraButtonTwo = findViewById(R.id.camera_Two)
        cameraButtonThree = findViewById(R.id.camera_Three)
        val client = OkHttpClient.Builder().build()

        apiService =
            Retrofit.Builder().baseUrl("http://192.168.1.12:3000").client(client).build().create(
                ApiService::class.java
            )


        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        selectButtonOne.setOnClickListener {
            if(cameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_ONE)
            } else {
                Toast.makeText(this, "Gallery does not exist on this phone", Toast.LENGTH_LONG).show()
            }
        }

        selectButtonTwo.setOnClickListener {
            if(cameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_TWO)
            } else {
                Toast.makeText(this, "Gallery does not exist on this phone", Toast.LENGTH_LONG).show()
            }
        }

        selectButtonThree.setOnClickListener {
            if(cameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_THREE)
            } else {
                Toast.makeText(this, "Gallery does not exist on this phone", Toast.LENGTH_LONG).show()
            }
        }

        cameraButtonOne.setOnClickListener{
            if(cameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(cameraIntent, RESULT_CAMERA_IMAGE_ONE)
            } else {
                Toast.makeText(this, "There is no app that supports this action", Toast.LENGTH_LONG).show()
            }
        }

        cameraButtonTwo.setOnClickListener{
            if(cameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(cameraIntent, RESULT_CAMERA_IMAGE_TWO)
            } else {
                Toast.makeText(this, "There is no app that supports this action", Toast.LENGTH_LONG).show()
            }
        }

        cameraButtonThree.setOnClickListener{
            if(cameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(cameraIntent, RESULT_CAMERA_IMAGE_THREE)
            } else {
                Toast.makeText(this, "There is no app that supports this action", Toast.LENGTH_LONG).show()
            }
        }
        uploadButton.setOnClickListener{
            if(a>0 && b>0 && c>0){
                multipartImageUpload(image1)
                multipartImageUpload(image2)
                multipartImageUpload(image3)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null) {
            val bundle :Bundle?
            val camPhoto : Bitmap
            when(requestCode) {
                RESULT_LOAD_IMAGE_ONE -> imageUploadOne.setImageURI(data.data)
                RESULT_LOAD_IMAGE_TWO -> imageUploadTwo.setImageURI(data.data)
                RESULT_LOAD_IMAGE_THREE -> imageUploadThree.setImageURI(data.data)
                RESULT_CAMERA_IMAGE_ONE -> {
                    bundle = data.extras
                    if (bundle != null) {
                        camPhoto = bundle.get("data") as Bitmap
                        val bytes = ByteArrayOutputStream()
                        camPhoto.compress(Bitmap.CompressFormat.JPEG, 0, bytes)
                        image1 = camPhoto
                        a = 1
                        imageUploadOne.setImageBitmap(camPhoto)
                    } else {
                        Toast.makeText(this, "Unable to upload camera photo", Toast.LENGTH_LONG).show()
                    }
                }
                RESULT_CAMERA_IMAGE_TWO -> {
                    bundle = data.extras
                    if (bundle != null) {
                        camPhoto = bundle.get("data") as Bitmap
                        val bytes = ByteArrayOutputStream()
                        camPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                        image2 = camPhoto
                        b = 1
                        imageUploadTwo.setImageBitmap(camPhoto)
                    } else {
                        Toast.makeText(this, "Unable to upload camera photo", Toast.LENGTH_LONG).show()
                    }
                }
                RESULT_CAMERA_IMAGE_THREE -> {
                    bundle = data.extras
                    if (bundle != null) {
                        camPhoto = bundle.get("data") as Bitmap
                        val bytes = ByteArrayOutputStream()
                        camPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                        image3 = camPhoto
                        c = 1
                        imageUploadThree.setImageBitmap(camPhoto)
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
                    override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>) {

                        Toast.makeText(
                            applicationContext,
                            response.code().toString() + " ",
                            Toast.LENGTH_SHORT
                        ).show()
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
}