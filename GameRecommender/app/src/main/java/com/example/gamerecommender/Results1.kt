package com.example.gamerecommender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class Results1 : AppCompatActivity() {
    private lateinit var listView:ListView
    private lateinit var button:Button
    private lateinit var arrayAdapter: ArrayAdapter<String>

    val preds:ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results1)
        listView = findViewById(R.id.listView1)
        button = findViewById(R.id.next_button)
        val intent: Intent = getIntent()
        val results:String = intent.getStringExtra("Results").toString()
        val arrayList:ArrayList<String> = ArrayList<String>()
        var beg = 0
        var end = 0
        val l = results.length
        while(end<l){
            if(results.get(end) == '*'){
                arrayList.add(results.subSequence(beg,end).toString())
                beg = end+3
                end = end+3
            }
            else end++
        }
        preds.add(arrayList[0])
        preds.add(arrayList[1])
        preds.add(arrayList[2])
        arrayAdapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_1,preds)
        listView.adapter = arrayAdapter

        button.setOnClickListener{
            val predIntent:Intent = Intent(applicationContext,Results2::class.java)
            predIntent.putExtra("one",arrayList[3])
            predIntent.putExtra("two",arrayList[4])
            predIntent.putExtra("three",arrayList[5])
            predIntent.putExtra("four",arrayList[6])
            predIntent.putExtra("five",arrayList[7])
            predIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(predIntent)
            finish()
        }
    }
}