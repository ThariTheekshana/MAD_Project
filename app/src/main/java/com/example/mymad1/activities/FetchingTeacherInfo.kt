package com.example.mymad1.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymad1.R
import com.example.mymad1.activities.TeacherDetailsActivity

import com.example.mymad1.adapters.TeacherInfoAdapter
//import com.example.mymad1.models.TeacherModel
import com.example.mymad1.models.Teachers
import com.google.firebase.database.*

class FetchingTeacherInfo : AppCompatActivity(){
    private lateinit var teacherRecyclerView : RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var teacherList : ArrayList<Teachers>

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

        teacherList = arrayListOf<Teachers>()

        getTeacherData(condition)
    }

    private fun getTeacherData(condition: Int){
        teacherRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("teachers")

        //filter teachers by subject
        val query = when (condition) {
            1 -> dbRef.orderByChild("subject").equalTo("Combined Maths")
            2 -> dbRef.orderByChild("subject").equalTo("Biology")
            3 -> dbRef.orderByChild("subject").equalTo("Chemistry")
            4 -> dbRef.orderByChild("subject").equalTo("Physics")
            else -> dbRef
        }

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                teacherList.clear()
                if(snapshot.exists()){
                    for(teacherSnap in snapshot.children){
                        val teacherData = teacherSnap.getValue(Teachers::class.java)
                        teacherList.add(teacherData!!)
                    }

                    val mAdapter = TeacherInfoAdapter(teacherList)
                    teacherRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : TeacherInfoAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchingTeacherInfo, TeacherDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("uid", teacherList[position].uid)
                            intent.putExtra("name", teacherList[position].name)
                            intent.putExtra("subject", teacherList[position].subject)
                            intent.putExtra("fee", teacherList[position].fee)

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