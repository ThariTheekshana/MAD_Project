package com.example.mymad1.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R
import com.example.mymad1.models.EmployeeModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : AppCompatActivity() {

    private lateinit var etStdName: EditText
    private lateinit var etStdSubject: EditText
    private lateinit var etStdMarks: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)

        etStdName = findViewById(R.id.etStdName)
        etStdSubject = findViewById(R.id.etStdSubject)
        etStdMarks = findViewById(R.id.etStdMarks)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Student Results")

        btnSaveData.setOnClickListener {
            saveEmployeeData()
        }
    }

    private fun saveEmployeeData() {
        val stdName = etStdName.text.toString()
        val stdSubject = etStdSubject.text.toString()
        val stdMarks = etStdMarks.text.toString()

        if (stdName.isNotEmpty() && stdSubject.isNotEmpty() && stdMarks.isNotEmpty()) {
            val stdId = dbRef.push().key!!

            val student = EmployeeModel(stdId, stdName, stdSubject, stdMarks)

            dbRef.child(stdId).setValue(student)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                    etStdName.text.clear()
                    etStdSubject.text.clear()
                    etStdMarks.text.clear()
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_LONG).show()
        }
    }
}
