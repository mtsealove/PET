package com.vipet.petvip.Fragments.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipet.petvip.Account.LoginActivity
import com.vipet.petvip.Account.RegisterPetActivity
import com.vipet.petvip.Design.NextBtn
import com.vipet.petvip.Design.ReviewAdapter
import com.vipet.petvip.Design.ScheduleAdapter
import com.vipet.petvip.MainActivity
import com.vipet.petvip.R
import com.vipet.petvip.ReserveActivity
import com.vipet.petvip.Restful.Pet
import com.vipet.petvip.Restful.Rest
import com.vipet.petvip.Restful.Review
import com.vipet.petvip.Restful.Schedule
import kotlinx.android.synthetic.main.activity_reserve.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    val REGISTER = 200

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var btn_play: LinearLayout
    private lateinit var btn_walk: LinearLayout
    private lateinit var btn_reserve: NextBtn
    private lateinit var rv: RecyclerView
    private lateinit var reviewRv: RecyclerView
    private lateinit var tv_schedule_more: TextView
    private var selected = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        rv = root.findViewById(R.id.home_rv)
        rv.layoutManager = LinearLayoutManager(context)
        reviewRv = root.findViewById(R.id.home_rv_review)
        reviewRv.layoutManager = LinearLayoutManager(context)
        btn_play = root.findViewById(R.id.home_btn_play)
        btn_walk = root.findViewById(R.id.home_btn_walk)
        btn_reserve = root.findViewById(R.id.home_reserve_btn)
        btn_reserve.setTitle("예약하기")

        btn_play.setOnClickListener { setReserveBtn(1) }
        btn_walk.setOnClickListener { setReserveBtn(0) }

        btn_reserve.root.setOnClickListener {
            next()
        }
        tv_schedule_more = root.findViewById(R.id.home_tv_schedule_more)
        // 2번 탭 클릭
        tv_schedule_more.setOnClickListener {
            (context as MainActivity).navController.navigate(R.id.navigation_dashboard)
        }
        getSchedule()
        getReviews()
        return root
    }

    // 클릭에 따른 테두리 변경
    private fun setReserveBtn(selected: Int) {
        if (selected == 0) {
            btn_play.setBackgroundResource(R.drawable.reserve_back)
            btn_walk.setBackgroundResource(R.drawable.reserve_back_active)
            this.selected = 1
        } else {
            btn_play.setBackgroundResource(R.drawable.reserve_back_active)
            btn_walk.setBackgroundResource(R.drawable.reserve_back)
            this.selected = 0
        }
    }

    // 다음 페이지로 이동
    private fun next() {
        val id = LoginActivity.user.value?.ID

        val call = Rest.getService().getPets(id!!)
        call.enqueue(object : Callback<List<Pet>> {
            override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                if (response.isSuccessful) {
                    // 등록한 반려견이 없을 경우
                    if (response.body().isNullOrEmpty()) {
                        startActivityForResult(
                            Intent(context, RegisterPetActivity::class.java),
                            REGISTER
                        )
                    } else {
                        // 등록한 반려견이 있을 경우
                        val it = Intent(context, ReserveActivity::class.java)
                        it.putExtra("service", selected)
                        startActivity(it)
                    }
                } else {
                    Toast.makeText(context, "서버 오류 발생", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun getSchedule() {
        val id = LoginActivity.user.value!!.ID
        val call = Rest.getService().getSchedule(id, 2)
        call.enqueue(object : Callback<List<Schedule>> {
            override fun onFailure(call: Call<List<Schedule>>, t: Throwable) {
                context?.let { c ->
                    Toast.makeText(c, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(
                call: Call<List<Schedule>>,
                response: Response<List<Schedule>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    // 크기 조절
                    response.body()?.let { schedules ->
                        context?.let { c ->
                            var height = 206
                            height *= response.body()!!.size * 5
                            rv.setHeight(height)
                            rv.adapter = ScheduleAdapter(schedules, c)
                        }

                    }
                }
            }
        })
    }

    // 리뷰 3개 가저오기
    private fun getReviews() {
        val call = Rest.getService().getAllReviews()
        call.enqueue(object : Callback<List<Review>> {
            override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<Review>>, response: Response<List<Review>>) {
                if (response.isSuccessful) {
                    response.body()?.let { reviews ->
                        reviewRv.adapter = ReviewAdapter(reviews)
                        val height = 400 * reviews.size
                        reviewRv.setHeight(height)
                    }
                }
            }
        })
    }

    fun View.setHeight(value: Int) {
        val lp = layoutParams
        lp?.let {
            lp.height = value
            layoutParams = lp
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REGISTER -> {
                    next()
                }
            }
        }
    }
}