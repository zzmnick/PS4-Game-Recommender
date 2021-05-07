package com.example.gamerecommender

import android.R.attr
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.lang.ref.WeakReference


class SubInput : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sub_input)

        backButton = findViewById(R.id.back_button)
        submitButton = findViewById(R.id.text_submit)

        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        submitButton.setOnClickListener {
            startActivity(Intent(this, RecommendationScreen::class.java))
        }
    }
}
