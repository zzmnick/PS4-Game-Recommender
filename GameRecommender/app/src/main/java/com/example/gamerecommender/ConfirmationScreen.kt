package com.example.gamerecommender

import android.R.attr
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
import okhttp3.*
import org.jetbrains.annotations.NotNull
import java.io.*
import java.lang.ref.WeakReference
import java.net.URI


class ConfirmationScreen : AppCompatActivity() {

    private lateinit var toText: Button
    private lateinit var toRecommender: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirmation_screen)

        toText = findViewById(R.id.toTextButton)
        toRecommender = findViewById(R.id.toRecommenderButton)

        toText.setOnClickListener {
            startActivity(Intent(this, SubInput::class.java))
        }

        toRecommender.setOnClickListener {
            startActivity(Intent(this, RecommendationScreen::class.java))
        }


    }

}

