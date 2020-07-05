package com.vipet.petvip.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipet.petvip.Design.ManagerAdapter
import com.vipet.petvip.Fragments.SelectTime.SelectTimeFragment
import com.vipet.petvip.Fragments.SelectTime.TimeViewModel

import com.vipet.petvip.R
import com.vipet.petvip.ReserveActivity
import com.vipet.petvip.Restful.Manager
import com.vipet.petvip.Restful.Rest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException
import java.util.*
import javax.security.auth.login.LoginException
import kotlin.math.log

class SelectManagerFragment(val Start: String, val End: String, val price: Int) : Fragment() {
    val model = TimeViewModel()
    lateinit var rv: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getManagers(Start, End)
        val root = inflater.inflate(R.layout.fragment_select_manager, container, false)
        rv = root.findViewById(R.id.select_manager_rv)
        rv.layoutManager = LinearLayoutManager(context)
        return root
    }

    //가용 매니저 목록 추력
    private fun getManagers(start: String, end: String) {
        val call = Rest.getService().getAbleManagers(start, end)
        call.enqueue(object : Callback<List<Manager>> {
            override fun onFailure(call: Call<List<Manager>>, t: Throwable) {
                Log.e("error", t.toString())
                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Manager>>, response: Response<List<Manager>>) {
                if (response.isSuccessful && response.body() != null) {
                    val adapter = ManagerAdapter(response.body()!!, context!!, start, end, price)
                    rv.adapter = adapter
                }
            }

        })
    }

}
