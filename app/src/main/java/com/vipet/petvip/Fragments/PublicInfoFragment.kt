package com.vipet.petvip.Fragments

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipet.petvip.Design.FoodAdapter
import com.vipet.petvip.MainActivity
import com.vipet.petvip.R
import com.vipet.petvip.Restful.PublicData
import com.vipet.petvip.Restful.PublicRest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PublicInfoFragment : Fragment() {
    private lateinit var searchEt: EditText
    private lateinit var searchBtn: Button
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_public_info, container, false)
        searchBtn = root.findViewById(R.id.public_info_btn_search)
        searchEt = root.findViewById(R.id.public_info_et_search)

        searchBtn.setOnClickListener {
            search()
        }
        rv = root.findViewById(R.id.public_info_rv)
        rv.layoutManager = LinearLayoutManager(context)

        // 엔터키로 검색
        searchEt.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                    true
                }
                else -> {
                    false
                }
            }
        }
        return root
    }


    // 입력값 체크 후 검색
    private fun search() {
        closeKeyboard()
        if (searchEt.text.toString().isEmpty()) {
            Toast.makeText(context, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show()
        } else {
            getPublicData(searchEt.text.toString())
        }
    }


    // 공공데이터 접속
    private fun getPublicData(query: String) {
        val call = PublicRest.getService().getFoodList(fd = query)
        call.enqueue(object : Callback<PublicData> {
            override fun onFailure(call: Call<PublicData>, t: Throwable) {
                Log.e("err", t.toString())
                rv.adapter = null
                rv.visibility = View.GONE
            }

            override fun onResponse(call: Call<PublicData>, response: Response<PublicData>) {
                if (response.isSuccessful) {
                    Log.e("success", response.toString())
                    response.body()?.let { data ->
                        val list = data.body!!.items!!.itemList!!
                        if (list.isNotEmpty()) {
                            rv.adapter = FoodAdapter(list, context!!)
                            rv.visibility = View.VISIBLE
                        } else {
                            rv.adapter = null
                            rv.visibility = View.GONE
                        }
                    } ?: {
                        rv.adapter = null

                    }()
                }
            }
        })
    }


    private fun closeKeyboard() {
        var view = (context as MainActivity).currentFocus

        if (view != null) {
            val inputMethodManager =
                (context as Activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
