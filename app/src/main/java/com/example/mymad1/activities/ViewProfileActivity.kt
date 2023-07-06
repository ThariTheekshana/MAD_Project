package com.example.mymad1.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R
import com.example.mymad1.models.Students
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewProfileActivity: AppCompatActivity() {

    //Assign text views to variables
    private lateinit var viewProfileName: TextView
    private lateinit var viewProfileEmail: TextView
    private lateinit var viewProfileMobileNumber: TextView
    private  lateinit var viewProfileHeaderEmail : TextView
    private  lateinit var viewProfileHeaderName : TextView

    //Assign button to variables
    private lateinit var btnUpdateProfile : Button
    private lateinit var btnDeleteProfile : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_profile)

        //set field values for variables
        viewProfileName = findViewById(R.id.viewProfileName)
        viewProfileEmail = findViewById(R.id.viewProfileEmail)
        viewProfileMobileNumber = findViewById(R.id.viewProfileMobileNumber)
        viewProfileHeaderEmail = findViewById(R.id.viewProfileHeaderEmail)
        viewProfileHeaderName = findViewById(R.id.viewProfileHeaderName)

        //Assign btn IDs to variables
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)
        btnDeleteProfile = findViewById(R.id.btnDeleteProfile)

        //ImageViews as buttons
        val homeIcon = findViewById<ImageView>(R.id.ivHome)
        val timetableIcon = findViewById<ImageView>(R.id.ivTimetable)
        val resultsIcon = findViewById<ImageView>(R.id.ivResults)

        //Call student data
        getStudentData()


        //Onclick listners for image views
        homeIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)  //change activity
            startActivity(intent)
        }

        timetableIcon.setOnClickListener{
            val intent = Intent(this, SelectSubjectActivity::class.java)  //change activity
            startActivity(intent)
        }

        resultsIcon.setOnClickListener {
            val intent = Intent(this, StudentMainActivity::class.java)  //change activity
            startActivity(intent)
        }

        //When update button clicked
        btnUpdateProfile.setOnClickListener {

            //Get instance by user id
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val userRef = FirebaseDatabase.getInstance().getReference("students/$userId")

            userRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    //Pass Updating data to database students
                    val student = snapshot.getValue(Students::class.java)
                    openUpdateDialog(student?.uid.toString(), student?.stdName.toString(), student?.stdEmail.toString(), student?.stdPhone.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Failed to read user data.", error.toException())
                }
            })
        }

        //Delete btn action
        btnDeleteProfile.setOnClickListener {
            val intent = Intent(this, DeleteProfileActivity::class.java)  //change activity
            startActivity(intent)
        }


    }

    //Function to get student data
    private fun getStudentData(){

        //Get instance from database
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = FirebaseDatabase.getInstance().getReference("students/$userId")

        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //Get data from database to the relevant
                val student = snapshot.getValue(Students::class.java)
                viewProfileName.text = student?.stdName
                viewProfileEmail.text = student?.stdEmail
                viewProfileMobileNumber.text = student?.stdPhone
                viewProfileHeaderName.text = student?.stdName
                viewProfileHeaderEmail.text = student?.stdEmail
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read user data.", error.toException())
            }

        })
    }

    //Open update dialog pop up
    private fun openUpdateDialog(
        userId: String,
        profName: String,
        profEmail: String,
        profPhoneNo: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.activity_edit_profile, null)

        mDialog.setView(mDialogView)

        //Assign Edit text fields to variables
        val etProfName = mDialogView.findViewById<EditText>(R.id.editStudentName)
        val etProfEmail = mDialogView.findViewById<EditText>(R.id.editStudentEmail)
        val etProfPhoneNo = mDialogView.findViewById<EditText>(R.id.editStudentMobileNumber)

        etProfName.setText(profName)
        etProfEmail.setText(profEmail)
        etProfPhoneNo.setText(profPhoneNo)

        mDialog.setTitle("Updating $userId record!")

        val alertDialog = mDialog.create()
        alertDialog.show()

        val btnUpdate = mDialogView.findViewById<Button>(R.id.btnUpdateEditProfile)

        btnUpdate.setOnClickListener{
            updateProfileData(
                userId,
                etProfName.text.toString(),
                etProfEmail.text.toString(),
                etProfPhoneNo.text.toString(),
            )

            //set updated data to text
            viewProfileName.text = etProfName.text.toString()
            viewProfileEmail.text = etProfEmail.text.toString()
            viewProfileMobileNumber.text = etProfPhoneNo.text.toString()

            alertDialog.dismiss()

            val intent = Intent(this, ViewProfileActivity::class.java)  //change activity
            startActivity(intent)
        }
    }

    private fun updateProfileData(
        userId: String,
        name: String,
        email: String,
        phoneNo: String,
    ){
        //get instance from students database collection by id
        val dbRef = FirebaseDatabase.getInstance().getReference("students").child(userId)
        val profInfo = Students(name, email, phoneNo, userId)
        dbRef.setValue(profInfo)

        //Identify current user
        val user = FirebaseAuth.getInstance().currentUser
        user?.updateEmail(email)?.addOnCompleteListener { task->
            if(task.isSuccessful){
                //Display toast msg if success
                Toast.makeText(applicationContext, "Profile Updated", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(applicationContext, "Profile Not Updated", Toast.LENGTH_LONG).show()
            }
        }
    }
}
