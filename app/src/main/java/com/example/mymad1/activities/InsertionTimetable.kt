package com.example.mymad1.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.models.TimetableModel
import com.example.mymad1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.values
import java.util.*

class InsertionTimetable : AppCompatActivity() {
    //variable declaration
    private lateinit var etName : EditText
    private lateinit var spnSubject : Spinner
    private lateinit var etDate : EditText
    private lateinit var etTime : EditText
    private lateinit var etEndTime : EditText
    private lateinit var etLink : EditText
    private lateinit var btnSaveTimetable : Button

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_timetable)

        //variable initialization
        etName = findViewById(R.id.addTeacherName)
        spnSubject = findViewById(R.id.addSubject)
        etDate = findViewById(R.id.addDate)
        etTime = findViewById(R.id.addTime)
        etEndTime = findViewById(R.id.addEndTime)
        etLink = findViewById(R.id.addLink)
        btnSaveTimetable = findViewById(R.id.btnSaveTimetable)

        //get current user(teacher)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid.toString()
        val userRef = FirebaseDatabase.getInstance().getReference("teachers").child(userId)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // get teacher name
                val teacherName = dataSnapshot.child("name").value as String
                // set teacher name
                etName.setText(teacherName)
                //disable etName
                etName.isEnabled = false
                etName.isFocusable = false
            }
            override fun onCancelled(error: DatabaseError) {
                // handle error
            }
        }
        userRef.addListenerForSingleValueEvent(valueEventListener)

        val subjectsArray = resources.getStringArray(R.array.subjects_array)

        // default item in array
        val defaultItem = "Select subject"
        val subjectsList = mutableListOf(defaultItem)
        subjectsList.addAll(subjectsArray)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, subjectsList)
        spnSubject.adapter = adapter

        // Set etDate, etTime, etEndTime EditText views to be non-editable
        etDate.keyListener = null
        etTime.keyListener = null
        etEndTime.keyListener = null

        etDate.setOnClickListener {
            showDatePicker()
        }

        etTime.setOnClickListener {
            showTimePicker()
        }

        etEndTime.setOnClickListener {
            showEndTimePicker()
        }

        dbRef = FirebaseDatabase.getInstance().getReference("Timetable")

        btnSaveTimetable.setOnClickListener{
            saveTimetableData()
        }
    }

    private fun saveTimetableData(){
        //get current user (teacher)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        //get values
        val teacherId = userId.toString()
        val teacherName = etName.text.toString()
        val subjectName = spnSubject.selectedItem.toString()
        val timetableDate = etDate.text.toString()
        val timetableTime = etTime.text.toString()
        val timetableEndTime = etEndTime.text.toString()
        val timetableLink = etLink.text.toString()

        if(teacherName.isEmpty() || subjectName.isEmpty() || timetableDate.isEmpty() || timetableTime.isEmpty() || timetableEndTime.isEmpty() || timetableLink.isEmpty()){
            Toast.makeText(this, "Please provide all the values!", Toast.LENGTH_LONG).show()
        } else if(subjectName == "Select subject") {
            Toast.makeText(this, "Please select a subject!", Toast.LENGTH_LONG).show()
        } else {
            val timetableCode = dbRef.push().key!!

            //pass data to variable
            val timetable = TimetableModel(timetableCode, teacherName, subjectName, timetableDate, timetableTime, timetableEndTime, timetableLink, teacherId)

            //add data to database
            dbRef.child(timetableCode).setValue(timetable)
                //success listener
                .addOnCompleteListener{
                    Toast.makeText(this, "Timetable inserted successfully!", Toast.LENGTH_LONG).show()

                    etName.text.clear()
                    spnSubject.setSelection(0)
                    etDate.text.clear()
                    etTime.text.clear()
                    etEndTime.text.clear()
                    etLink.text.clear()
                }
                //failure listener
                .addOnFailureListener{err->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

            val intent = Intent(this, TimetableSetup::class.java)
            startActivity(intent)
        }
    }

    private fun showDatePicker() {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }

                if (selectedDate.before(currentDate)) {
                    // error message for past days
                    Toast.makeText(this, "Please select today or a date in the future!", Toast.LENGTH_SHORT).show()
                } else {
                    // update EditText field with selected date
                    etDate.setText("$year-${month+1}-$dayOfMonth")
                }
            },
            year,
            month,
            day
        )
        // show date picker
        datePicker.show()
    }

    private fun showTimePicker() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // update selected time
                etTime.setText("$hourOfDay:$minute")
            },
            hour,
            minute,
            true
        )
        // show time picker
        timePicker.show()
    }

    private fun showEndTimePicker() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        // check if start time is set
        if (etTime.text.isEmpty()) {
            Toast.makeText(this, "Please set start time first!", Toast.LENGTH_LONG).show()
            return
        } else{
            val timePicker = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    // check if the selected end time is valid
                    val selectedTime = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                    }

                    val startTime = etTime.text.toString().split(":")
                    val hourOfStartTime = startTime[0].toInt()
                    val minuteOfStartTime = startTime[1].toInt()

                    val startTimeCalendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hourOfStartTime)
                        set(Calendar.MINUTE, minuteOfStartTime)
                    }

                    if (selectedTime.before(startTimeCalendar)) {
                        Toast.makeText(this, "Please note that the end time cannot be earlier than start time", Toast.LENGTH_LONG).show()
                    } else {
                        // update EditText field with selected time
                        etEndTime.setText("$hourOfDay:$minute")
                    }
                },
                hour,
                minute,
                true
            )
            // show time picker
            timePicker.show()
        }
    }
}








