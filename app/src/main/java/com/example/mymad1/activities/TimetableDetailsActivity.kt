package com.example.mymad1.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R
import com.example.mymad1.models.Teachers
import com.example.mymad1.models.TimetableModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class TimetableDetailsActivity : AppCompatActivity(){
    private lateinit var tvCode: TextView
    private lateinit var tvName : TextView
    private lateinit var tvSubject : TextView
    private lateinit var tvDate : TextView
    private lateinit var tvTime : TextView
    private lateinit var tvEndTime : TextView
    private lateinit var tvLink : TextView
    private lateinit var btnUpdateTimetable : Button
    private lateinit var btnDeleteTimetable : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_timetable)

        initView()
        setValuesToViews()

        val currentUser = FirebaseAuth.getInstance().currentUser
        val teacherUser = FirebaseDatabase.getInstance().getReference("teachers")
        val timetableOwner = FirebaseDatabase.getInstance().getReference("Timetable")

        teacherUser
            .orderByChild("uid")
            .equalTo(currentUser?.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        //user is a teacher
                        val teacher = snapshot.children.firstOrNull()?.getValue(Teachers::class.java)
                        val teacherSubject = teacher?.subject

                        timetableOwner
                            .orderByChild("teacherId")
                            .equalTo(currentUser?.uid)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        val timetable = snapshot.children.firstOrNull()?.getValue(TimetableModel::class.java)
                                        val timetableSubject = timetable?.subjectName

                                        if (teacherSubject == timetableSubject) {
                                            //user owns timetable
                                            //access to update/delete timetable
                                            btnUpdateTimetable.setOnClickListener{
                                                openUpdateDialog(
                                                    intent.getStringExtra("timetableCode").toString(),
                                                    intent.getStringExtra("teacherId").toString()
                                                )
                                            }

                                            btnDeleteTimetable.setOnClickListener{
                                                deleteRecord(
                                                    intent.getStringExtra("timetableCode").toString()
                                                )
                                            }
                                        } else {
                                            //user does not own timetable
                                            //no access to update/delete timetable
                                            btnUpdateTimetable.setOnClickListener{
                                                btnUpdateTimetable.isEnabled = false
                                                Toast.makeText(this@TimetableDetailsActivity, "Kindly note that you cannot edit another user's timetable!", Toast.LENGTH_LONG).show()
                                            }

                                            btnDeleteTimetable.setOnClickListener{
                                                btnDeleteTimetable.isEnabled = false
                                                Toast.makeText(this@TimetableDetailsActivity, "Kindly note that you cannot delete another user's timetable!", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    } else {
                                        //user does not own timetable
                                        //no access to update/delete timetable
                                        btnUpdateTimetable.setOnClickListener{
                                            btnUpdateTimetable.isEnabled = false
                                            Toast.makeText(this@TimetableDetailsActivity, "Kindly note that you cannot edit another user's timetable!", Toast.LENGTH_LONG).show()
                                        }

                                        btnDeleteTimetable.setOnClickListener{
                                            btnDeleteTimetable.isEnabled = false
                                            Toast.makeText(this@TimetableDetailsActivity, "Kindly note that you cannot delete another user's timetable!", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@TimetableDetailsActivity, "Error ${error.message}", Toast.LENGTH_LONG).show()
                                }
                            })
                    } else {
                        //user is a student
                        btnUpdateTimetable.visibility = View.GONE
                        btnDeleteTimetable.visibility = View.GONE
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@TimetableDetailsActivity, "Error ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun deleteRecord(
        code: String
    ){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete timetable?")
            .setTitle("Delete Timetable")
            .setPositiveButton("Yes") { dialog, which ->
                val dbRef = FirebaseDatabase.getInstance().getReference("Timetable").child(code)
                val mTask = dbRef.removeValue()

                mTask.addOnSuccessListener {
                    Toast.makeText(this, "Timetable data deleted!!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, TimetableSetup::class.java)
                    finish()
                    startActivity(intent)
                }.addOnFailureListener {
                        error ->
                    Toast.makeText(this, "Deleting Error ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun initView() {
        tvCode = findViewById(R.id.tvTimetableCode)
        tvName = findViewById(R.id.tvTeacherName)
        tvSubject = findViewById(R.id.tvSubjectName)
        tvDate = findViewById(R.id.tvTimetableDate)
        tvTime = findViewById(R.id.tvTimetableTime)
        tvEndTime = findViewById(R.id.tvTimetableEndTime)
        tvLink = findViewById(R.id.tvTimetableLink)

        btnUpdateTimetable = findViewById(R.id.btnUpdate)
        btnDeleteTimetable = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews(){
        tvCode.text = intent.getStringExtra("timetableCode")
        tvName.text = intent.getStringExtra("teacherName")
        tvSubject.text = intent.getStringExtra("subjectName")
        tvDate.text = intent.getStringExtra("timetableDate")
        tvTime.text = intent.getStringExtra("timetableTime")
        tvEndTime.text = intent.getStringExtra("timetableEndTime")
        tvLink.text = intent.getStringExtra("timetableLink")
    }

    private fun openUpdateDialog(
        timetableCode: String,
        teacherId: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_timetable, null)

        mDialog.setView(mDialogView)

        val etTeacherName = mDialogView.findViewById<EditText>(R.id.etTeacherName)
        val spnSubject = mDialogView.findViewById<Spinner>(R.id.editSubject)
        val etDate = mDialogView.findViewById<EditText>(R.id.etDate)
        val etTime= mDialogView.findViewById<EditText>(R.id.etTime)
        val etEndTime= mDialogView.findViewById<EditText>(R.id.etEndTime)
        val etLink= mDialogView.findViewById<EditText>(R.id.etLink)

        val btnUpdate = mDialogView.findViewById<Button>(R.id.btnUpdate)

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

        //set values
        etTeacherName.setText(intent.getStringExtra("teacherName").toString())
        etDate.setText(intent.getStringExtra("timetableDate").toString())
        etTime.setText(intent.getStringExtra("timetableTime").toString())
        etEndTime.setText(intent.getStringExtra("timetableEndTime").toString())
        etLink.setText(intent.getStringExtra("timetableLink").toString())

        //set spinner value
        val subjectName = intent.getStringExtra("subjectName").toString()
        val subAdapter = spnSubject.adapter
        val index = adapter.getPosition(subjectName)
        if (index != -1) {
            spnSubject.setSelection(index)
        }

        etDate.setOnClickListener {
            showDatePicker(etDate)
        }

        etTime.setOnClickListener {
            showTimePicker(etTime)
        }

        etEndTime.setOnClickListener {
            showEndTimePicker(etEndTime, etTime)
        }

        mDialog.setTitle("Updating $teacherId record!!")

        val alertDialog = mDialog.create()
        alertDialog.show()

        //get current user
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        btnUpdate.setOnClickListener{
            updateTimetableData(
                timetableCode,
                etTeacherName.text.toString(),
                spnSubject.selectedItem.toString(),
                etDate.text.toString(),
                etTime.text.toString(),
                etEndTime.text.toString(),
                etLink.text.toString(),
                userId.toString()
            )

            Toast.makeText(applicationContext, "Timetable Data Updated", Toast.LENGTH_LONG).show()

            //set updated data to textviews
            tvName.text = etTeacherName.text.toString()
            tvSubject.text = spnSubject.selectedItem.toString()
            tvDate.text = etDate.text.toString()
            tvTime.text = etTime.text.toString()
            tvEndTime.text = etEndTime.text.toString()
            tvLink.text = etLink.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateTimetableData(
        code:String,
        name:String,
        subject:String,
        date:String,
        time:String,
        endTime:String,
        link:String,
        teacherId:String
    ){
        if(name.isEmpty() || subject.isEmpty() || date.isEmpty() || time.isEmpty() || endTime.isEmpty() || link.isEmpty()){
            Toast.makeText(this, "Please provide all the values!", Toast.LENGTH_LONG).show()
        } else if(subject == "Select subject") {
            Toast.makeText(this, "Please select a subject!", Toast.LENGTH_LONG).show()
        } else {
            val dbRef = FirebaseDatabase.getInstance().getReference("Timetable").child(code)
            val timetableInfo = TimetableModel(code, name, subject, date, time, endTime, link, teacherId)
            dbRef.setValue(timetableInfo)
        }
    }


    private fun showDatePicker(etDate: EditText) {
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

    private fun showTimePicker(etTime: EditText) {
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

    private fun showEndTimePicker(etEndTime: EditText, etTime: EditText) {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        //check if start time is set
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
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import com.example.mymad1.R
//import com.example.mymad1.models.TimetableModel
//import com.google.firebase.database.FirebaseDatabase
//import java.util.*
//
//class TimetableDetailsActivity : AppCompatActivity(){
//    private lateinit var tvCode: TextView
//    private lateinit var tvName : TextView
//    private lateinit var tvSubject : TextView
//    private lateinit var tvDate : TextView
//    private lateinit var tvTime : TextView
//    private lateinit var tvLink : TextView
//    private lateinit var btnUpdateTimetable : Button
//    private lateinit var btnDeleteTimetable : Button
//
//    private lateinit var etDate : EditText
//    private lateinit var etTime : EditText
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.view_timetable)
//
//        initView()
//        setValuesToViews()
//
//        btnUpdateTimetable.setOnClickListener{
//            openUpdateDialog(
//                intent.getStringExtra("timetableCode").toString(),
//                intent.getStringExtra("teacherName").toString()
//            )
//        }
//
//        btnDeleteTimetable.setOnClickListener{
//            deleteRecord(
//                intent.getStringExtra("timetableCode").toString()
//            )
//        }
//    }
//
//    private fun deleteRecord(
//        code: String
//    ){
//        val dbRef = FirebaseDatabase.getInstance().getReference("Timetable").child(code)
//        val mTask = dbRef.removeValue()
//
//        mTask.addOnSuccessListener {
//            Toast.makeText(this, "Timetable data deleted!!", Toast.LENGTH_LONG).show()
//            val intent = Intent(this, FetchingTimetable::class.java)
//            finish()
//            startActivity(intent)
//        }.addOnFailureListener {
//            error ->
//                Toast.makeText(this, "Deleting Error ${error.message}", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    private fun initView() {
//        tvCode = findViewById(R.id.tvTimetableCode)
//        tvName = findViewById(R.id.tvTeacherName)
//        tvSubject = findViewById(R.id.tvSubjectName)
//        tvDate = findViewById(R.id.tvTimetableDate)
//        tvTime = findViewById(R.id.tvTimetableTime)
//        tvLink = findViewById(R.id.tvTimetableLink)
//
//        btnUpdateTimetable = findViewById(R.id.btnUpdate)
//        btnDeleteTimetable = findViewById(R.id.btnDelete)
//    }
//
//    private fun setValuesToViews(){
//        tvCode.text = intent.getStringExtra("timetableCode")
//        tvName.text = intent.getStringExtra("teacherName")
//        tvSubject.text = intent.getStringExtra("subjectName")
//        tvDate.text = intent.getStringExtra("timetableDate")
//        tvTime.text = intent.getStringExtra("timetableTime")
//        tvLink.text = intent.getStringExtra("timetableLink")
//    }
//
//    private fun openUpdateDialog(
//        timetableCode: String,
//        teacherName: String
//    ){
//        val mDialog = AlertDialog.Builder(this)
//        val inflater = layoutInflater
//        val mDialogView = inflater.inflate(R.layout.update_timetable, null)
//
//        mDialog.setView(mDialogView)
//
//        val etTeacherName = mDialogView.findViewById<EditText>(R.id.etTeacherName)
//        val etSubjectName = mDialogView.findViewById<EditText>(R.id.etSubject)
//        val etDate = mDialogView.findViewById<EditText>(R.id.etDate)
//        val etTime= mDialogView.findViewById<EditText>(R.id.etTime)
//        val etLink= mDialogView.findViewById<EditText>(R.id.etLink)
//
//        val btnUpdate = mDialogView.findViewById<Button>(R.id.btnUpdate)
//
//        etDate.setOnClickListener {
//            showDatePicker()
//        }
//
//        etTime.setOnClickListener {
//            showTimePicker()
//        }
//
//        etTeacherName.setText(intent.getStringExtra("teacherName").toString())
//        etSubjectName.setText(intent.getStringExtra("subjectName").toString())
//        etDate.setText(intent.getStringExtra("timetableDate").toString())
//        etTime.setText(intent.getStringExtra("timetableTime").toString())
//        etLink.setText(intent.getStringExtra("timetableLink").toString())
//
//        mDialog.setTitle("Updating $teacherName record!!")
//
//        val alertDialog = mDialog.create()
//        alertDialog.show()
//
//        btnUpdate.setOnClickListener{
//            updateTimetableData(
//                timetableCode,
//                etTeacherName.text.toString(),
//                etSubjectName.text.toString(),
//                etDate.text.toString(),
//                etTime.text.toString(),
//                etLink.text.toString(),
//            )
//
//            Toast.makeText(applicationContext, "Employee Data Updated", Toast.LENGTH_LONG).show()
//
//            //set updated data to textviews
//            tvName.text = etTeacherName.text.toString()
//            tvSubject.text = etSubjectName.text.toString()
//            tvDate.text = etDate.text.toString()
//            tvTime.text = etTime.text.toString()
//            tvLink.text = etLink.text.toString()
//
//            alertDialog.dismiss()
//        }
//    }
//
//    private fun updateTimetableData(
//        code:String,
//        name:String,
//        subject:String,
//        date:String,
//        time:String,
//        link:String
//    ){
//        val dbRef = FirebaseDatabase.getInstance().getReference("Timetable").child(code)
//        val timetableInfo = TimetableModel(code, name, subject, date, time, link)
//        dbRef.setValue(timetableInfo)
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
//}