package com.example.mymad1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R

class TimetableSetup : AppCompatActivity() {
    private lateinit var btnInsertData: Button
    private lateinit var btnFetchData: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timetable_setup)

        btnInsertData = findViewById(R.id.btnAddTimetable)
        btnFetchData = findViewById(R.id.btnViewTimetable)

        //insert btn
        btnInsertData.setOnClickListener{
            val intent = Intent(this, InsertionTimetable::class.java)
            startActivity(intent)
        }

        //view btn
        btnFetchData.setOnClickListener{
            val intent = Intent(this, FetchingTimetable::class.java)
            startActivity(intent)
        }
    }
}