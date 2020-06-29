package com.vipet.petvip.Fragments.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipet.petvip.Account.LoginActivity
import com.vipet.petvip.Design.BlankDecoration
import com.vipet.petvip.Design.ScheduleAdapter
import com.vipet.petvip.R
import com.vipet.petvip.Restful.Rest
import com.vipet.petvip.Restful.Schedule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScheduleFragment : Fragment() {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scheduleViewModel =
            ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)
        rv = root.findViewById(R.id.schedule_rv)
        rv.layoutManager = LinearLayoutManager(context)
        getSchedule()
        return root
    }

    private fun getSchedule() {
        val id = LoginActivity.user.value!!.ID
        val call = Rest.getService().getSchedule(id, 9999)
        call.enqueue(object : Callback<List<Schedule>> {
            override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<Schedule>>,
                response: Response<List<Schedule>>
            ) {
                if (response.isSuccessful) {
                    if (response.body()!!.isNotEmpty()) {
                        val adapter = ScheduleAdapter(response.body()!!, context!!)
                        rv.adapter = adapter
                    } else {

                    }
                }
            }

        })
    }
}