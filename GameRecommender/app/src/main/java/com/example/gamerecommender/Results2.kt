package com.example.gamerecommender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class Results2 : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var button: Button
    private lateinit var arrayAdapter: ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results2)
        listView = findViewById(R.id.listView2)
        button = findViewById(R.id.reset_button)
        val intent: Intent = getIntent()
        val preds:ArrayList<String> = ArrayList()
        preds.add(intent.getStringExtra("one").toString())
        preds.add(intent.getStringExtra("two").toString())
        preds.add(intent.getStringExtra("three").toString())
        preds.add(intent.getStringExtra("four").toString())
        preds.add(intent.getStringExtra("five").toString())
        arrayAdapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,preds)
        listView.adapter = arrayAdapter

        button.setOnClickListener{
            finish()
        }
    }
}