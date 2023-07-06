package com.example.mymad1.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R
import com.example.mymad1.models.Teachers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TeacherViewProfileActivity: AppCompatActivity() {


    private lateinit var viewTProfileName: TextView
    private lateinit var viewTProfileEmail: TextView
    private lateinit var viewTProfileMobileNumber: TextView
    private  lateinit var viewTProfileHeaderEmail : TextView
    private  lateinit var viewTProfileHeaderName : TextView

    private lateinit var btnTUpdateProfile : Button
    private lateinit var btnTDeleteProfile : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teacher_view_profile)

        viewTProfileName = findViewById(R.id.viewTProfileName)
        viewTProfileEmail = findViewById(R.id.viewTProfileEmail)
        viewTProfileMobileNumber = findViewById(R.id.viewTProfileMobileNumber)
        viewTProfileHeaderEmail = findViewById(R.id.viewTProfileHeaderEmail)
        viewTProfileHeaderName = findViewById(R.id.viewTProfileHeaderName)

        btnTUpdateProfile = findViewById(R.id.btnTUpdateProfile)
        btnTDeleteProfile = findViewById(R.id.btnTDeleteProfile)

        //ImageViews as buttons
        val homeIcon = findViewById<ImageView>(R.id.ivHome)
        val timetableIcon = findViewById<ImageView>(R.id.ivTimetable)
        val resultsIcon = findViewById<ImageView>(R.id.ivResults)

        getTeacherData()

        //Onclick listners for image views
        homeIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)  //change activity
            startActivity(intent)
        }

        timetableIcon.setOnClickListener{
            val intent = Intent(this, TimetableSetup::class.java)  //change activity
            startActivity(intent)
        }

        resultsIcon.setOnClickListener {
            val intent = Intent(this, ShashikaMainActivity::class.java)  //change activity
            startActivity(intent)
        }









        btnTUpdateProfile.setOnClickListener {
//            val intent = Intent(this, UpdateProfileActivity::class.java)  //change activity
//            startActivity(intent)

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val userRef = FirebaseDatabase.getInstance().getReference("teachers/$userId")

            userRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val teacher = snapshot.getValue(Teachers::class.java)
                    openUpdateDialog(teacher?.uid.toString(), teacher?.name.toString(), teacher?.email.toString(), teacher?.phone.toString())
                }

                override fun onCancelled(error: DatabaseError) {
//                    Log.w(TAG, "Failed to read user data.", error.toException())
                }
            })
        }

        btnTDeleteProfile.setOnClickListener {
            val intent = Intent(this, TeacherDeleteProfileActivity::class.java)  //change activity
            startActivity(intent)
        }


    }

    private fun getTeacherData(){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        Log.w("Failed to catch user$userId","$userId")
        val tRef = FirebaseDatabase.getInstance().getReference("teachers/$userId")

        tRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val teacher = snapshot.getValue(Teachers::class.java)
                viewTProfileName.text = teacher?.name
                viewTProfileEmail.text = teacher?.email
                viewTProfileMobileNumber.text = teacher?.phone
                viewTProfileHeaderName.text = teacher?.name
                viewTProfileHeaderEmail.text = teacher?.email
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read user data.", error.toException())
            }

        })
    }

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



            //set updated data to textviews
            viewTProfileName.text = etProfName.text.toString()
            viewTProfileEmail.text = etProfEmail.text.toString()
            viewTProfileMobileNumber.text = etProfPhoneNo.text.toString()

            alertDialog.dismiss()

            val intent = Intent(this, TeacherViewProfileActivity::class.java)  //change activity
            startActivity(intent)
        }
    }

    private fun updateProfileData(
        userId: String,
        name: String,
        email: String,
        phoneNo: String,
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("teachers").child(userId)
        val profInfo = Teachers(name, email, phoneNo, userId)
        dbRef.setValue(profInfo)

        val user = FirebaseAuth.getInstance().currentUser
        user?.updateEmail(email)?.addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(applicationContext, "Profile Updated", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(applicationContext, "Profile Not Updated", Toast.LENGTH_LONG).show()
            }
        }
    }
}



