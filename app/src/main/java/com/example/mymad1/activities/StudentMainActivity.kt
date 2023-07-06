package com.example.mymad1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.mymad1.R

class StudentMainActivity:AppCompatActivity() {


    private lateinit var btnViewData: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_student)


        btnViewData = findViewById(R.id.btnViewData)


        btnViewData.setOnClickListener {
            val intent = Intent(this, StudentFetchingActivity::class.java)  //change activity
            startActivity(intent)
        }



    }
}