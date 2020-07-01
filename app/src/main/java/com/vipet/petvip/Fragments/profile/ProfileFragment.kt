package com.vipet.petvip.Fragments.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipet.petvip.Account.LoginActivity
import com.vipet.petvip.Account.RegisterPetActivity
import com.vipet.petvip.Design.PetAdapter
import com.vipet.petvip.R
import com.vipet.petvip.Restful.Pet
import com.vipet.petvip.Restful.Rest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var petRv: RecyclerView
    private lateinit var addBtn: Button
    private lateinit var logoutBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        petRv = root.findViewById(R.id.profile_rv_pet)
        addBtn = root.findViewById(R.id.profile_btn_add_pet)
        logoutBtn = root.findViewById(R.id.profile_btn_logout)
        getPetList()

        addBtn.setOnClickListener {
            addPet()
        }

        logoutBtn.setOnClickListener {
            showLogoutDialog()
        }

        return root
    }
    // 반려견 목록 출력
    private fun getPetList() {
        petRv.layoutManager = LinearLayoutManager(context)
        LoginActivity.user.value?.ID?.let { id ->
            val call = Rest.getService().getPets(id)
            call.enqueue(object : Callback<List<Pet>> {
                override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                    Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                    if (response.isSuccessful && response.body()?.isNotEmpty()!!) {
                        petRv.adapter = PetAdapter(response.body()!!, context!!, false)
                    }
                }
            })
        }
    }
    // 반려견 추가 액티비티로 이동
    private fun addPet() {
        context?.let { c ->
            c.startActivity(Intent(c, RegisterPetActivity::class.java))
        }
    }
    // 로그아웃 다이얼로그 출력
    @SuppressLint("CommitPrefEdits")
    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("로그아웃")
            .setMessage("로그아웃 하시겠습니까?")
            .setNegativeButton("취소", null)
            .setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                // 로그인 데이터 삭제
                LoginActivity.user.postValue(null)
                val pref = context?.getSharedPreferences("account", Context.MODE_PRIVATE)
                val edit = pref?.edit()
                edit?.clear()
                edit?.apply()
                // 로그인 액티비티를 남기고 액티비티 종료
                context?.startActivity(Intent(context, LoginActivity::class.java))
                (context as Activity).finish()
            }
        builder.create().show()
    }
}