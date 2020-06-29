package com.vipet.petvip.Design

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vipet.petvip.R
import com.vipet.petvip.Restful.Schedule
import com.vipet.petvip.ReviewActivity
import kotlinx.android.synthetic.main.item_schedule.view.*

class ScheduleAdapter(private val scheduleList: List<Schedule>, val context: Context) :
    RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scheduleList[position], context)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val serviceTv = v.findViewById<TextView>(R.id.item_schedule_tv_service)
        private val dateTv = v.findViewById<TextView>(R.id.item_schedule_tv_date)
        private val petTv = v.findViewById<TextView>(R.id.item_schedule_tv_pet)
        private val timeTv = v.findViewById<TextView>(R.id.item_schedule_tv_time)
        private val expertTv = v.findViewById<TextView>(R.id.item_schedule_tv_expert)
        private val monthTv = v.findViewById<TextView>(R.id.item_schedule_tv_month)
        private val dayTv = v.findViewById<TextView>(R.id.item_schedule_tv_day)
        private val reviewTv = v.findViewById<TextView>(R.id.item_schedule_tv_review)

        fun bind(schedule: Schedule, context: Context) {
            serviceTv.text = schedule.Service
            dateTv.text = schedule.Date
            petTv.text = schedule.Pet
            timeTv.text = schedule.Time
            expertTv.text = schedule.Manager
            monthTv.text = schedule.Month
            dayTv.text = schedule.Day

            // 후기 작성
            reviewTv.setOnClickListener {
                val intent = Intent(context, ReviewActivity::class.java)
                intent.putExtra("MANAGER", schedule.ManagerID)
                context.startActivity(intent)
            }
        }
    }
}