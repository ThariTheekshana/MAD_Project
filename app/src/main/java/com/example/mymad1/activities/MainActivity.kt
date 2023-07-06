package com.example.mymad1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mymad1.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    //Login btn variable
    private lateinit var btnLoginDashBoard: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()

        btnLoginDashBoard = findViewById(R.id.btnDashboardLogin)

        //Change to Login activities
        btnLoginDashBoard.setOnClickListener {
            val intent = Intent(this, TeacherStudentLogin::class.java)
            startActivity(intent)
        }


    }
}