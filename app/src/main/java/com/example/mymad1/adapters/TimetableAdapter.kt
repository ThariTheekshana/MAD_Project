package com.example.mymad1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymad1.R
import com.example.mymad1.models.TimetableModel

class TimetableAdapter(private val timetableList : ArrayList<TimetableModel>) : RecyclerView.Adapter<TimetableAdapter.ViewHolder>(){
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.timetable_list, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: TimetableAdapter.ViewHolder, position: Int) {
        val currentTimetable = timetableList[position]
        holder.tvTimetableTime.text = currentTimetable.teacherName
    }

    override fun getItemCount(): Int {
        return timetableList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvTimetableTime : TextView = itemView.findViewById(R.id.tvTimetableTime)

        init{
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}

