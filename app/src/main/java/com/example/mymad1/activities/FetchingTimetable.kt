package com.example.mymad1.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymad1.activities.TimetableDetailsActivity
import com.example.mymad1.adapters.TimetableAdapter
import com.example.mymad1.models.TimetableModel
import com.example.mymad1.R

import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class FetchingTimetable : AppCompatActivity() {

    private lateinit var timetableRecyclerView : RecyclerView
    private lateinit var tvLoadingData : TextView
    private lateinit var timetableList : ArrayList<TimetableModel>

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timetable_fetch)

        timetableRecyclerView = findViewById(R.id.rvTimetableTime)
        timetableRecyclerView.layoutManager = LinearLayoutManager(this)
        timetableRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        timetableList = arrayListOf<TimetableModel>()

        getTimetableData()
    }

    private fun getTimetableData(){
        timetableRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Timetable")

        dbRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                timetableList.clear()
                if(snapshot.exists()){
                    for(timetableSnap in snapshot.children){
                        val timetableData = timetableSnap.getValue(TimetableModel::class.java)
                        timetableList.add(timetableData!!)
                    }

                    val mAdapter = TimetableAdapter(timetableList)
                    timetableRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : TimetableAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@FetchingTimetable, TimetableDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("timetableCode", timetableList[position].timetableCode)
                            intent.putExtra("teacherName", timetableList[position].teacherName)
                            intent.putExtra("subjectName", timetableList[position].subjectName)
                            intent.putExtra("timetableDate", timetableList[position].timetableDate)
                            intent.putExtra("timetableTime", timetableList[position].timetableTime)
                            intent.putExtra("timetableEndTime", timetableList[position].timetableEndTime)
                            intent.putExtra("timetableLink", timetableList[position].timetableLink)

                            startActivity(intent)
                        }
                    })
                    timetableRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}


















//package com.example.mymad1.activities
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.mymad1.activities.TimetableDetailsActivity
//import com.example.mymad1.adapters.TimetableAdapter
//import com.example.mymad1.models.TimetableModel
//import com.example.mymad1.R
//
//import com.google.firebase.database.*
//import com.google.firebase.ktx.Firebase
//
//class FetchingTimetable : AppCompatActivity() {
//
//    private lateinit var timetableRecyclerView : RecyclerView
//    private lateinit var tvLoadingData : TextView
//    private lateinit var timetableList : ArrayList<TimetableModel>
//
//    private lateinit var dbRef : DatabaseReference
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.timetable_fetch)
//
//        timetableRecyclerView = findViewById(R.id.rvTimetableTime)
//        timetableRecyclerView.layoutManager = LinearLayoutManager(this)
//        timetableRecyclerView.setHasFixedSize(true)
//        tvLoadingData = findViewById(R.id.tvLoadingData)
//
//        timetableList = arrayListOf<TimetableModel>()
//
//        getTimetableData()
//    }
//
//    private fun getTimetableData(){
//        timetableRecyclerView.visibility = View.GONE
//        tvLoadingData.visibility = View.VISIBLE
//
//        dbRef = FirebaseDatabase.getInstance().getReference("Timetable")
//
//        dbRef.addValueEventListener(object:ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                timetableList.clear()
//                if(snapshot.exists()){
//                    for(timetableSnap in snapshot.children){
//                        val timetableData = timetableSnap.getValue(TimetableModel::class.java)
//                        timetableList.add(timetableData!!)
//                    }
//
//                    val mAdapter = TimetableAdapter(timetableList)
//                    timetableRecyclerView.adapter = mAdapter
//
//                    mAdapter.setOnItemClickListener(object : TimetableAdapter.onItemClickListener{
//                        override fun onItemClick(position: Int) {
//                            val intent = Intent(this@FetchingTimetable, TimetableDetailsActivity::class.java)
//
//                            //put extras
//                            intent.putExtra("timetableCode", timetableList[position].timetableCode)
//                            intent.putExtra("teacherName", timetableList[position].teacherName)
//                            intent.putExtra("subjectName", timetableList[position].subjectName)
//                            intent.putExtra("timetableDate", timetableList[position].timetableDate)
//                            intent.putExtra("timetableTime", timetableList[position].timetableTime)
//                            intent.putExtra("timetableLink", timetableList[position].timetableLink)
//
//                            startActivity(intent)
//                        }
//                    })
//                    timetableRecyclerView.visibility = View.VISIBLE
//                    tvLoadingData.visibility = View.GONE
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//
//
//            }
//        })
//    }
//}