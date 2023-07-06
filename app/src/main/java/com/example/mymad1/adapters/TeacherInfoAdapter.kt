package com.example.mymad1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
//import com.example.mymad1.models.TeacherModel
import com.example.mymad1.R
import com.example.mymad1.models.Teachers

//class TeacherInfoAdapter(private val teacherList : ArrayList<TeacherModel>) : RecyclerView.Adapter<TeacherInfoAdapter.ViewHolder>(){
class TeacherInfoAdapter(private val teacherList : ArrayList<Teachers>) : RecyclerView.Adapter<TeacherInfoAdapter.ViewHolder>(){
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: TeacherInfoAdapter.onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.teacher_list, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: TeacherInfoAdapter.ViewHolder, position: Int) {
        val currentUser = teacherList[position]

//remember about tvTeacherName
        holder.tvTeacherName.text = currentUser.name
// holder.tvTeacherName.text = currentUser.teacherName
    }

    override fun getItemCount(): Int {
        return teacherList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvTeacherName : TextView = itemView.findViewById(R.id.tvTeacher)

        init{
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}










//package com.example.mymad1.adapters
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.mymad1.models.TeacherModel
//import com.example.mymad1.R
//
//class TeacherInfoAdapter(private val teacherList : ArrayList<TeacherModel>) : RecyclerView.Adapter<TeacherInfoAdapter.ViewHolder>(){
//    private lateinit var mListener: onItemClickListener
//
//    interface onItemClickListener{
//        fun onItemClick(position: Int)
//    }
//
//    fun setOnItemClickListener(clickListener: TeacherInfoAdapter.onItemClickListener){
//        mListener = clickListener
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.teacher_list, parent, false)
//        return ViewHolder(itemView, mListener)
//    }
//
//    override fun onBindViewHolder(holder: TeacherInfoAdapter.ViewHolder, position: Int) {
//        val currentUser = teacherList[position]
//        holder.tvTeacherName.text = currentUser.teacherName
//    }
//
//    override fun getItemCount(): Int {
//        return teacherList.size
//    }
//
//    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
//        val tvTeacherName : TextView = itemView.findViewById(R.id.tvTeacher)
//
//        init{
//            itemView.setOnClickListener{
//                clickListener.onItemClick(adapterPosition)
//            }
//        }
//    }
//}
//
