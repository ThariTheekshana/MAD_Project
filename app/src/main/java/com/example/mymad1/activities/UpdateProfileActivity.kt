package com.example.mymad1.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R
import com.example.mymad1.models.Students
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*





//This activity will not be used in the project as we are going to edit user inside the ViewProfileActivity

// HABAI MEKA THIYANNA PODDAK... MONA MAGULAK WEIDA DANNE NANE... HEEEEE






class UpdateProfileActivity:AppCompatActivity() {

    private lateinit var editProfileName: TextView
    private lateinit var editProfileEmail: TextView
    private lateinit var editProfileMobileNumber: TextView

    private lateinit var btnUpdateEditProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        editProfileName = findViewById(R.id.editStudentName)
        editProfileEmail = findViewById(R.id.editStudentEmail)
        editProfileMobileNumber = findViewById(R.id.editStudentMobileNumber)

        btnUpdateEditProfile = findViewById(R.id.btnUpdateEditProfile)

        getStudentData()

//        initView()
        setValuesToViews()

        btnUpdateEditProfile.setOnClickListener {
//            val intent = Intent(this, ViewProfileActivity::class.java)  //change activity
//            startActivity(intent)

            openUpdateDialog(
                intent.getStringExtra("userId").toString()
//                intent.getStringExtra("profPhoneNo").toString()
            )
        }

    }

//    private fun initView() {
//        editProfileName = findViewById(R.id.editStudentName)
//        editProfileEmail = findViewById(R.id.editStudentEmail)
//        editProfileMobileNumber = findViewById(R.id.editStudentMobileNumber)
//
//        btnUpdateEditProfile = findViewById(R.id.btnUpdateEditProfile)
//    }

    private fun setValuesToViews(){
        editProfileName.text = intent.getStringExtra("profName")
        editProfileEmail.text = intent.getStringExtra("profEmail")
        editProfileMobileNumber.text = intent.getStringExtra("profPhoneNo")
    }


    private fun getStudentData(){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = FirebaseDatabase.getInstance().getReference("students/$userId")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val student = snapshot.getValue(Students::class.java)
                editProfileName.text = student?.stdName
                editProfileEmail.text = student?.stdEmail
                editProfileMobileNumber.text = student?.stdPhone
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read user data.", error.toException())
            }

        })
    }

    private fun openUpdateDialog(
        userId: String
//        profEmail: String,
//        profPhoneNo: String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.activity_edit_profile, null)

        mDialog.setView(mDialogView)

        val etProfName = mDialogView.findViewById<EditText>(R.id.editStudentName)
        val etProfEmail = mDialogView.findViewById<EditText>(R.id.editStudentEmail)
        val etProfPhoneNo = mDialogView.findViewById<EditText>(R.id.editStudentMobileNumber)

        val btnUpdate = mDialogView.findViewById<Button>(R.id.btnUpdateEditProfile)

        etProfName.setText(intent.getStringExtra("profName").toString())
        etProfEmail.setText(intent.getStringExtra("profEmail").toString())
        etProfPhoneNo.setText(intent.getStringExtra("profPhoneNo").toString())

        mDialog.setTitle("Updating $userId record!")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdate.setOnClickListener{
            updateProfileData(
                userId,
                etProfName.text.toString(),
                etProfEmail.text.toString(),
                etProfPhoneNo.text.toString(),
            )

            Toast.makeText(applicationContext, "Profile Updated", Toast.LENGTH_LONG).show()

            //set updated data to textviews
            editProfileName.text = etProfName.text.toString()
            editProfileEmail.text = etProfEmail.text.toString()
            editProfileMobileNumber.text = etProfPhoneNo.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateProfileData(
        userId: String,
        name: String,
        email: String,
        phoneNo: String,
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("students").child(userId)
        val profInfo = Students(name, email, phoneNo, userId)
        dbRef.setValue(profInfo)
    }





//    private fun UpdateStudentProfileData(){
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val userRef = FirebaseDatabase.getInstance().getReference("students/$userId")
//        updateProfileName = findViewById<EditText>(R.id.editStudentName)
//        updateProfileEmail = findViewById<EditText>(R.id.editStudentEmail)
//        updateProfileMobileNumber = findViewById<EditText>(R.id.editStudentMobileNumber)
//    }
//}
///****************************************************************
//    private fun updateStudentData(){
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val userRef = FirebaseDatabase.getInstance().getReference("students/$userId")
//        val database = FirebaseDatabase.getInstance()
//        val updateProfileName = findViewById<EditText>(R.id.editStudentName)
//        val updateProfileEmail = findViewById<EditText>(R.id.editStudentEmail)
//        val updateProfileMobileNumber = findViewById<EditText>(R.id.editStudentMobileNumber)
//
//        updateProfileName.setOnEditorActionListener {_,actionId,_->
//            if(actionId == EditorInfo.IME_ACTION_DONE){
//                updateData(updateProfileName.text.toString(), database, userId)
//                true
//            }else{
//                false
//            }
//
//        }
//
//        updateProfileEmail.setOnEditorActionListener {_,actionId,_->
//            if(actionId == EditorInfo.IME_ACTION_DONE){
//                updateData(updateProfileEmail.text.toString(), database, userId)
//                true
//            }else{
//                false
//            }
//
//        }
//
//        updateProfileMobileNumber.setOnEditorActionListener {_,actionId,_->
//            if(actionId == EditorInfo.IME_ACTION_DONE){
//                updateData(updateProfileMobileNumber.text.toString(), database, userId)
//                true
//            }else{
//                false
//            }
//
//        }
//
//    }

//    private fun updateData(newData: String, database: FirebaseDatabase, uid: String?) {
//
//        val updateProfileName = findViewById<EditText>(R.id.editStudentName)
//        val updateProfileEmail = findViewById<EditText>(R.id.editStudentEmail)
//        val updateProfileMobileNumber = findViewById<EditText>(R.id.editStudentMobileNumber)
//
//        // Get a reference to the user's data in the database
//        val userRef = database.getReference("students/$uid")
//
//        // Update the data in the database
//        userRef.child("stdName").setValue(newData)
//        // Update the text view with the new data
//        updateProfileName.text = newData
//
//        userRef.child("stdEmail").setValue(newData)
//        // Update the text view with the new data
//        updateProfileEmail.text = newData
//
//        userRef.child("stdPhone").setValue(newData)
//        // Update the text view with the new data
//        updateProfileMobileNumber.text = newData
//    }
//************************************************************************************************

}