package com.example.gamerecommender

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.*


class MainActivity : AppCompatActivity() {

    private lateinit var imageUploadOne: ImageView
    private lateinit var imageUploadTwo: ImageView
    private lateinit var imageUploadThree: ImageView

    private lateinit var uploadButton: Button
    private lateinit var forwardButton: Button

    private lateinit var selectButtonOne: Button
    private lateinit var selectButtonTwo: Button
    private lateinit var selectButtonThree: Button

    private lateinit var cameraButtonOne: Button
    private lateinit var cameraButtonTwo: Button
    private lateinit var cameraButtonThree: Button

    private lateinit var image_upload_one: Uri
    private lateinit var image_upload_two: Uri
    private lateinit var image_upload_three: Uri

    private val RESULT_LOAD_IMAGE_ONE = 1
    private val RESULT_LOAD_IMAGE_TWO = 2
    private val RESULT_LOAD_IMAGE_THREE = 3
    private val RESULT_CAMERA_IMAGE_ONE = 4
    private val RESULT_CAMERA_IMAGE_TWO = 5
    private val RESULT_CAMERA_IMAGE_THREE = 6

    private lateinit var currentPhotoPath : String


    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageUploadOne = findViewById(R.id.imageToUpload_1)
        imageUploadTwo = findViewById(R.id.imageToUpload_2)
        imageUploadThree = findViewById(R.id.imageToUpload_3)

        uploadButton = findViewById(R.id.buttonUploadImage)
        forwardButton = findViewById(R.id.forward_button)

        selectButtonOne = findViewById(R.id.gallery_One)
        selectButtonTwo = findViewById(R.id.gallery_Two)
        selectButtonThree = findViewById(R.id.gallery_Three)

        cameraButtonOne = findViewById(R.id.camera_One)
        cameraButtonTwo = findViewById(R.id.camera_Two)
        cameraButtonThree = findViewById(R.id.camera_Three)

        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        forwardButton.setOnClickListener {
            startActivity(Intent(this, SubInput::class.java))
        }

        uploadButton.setOnClickListener {
            startActivity(Intent(this, ConfirmationScreen::class.java))
        }


        selectButtonOne.setOnClickListener {
            if(cameraIntent.resolveActivity(packageManager) != null) {
                val imageUri = imageUpload("image_one")
                galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_ONE)
            } else {
                Toast.makeText(this, "Gallery does not exist on this phone", Toast.LENGTH_LONG).show()
            }
        }

        selectButtonTwo.setOnClickListener {
            if(cameraIntent.resolveActivity(packageManager) != null) {
                val imageUri = imageUpload("image_two")
                galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_TWO)
            } else {
                Toast.makeText(this, "Gallery does not exist on this phone", Toast.LENGTH_LONG).show()
            }
        }

        selectButtonThree.setOnClickListener {
            if(cameraIntent.resolveActivity(packageManager) != null) {
                val imageUri = imageUpload("image_three")
                galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE_THREE)
            } else {
                Toast.makeText(this, "Gallery does not exist on this phone", Toast.LENGTH_LONG).show()
            }
        }

        cameraButtonOne.setOnClickListener{
            if(cameraIntent.resolveActivity(packageManager) != null) {
                val imageUri = imageUpload("image_one")
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(cameraIntent, RESULT_CAMERA_IMAGE_ONE)
            } else {
                Toast.makeText(this, "There is no app that supports this action", Toast.LENGTH_LONG).show()
            }
        }

        cameraButtonTwo.setOnClickListener{
            if(cameraIntent.resolveActivity(packageManager) != null) {
                val imageUri = imageUpload("image_two")
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(cameraIntent, RESULT_CAMERA_IMAGE_TWO)
            } else {
                Toast.makeText(this, "There is no app that supports this action", Toast.LENGTH_LONG).show()
            }
        }

        cameraButtonThree.setOnClickListener{
            if(cameraIntent.resolveActivity(packageManager) != null) {
                val imageUri = imageUpload("image_three")
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(cameraIntent, RESULT_CAMERA_IMAGE_THREE)
            } else {
                Toast.makeText(this, "There is no app that supports this action", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && data != null) {
            val bundle :Bundle?
            when(requestCode) {
                RESULT_LOAD_IMAGE_ONE -> {
                    image_upload_one = data.data!!;
                    imageUploadOne.setImageURI(data.data)
                }
                RESULT_LOAD_IMAGE_TWO -> {
                    image_upload_two = data.data!!;
                    imageUploadTwo.setImageURI(data.data)
                }
                RESULT_LOAD_IMAGE_THREE -> {
                    image_upload_three = data.data!!;
                    imageUploadThree.setImageURI(data.data)
                }
                RESULT_CAMERA_IMAGE_ONE -> {
                    image_upload_one = data.data!!;
                    bundle = data.extras
                    if (bundle != null) {
                        val camPhoto = data.extras?.get("data") as Bitmap
                        val bytes = ByteArrayOutputStream()
                        camPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                        imageUploadOne.setImageBitmap(camPhoto)
                    } else {
                        Toast.makeText(this, "Unable to upload camera photo", Toast.LENGTH_LONG).show()
                    }
                }
                RESULT_CAMERA_IMAGE_TWO -> {
                    image_upload_two = data.data!!;
                    bundle = data.extras
                    if (bundle != null) {
                        val camPhoto = data.extras?.get("data") as Bitmap
                        val bytes = ByteArrayOutputStream()
                        camPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                        imageUploadTwo.setImageBitmap(camPhoto)
                    } else {
                        Toast.makeText(this, "Unable to upload camera photo", Toast.LENGTH_LONG).show()
                    }
                }
                RESULT_CAMERA_IMAGE_THREE -> {
                    image_upload_three = data.data!!;
                    bundle = data.extras
                    if (bundle != null) {
                        val camPhoto = data.extras?.get("data") as Bitmap
                        val bytes = ByteArrayOutputStream()
                        camPhoto.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
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

    fun imageUpload(filename : String): Uri? {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        try {
            val imageFile = File.createTempFile(filename, ".jpg", storageDirectory)
            currentPhotoPath = imageFile.absolutePath
            return FileProvider.getUriForFile(this, "com.example.gamerecommender.fileprovider", imageFile)
        } catch (e : IOException) {
            e.printStackTrace()
        }

        return null
    }



}