//package com.example.mymad1.activities
//
//import android.content.ContentValues.TAG
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.example.mymad1.R
//import com.example.mymad1.models.Students
//import com.example.mymad1.models.Teachers
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.*
//
//class TeacherViewProfileActivity: AppCompatActivity() {
//
//
//    private lateinit var viewProfileName: TextView
//    private lateinit var viewProfileEmail: TextView
//    private lateinit var viewProfileMobileNumber: TextView
//    private  lateinit var viewProfileHeaderEmail : TextView
//    private  lateinit var viewProfileHeaderName : TextView
//
//    private lateinit var btnUpdateProfile : Button
//    private lateinit var btnDeleteProfile : Button
//
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.view_profile)
//
//        viewProfileName = findViewById(R.id.viewProfileName)
//        viewProfileEmail = findViewById(R.id.viewProfileEmail)
//        viewProfileMobileNumber = findViewById(R.id.viewProfileMobileNumber)
//        viewProfileHeaderEmail = findViewById(R.id.viewProfileHeaderEmail)
//        viewProfileHeaderName = findViewById(R.id.viewProfileHeaderName)
//
//
//        getStudentData()
//
//        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)
//        btnDeleteProfile = findViewById(R.id.btnDeleteProfile)
//
//        btnUpdateProfile.setOnClickListener {
//            val intent = Intent(this, UpdateProfileActivity::class.java)  //change activity
//            startActivity(intent)
//        }
//
//        btnDeleteProfile.setOnClickListener {
//            val intent = Intent(this, DeleteProfileActivity::class.java)  //change activity
//            startActivity(intent)
//        }
//
//
//    }
//
//    private fun getStudentData(){
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val userRef = FirebaseDatabase.getInstance().getReference("teachers/$userId")
//
//        userRef.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val teacher = snapshot.getValue(Teachers::class.java)
//                viewProfileName.text = teacher?.Name
//                viewProfileEmail.text = teacher?.Email
//                viewProfileMobileNumber.text = teacher?.Phone
//                viewProfileHeaderName.text = teacher?.Name
//                viewProfileHeaderEmail.text = teacher?.Email
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w(TAG, "Failed to read user data.", error.toException())
//            }
//
//        })
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////import android.content.Intent
////import androidx.appcompat.app.AppCompatActivity
////import android.os.Bundle
////import android.widget.Button
////import android.widget.Toast
////import androidx.appcompat.app.AlertDialog
////import com.example.mymad1.R
////import com.example.mymad1.activities.DeleteProfileActivity
////import com.example.mymad1.activities.UpdateProfileActivity
////import com.google.firebase.auth.FirebaseAuth
////import com.google.firebase.database.DatabaseReference
////import com.google.firebase.database.FirebaseDatabase
////
////
////class ViewProfileActivity : AppCompatActivity() {
////
////    //private lateinit var btnDeleteProfile: Button
////    private lateinit var btnUpdateProfile: Button
////    private lateinit var auth : FirebaseAuth
////
////
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.view_profile)
////
////        //Check User exists or not
////        auth =FirebaseAuth.getInstance()
////
////        if(auth.currentUser == null){
////            val intent = Intent(this, SignInActivity::class.java)
////            startActivity(intent)
////        }
////
////        val firebase : DatabaseReference = FirebaseDatabase.getInstance().getReference()
////
////        //btnDeleteProfile = findViewById(R.id.btnDeleteProfile)
////        btnUpdateProfile = findViewById(R.id.btnUpdateProfile)
////
//////        btnDeleteProfile.setOnClickListener {
//////            val intent = Intent(this, DeleteProfileActivity::class.java)  //change activity
//////            startActivity(intent)
//////        }
////
////        btnUpdateProfile.setOnClickListener {
////            val intent = Intent(this, UpdateProfileActivity::class.java)
////            startActivity(intent)
////        }
////
////
////
////        val myButton = findViewById<Button>(R.id.btnDeleteProfile)
////
////// Set an onClickListener on the button
////        myButton.setOnClickListener {
////            // Call your function here
////            showDeleteAlert()
////        }
////
////    }
////
////
////
////    private fun showDeleteAlert(){
////        val builder = AlertDialog.Builder(this)
////        builder.setMessage("Are you sure you want to delete profile?")
////            .setTitle("Delete Profile")
////            .setPositiveButton("Yes") { dialog, which ->
////
////                val database = FirebaseDatabase.getInstance()
////                val nodeRef = database.getReference("Profile")
////
////                // Use the removeValue() method to delete the node from the database
////                nodeRef.removeValue()
////                    .addOnSuccessListener {
////                        // Display a success message to the user
////                        Toast.makeText(this, "Profile deleted successfully", Toast.LENGTH_SHORT).show()
////                    }
////                    .addOnFailureListener { error ->
////                        // Display an error message to the user
////                        Toast.makeText(this, "Error deleting Profile: ${error.message}", Toast.LENGTH_SHORT).show()
////                    }
////            }
////            .setNegativeButton("No") { dialog, which ->
////                // Do nothing
////                dialog.cancel()
////            }
////
////        val dialog = builder.create()
////        dialog.show()
////    }
////
////
////
////}
//
