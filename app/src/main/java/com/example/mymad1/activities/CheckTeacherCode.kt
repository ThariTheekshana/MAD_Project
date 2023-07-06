package com.example.mymad1.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mymad1.R
import com.example.mymad1.models.Students
import com.example.mymad1.models.TeacherCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class CheckTeacherCode: AppCompatActivity() {

    //Declare btn variables
    private lateinit var btnCodeConfirm: Button
    private lateinit var btnCodeExit: Button

    private lateinit var enterCode :EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.teacher_confirmation_code)

        //Assign btn Ids for variables
        btnCodeConfirm = findViewById(R.id.btn_code_confirm)
        btnCodeExit = findViewById(R.id.btn_code_confirm_exit)

        //Get TecherCode Reference from DB
        val codeRef = FirebaseDatabase.getInstance().reference.child("teachercode")
        val enterCode = findViewById<EditText>(R.id.enterCode)


        //btn on clicklistner to do the job and change activity
        btnCodeConfirm.setOnClickListener {
            val typeCode = enterCode.text.toString()

            codeRef.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(snapshot in snapshot.children){                            //Get reference snapshot of code
                        val dbValue = snapshot.getValue(String::class.java)
                        if(dbValue == typeCode){
                            //If code is correct then change activity
                            val intent = Intent(this@CheckTeacherCode, TeacherSignInActivity::class.java)
                            startActivity(intent)

                            return
                        }else{          //if code invalid display error toast msg
                            Toast.makeText(this@CheckTeacherCode, "Invalid Teacher Code. try again!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

        //exit btn action
        btnCodeExit.setOnClickListener{
            val intent = Intent(this, TeacherStudentLogin::class.java)
            startActivity(intent)
        }
    }}






