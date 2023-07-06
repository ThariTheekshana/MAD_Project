package com.example.mymad1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TeacherStudentLogin : AppCompatActivity()  {
    private lateinit var btnTeacherLogin: Button
    private lateinit var btnStudentLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_student)

       // val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()

        //btnDeleteProfile = findViewById(R.id.btnDeleteProfile)
        btnTeacherLogin = findViewById(R.id.btnTeacherLog)
        btnStudentLogin = findViewById(R.id.btnStudentLog)

        btnTeacherLogin.setOnClickListener {
            val intent = Intent(this, CheckTeacherCode::class.java)
            startActivity(intent)
        }
        btnStudentLogin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }



    }
}