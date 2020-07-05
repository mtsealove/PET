package com.vipet.petvip.Fragments

import android.app.AlertDialog
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipet.petvip.Account.LoginActivity
import com.vipet.petvip.Design.ManagerScheduleAdapter
import com.vipet.petvip.R
import com.vipet.petvip.Restful.ManagerSchedule
import com.vipet.petvip.Restful.Rest
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagerScheduleFragment : Fragment() {
    lateinit var scheduleRv: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_manager_schedule, container, false)
        scheduleRv = view.findViewById(R.id.manager_schedule_rv)
        scheduleRv.layoutManager = LinearLayoutManager(context)
        getSchedule()
        return view
    }

    private fun getSchedule() {
        val id = LoginActivity.user.value!!.ID
        val call = Rest.getService().getManagerSchedule(id)
        call.enqueue(object : Callback<List<ManagerSchedule>> {
            override fun onFailure(call: Call<List<ManagerSchedule>>, t: Throwable) {
                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<ManagerSchedule>>,
                response: Response<List<ManagerSchedule>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    scheduleRv.adapter = ManagerScheduleAdapter(response.body()!!, context!!)
                }
            }

        })
    }

}