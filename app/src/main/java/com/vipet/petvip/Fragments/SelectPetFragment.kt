package com.vipet.petvip.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipet.petvip.Account.LoginActivity
import com.vipet.petvip.Account.SignUpActivity
import com.vipet.petvip.Design.PetAdapter

import com.vipet.petvip.R
import com.vipet.petvip.Restful.Pet
import com.vipet.petvip.Restful.Rest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectPetFragment(val service: Int) : Fragment() {
    var Service: String
    lateinit var serviceTv: TextView
    lateinit var petRv: RecyclerView

    init {
        if (service == 1) {
            Service = "산책하기"
        } else {
            Service = "돌봄"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_select_pet, container, false)
        serviceTv = root.findViewById(R.id.select_pet_tv_service)
        serviceTv.text = Service

        petRv = root.findViewById(R.id.select_pet_rv_pet)
        petRv.layoutManager = LinearLayoutManager(context)
        getPets()
        return root
    }


    // 반려견 목록 가져오기
    private fun getPets() {
        val id = LoginActivity.user.value!!.ID
        val call = Rest.getService().getPets(id)
        call.enqueue(object : Callback<List<Pet>> {
            override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                if (response.isSuccessful && response.body() != null) {
                    val pets = response.body()
                    Log.e("pet", pets.toString())
                    val adapter = PetAdapter(pets!!, context!!)
                    petRv.adapter = adapter
                }
            }
        })
    }
}