//package com.example.mymad1.activities
//
//import android.app.DatePickerDialog
//import android.app.TimePickerDialog
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.mymad1.models.TimetableModel
//import com.example.mymad1.R
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import java.util.*
//
//class InsertionTimetable : AppCompatActivity() {
//    //variable initialization
//    private lateinit var etName : EditText
//    private lateinit var etSubject : EditText
//    private lateinit var etDate : EditText
//    private lateinit var etTime : EditText
//    private lateinit var etLink : EditText
//    private lateinit var btnSaveTimetable : Button
//
//    private lateinit var dbRef : DatabaseReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.add_timetable)
//
//        etName = findViewById(R.id.addTeacherName)
//        etSubject = findViewById(R.id.addSubject)
//        etDate = findViewById(R.id.addDate)
//        etTime = findViewById(R.id.addTime)
//        etLink = findViewById(R.id.addLink)
//        btnSaveTimetable = findViewById(R.id.btnSaveTimetable)
//
//        etDate.setOnClickListener {
//            showDatePicker()
//        }
//
//        etTime.setOnClickListener {
//            showTimePicker()
//        }
//
//
//        dbRef = FirebaseDatabase.getInstance().getReference("Timetable")
//
//        btnSaveTimetable.setOnClickListener{
//            saveTimetableData()
//        }
//    }
//
//    private fun saveTimetableData(){
//        //get values
//        val teacherName = etName.text.toString()
//        val subjectName = etSubject.text.toString()
//        val timetableDate = etDate.text.toString()
//        val timetableTime = etTime.text.toString()
//        val timetableLink = etLink.text.toString()
//
//        if(teacherName.isEmpty()){
//            etName.error = "Please enter teacher name"
//        }
//        if(subjectName.isEmpty()){
//            etSubject.error = "Please enter subject name"
//        }
//        if(timetableDate.isEmpty()){
//            etDate.error = "Please enter date"
//        }
//        if(timetableTime.isEmpty()){
//            etTime.error = "Please enter time"
//        }
//        if(timetableLink.isEmpty()){
//            etLink.error = "Please enter link"
//        }
//
//        val timetableCode = dbRef.push().key!!
//
//        //pass data to variable
//        val timetable = TimetableModel(timetableCode, teacherName, subjectName, timetableDate, timetableTime, timetableLink)
//
//        //add data to database
//        dbRef.child(timetableCode).setValue(timetable)
//            //success listener
//            .addOnCompleteListener{
//                Toast.makeText(this, "Timetable inserted successfully!!", Toast.LENGTH_LONG).show()
//
//                etName.text.clear()
//                etSubject.text.clear()
//                etDate.text.clear()
//                etTime.text.clear()
//                etLink.text.clear()
//            }
//            //failure listener
//            .addOnFailureListener{err->
//                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
//            }
//    }
//
//
//    private fun showDatePicker() {
//        val currentDate = Calendar.getInstance()
//        val year = currentDate.get(Calendar.YEAR)
//        val month = currentDate.get(Calendar.MONTH)
//        val day = currentDate.get(Calendar.DAY_OF_MONTH)
//
//        val datePicker = DatePickerDialog(
//            this,
//            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
//                // Update the EditText field with the selected date
//                etDate.setText("$year-${month+1}-$dayOfMonth")
//            },
//            year,
//            month,
//            day
//        )
//
//        // Show the date picker
//        datePicker.show()
//    }
//
//    private fun showTimePicker() {
//        val currentTime = Calendar.getInstance()
//        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
//        val minute = currentTime.get(Calendar.MINUTE)
//
//        val timePicker = TimePickerDialog(
//            this,
//            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
//                // Update the EditText field with the selected time
//                etTime.setText("$hourOfDay:$minute")
//            },
//            hour,
//            minute,
//            true
//        )
//
//        // Show the time picker
//        timePicker.show()
//    }
//
//
//}