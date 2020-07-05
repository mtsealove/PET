package com.vipet.petvip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.vipet.petvip.Design.ManagerAdapter
import com.vipet.petvip.Restful.Manager
import com.vipet.petvip.Restful.Rest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_manager_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagerListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_list)

        manager_list_rv.layoutManager = LinearLayoutManager(this)
        getManagerList()
    }

    private fun getManagerList() {
        val call = Rest.getService().getAllManagers()
        call.enqueue(object : Callback<List<Manager>> {
            override fun onFailure(call: Call<List<Manager>>, t: Throwable) {
                Toast.makeText(baseContext, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Manager>>, response: Response<List<Manager>>) {
                if (response.isSuccessful && response.body() != null) {
                    manager_list_rv.adapter =
                        ManagerAdapter(response.body()!!, baseContext, null, null, 0)
                }
            }
        })
    }
}
