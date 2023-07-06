package com.example.mymad1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R

class SelectSubjectActivity : AppCompatActivity() {
    private lateinit var btnMaths : Button
    private lateinit var btnBio : Button
    private lateinit var btnChem : Button
    private lateinit var btnPhy : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timetable_sublist)

        btnMaths = findViewById(R.id.btnMathsTt)
        btnBio = findViewById(R.id.btnBioTt)
        btnChem = findViewById(R.id.btnChemTt)
        btnPhy = findViewById(R.id.btnPhyTt)

        //maths teacher details
        btnMaths.setOnClickListener{
            val condition : Int = 1
            val intent = Intent(this, FetchingTimetableTeacher::class.java)
            intent.putExtra("condition", condition)
            startActivity(intent)
        }

        //bio teacher details
        btnBio.setOnClickListener{
            val condition : Int = 2
            val intent = Intent(this, FetchingTimetableTeacher::class.java)
            intent.putExtra("condition", condition)
            startActivity(intent)
        }

        //chemistry teacher details
        btnChem.setOnClickListener{
            val condition : Int = 3
            val intent = Intent(this, FetchingTimetableTeacher::class.java)
            intent.putExtra("condition", condition)
            startActivity(intent)
        }

        //physics teacher details
        btnPhy.setOnClickListener{
            val condition : Int = 4
            val intent = Intent(this, FetchingTimetableTeacher::class.java)
            intent.putExtra("condition", condition)
            startActivity(intent)
        }
    }
}