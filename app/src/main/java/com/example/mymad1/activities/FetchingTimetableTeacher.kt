package com.example.mymad1.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymad1.R
import com.example.mymad1.adapters.TeacherInfoAdapter
import com.example.mymad1.adapters.TimetableAdapter
import com.example.mymad1.models.Teachers
import com.example.mymad1.models.TimetableModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FetchingTimetableTeacher : AppCompatActivity() {
    private lateinit var teacherRecyclerView : RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var teacherList : ArrayList<TimetableModel>

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get condition variable from the intent
        val condition = intent.getIntExtra("condition", 0)

        //select layout according to the condition
        val layId = when(condition){
            1 -> R.layout.maths_teacher_fetching
            2 -> R.layout.bio_teacher_fetching
            3 -> R.layout.chem_teacher_fetching
            4 -> R.layout.phy_teacher_fetching
            else -> R.layout.activity_home
        }

        //set the selected layout
        setContentView(layId)

        teacherRecyclerView = findViewById(R.id.rvTeacherName)
        teacherRecyclerView.layoutManager = LinearLayoutManager(this)
        teacherRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        teacherList = arrayListOf<TimetableModel>()

        getTimetableTeacherData(condition)
    }

    private fun getTimetableTeacherData(condition: Int){
        teacherRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        val userRef = FirebaseAuth.getInstance().currentUser
        val studentUser = FirebaseDatabase.getInstance().getReference("students")

        dbRef = FirebaseDatabase.getInstance().getReference("Timetable")

        //filter teachers by subject
        val query = when (condition) {
            1 -> dbRef.orderByChild("subjectName").equalTo("Combined Maths")
            2 -> dbRef.orderByChild("subjectName").equalTo("Biology")
            3 -> dbRef.orderByChild("subjectName").equalTo("Chemistry")
            4 -> dbRef.orderByChild("subjectName").equalTo("Physics")
            else -> dbRef
        }

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teacherList.clear()
                if(snapshot.exists()){
                    for(teacherSnap in snapshot.children){
                        val teacherData = teacherSnap.getValue(TimetableModel::class.java)
                        teacherList.add(teacherData!!)
                    }

                    val mAdapter = TimetableAdapter(teacherList)
                    teacherRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : TimetableAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchingTimetableTeacher, TimetableDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("timetableCode", teacherList[position].subjectName)
                            intent.putExtra("teacherName", teacherList[position].teacherName)
                            intent.putExtra("subjectName", teacherList[position].subjectName)
                            intent.putExtra("timetableDate", teacherList[position].timetableDate)
                            intent.putExtra("timetableTime", teacherList[position].timetableTime)
                            intent.putExtra("timetableEndTime", teacherList[position].timetableEndTime)
                            intent.putExtra("timetableLink", teacherList[position].timetableLink)

                            startActivity(intent)
                        }
                    })
                    teacherRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}