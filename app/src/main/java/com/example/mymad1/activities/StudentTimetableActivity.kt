package com.example.mymad1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R
import com.example.mymad1.models.TimetableModel
import com.google.firebase.database.FirebaseDatabase

class StudentTimetableActivity : AppCompatActivity() {
    //    private lateinit var tvCode: TextView
    private lateinit var tvName: TextView
    private lateinit var tvSubject: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_timetable_student)

        initView()
        setValuesToViews()
    }

    private fun initView() {
//        tvCode = findViewById(R.id.tvTimetableCode)
        tvName = findViewById(R.id.tvTeacherName)
        tvSubject = findViewById(R.id.tvSubjectName)
        tvDate = findViewById(R.id.tvTimetableDate)
        tvTime = findViewById(R.id.tvTimetableTime)
        tvLink = findViewById(R.id.tvTimetableLink)
    }

    private fun setValuesToViews() {
//        tvCode.text = intent.getStringExtra("timetableCode")
        tvName.text = intent.getStringExtra("teacherName")
        tvSubject.text = intent.getStringExtra("subjectName")
        tvDate.text = intent.getStringExtra("timetableDate")
        tvTime.text = intent.getStringExtra("timetableTime")
        tvLink.text = intent.getStringExtra("timetableLink")
    }
}