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
import com.example.mymad1.models.EmployeeModel
import com.google.firebase.database.FirebaseDatabase
import javax.security.auth.Subject

class StudentEmployeeDetailsActivity : AppCompatActivity(){

    //Assign text views to variables
    private lateinit var tvStdId: TextView
    private lateinit var tvStdName: TextView
    private lateinit var tvStdSubject: TextView
    private lateinit var tvStdMarks: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_student_details)

        //Call functions
        initView()
        setValuesToViews()

    }

    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Student Results").child(id)

        AlertDialog.Builder(this)
            .setTitle("Delete Student Result")
            .setMessage("Are you sure you want to delete this student record?")
            .setPositiveButton("Yes") { _, _ ->
                val mTask = dbRef.removeValue()
                mTask.addOnSuccessListener {
                    Toast.makeText(this, "Student data deleted", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, FetchingActivity ::class.java)
                    finish()
                    startActivity(intent)
                }.addOnFailureListener { error ->
                    Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }


    private fun initView() {
        tvStdId = findViewById(R.id.tvStdId)
        tvStdName = findViewById(R.id.tvStdName)
        tvStdSubject = findViewById(R.id.tvStdSubject)
        tvStdMarks = findViewById(R.id.tvStdMarks)

    }

    private fun setValuesToViews() {

        tvStdId.text = intent.getStringExtra("stdId")
        tvStdName.text = intent.getStringExtra("stdName")
        tvStdSubject.text = intent.getStringExtra("stdSubject")
        tvStdMarks.text = intent.getStringExtra("stdMarks")

    }

    private fun openUpdateDialog(
        stdId: String,
        stdName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etStdName = mDialogView.findViewById<EditText>(R.id.etStdName)
        val etStdSubject = mDialogView.findViewById<EditText>(R.id.etStdSubject)
        val etStdMarks = mDialogView.findViewById<EditText>(R.id.etStdMarks)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        //Set data to edit text
        etStdName.setText(intent.getStringExtra("stdName").toString())
        etStdSubject.setText(intent.getStringExtra("stdSubject").toString())
        etStdMarks.setText(intent.getStringExtra("stdMarks").toString())

        mDialog.setTitle("Updating $stdName Record")     //which user data is updating

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateEmpData(            //recording updated data
                stdId,
                etStdName.text.toString(),
                etStdSubject.text.toString(),
                etStdMarks.text.toString()
            )

            //Update success Toast msg
            Toast.makeText(applicationContext, "Student Data Updated", Toast.LENGTH_LONG).show()

            //setting updated new data to our text views
            tvStdName.text = etStdName.text.toString()
            tvStdSubject.text = etStdSubject.text.toString()
            tvStdMarks.text = etStdMarks.text.toString()

            alertDialog.dismiss()   //clear alert
        }


    }

    private fun updateEmpData(
        id: String,
        name: String,
        subject: String,
        marks: String
    ) {
        //Get database reference by id
        val dbRef = FirebaseDatabase.getInstance().getReference("Student Results").child(id)
        val empInfo = EmployeeModel(id, name, subject, marks)
        dbRef.setValue(empInfo)
    }

}

