package com.example.mymad1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mymad1.activities.TimetableSetup
import com.example.mymad1.activities.TeacherDetailsActivity
import com.example.mymad1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity(){


    private lateinit var btnHomeTimetable: Button
    private lateinit var mathsBtn: Button
    private lateinit var bioBtn: Button
    private lateinit var chemBtn: Button
    private lateinit var phyBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnHomeTimetable = findViewById(R.id.btnTimetable)
        mathsBtn = findViewById(R.id.btnMaths)
        bioBtn = findViewById(R.id.btnBio)
        chemBtn = findViewById(R.id.btnChem)
        phyBtn = findViewById(R.id.btnPhy)

        val userIcon = findViewById<ImageView>(R.id.userIcon)

        //user profile navigation
        userIcon.setOnClickListener{
            //get current user (student/ teacher)
            val currentUser = FirebaseAuth.getInstance().currentUser
            val dbTeacherUser = FirebaseDatabase.getInstance().getReference("teachers")

            //check current user
            dbTeacherUser
                .orderByChild("uid")
                .equalTo(currentUser?.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    //check student or teacher
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            //user is a teacher
                            val intent = Intent(this@HomeActivity, TeacherViewProfileActivity::class.java)
                            startActivity(intent)
                        } else {
                            //user is a student
                            val intent = Intent(this@HomeActivity, ViewProfileActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@HomeActivity, "Error ${error.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }

        //timetable navigation
        btnHomeTimetable.setOnClickListener{
            //get current user (student/ teacher)
            val currentUser = FirebaseAuth.getInstance().currentUser
            val dbTeacherUser = FirebaseDatabase.getInstance().getReference("teachers")

            dbTeacherUser
                .orderByChild("uid")
                .equalTo(currentUser?.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            //user is a teacher
                            val intent = Intent(this@HomeActivity, TimetableSetup::class.java)
                            startActivity(intent)
                        } else {
                            //user is a student
                            val intent = Intent(this@HomeActivity, SelectSubjectActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@HomeActivity, "Error ${error.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }

        //subject teacher details navigation
        //combined maths
        mathsBtn.setOnClickListener{
            val condition : Int = 1
            val intent = Intent(this, FetchingTeacherInfo::class.java)
            intent.putExtra("condition", condition)
            startActivity(intent)
        }
        //biology
        bioBtn.setOnClickListener{
            val condition : Int = 2
            val intent = Intent(this, FetchingTeacherInfo::class.java)
            intent.putExtra("condition", condition)
            startActivity(intent)
        }
        //chemistry
        chemBtn.setOnClickListener{
            val condition : Int = 3
            val intent = Intent(this, FetchingTeacherInfo::class.java)
            intent.putExtra("condition", condition)
            startActivity(intent)
        }
        //physics
        phyBtn.setOnClickListener{
            val condition : Int = 4
            val intent = Intent(this, FetchingTeacherInfo::class.java)
            intent.putExtra("condition", condition)
            startActivity(intent)
        }
    }
}