package com.vipet.petvip.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vipet.petvip.Account.LoginActivity
import com.vipet.petvip.Design.ReviewAdapter
import com.vipet.petvip.R
import com.vipet.petvip.Restful.ManagerDetail
import com.vipet.petvip.Restful.Rest
import com.vipet.petvip.ReviewActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagerReviewFragment : Fragment() {
    private lateinit var ratingBar: RatingBar
    private lateinit var ratingTv: TextView
    private lateinit var scheduleTv: TextView
    private lateinit var reviewTv: TextView
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_manager_review, container, false)
        ratingBar = view.findViewById(R.id.manager_review_rb_rating)
        ratingTv = view.findViewById(R.id.manager_review_tv_rating)
        scheduleTv = view.findViewById(R.id.manager_review_tv_schedule)
        reviewTv = view.findViewById(R.id.manager_review_tv_review)
        rv = view.findViewById(R.id.manager_review_rv)
        rv.layoutManager = LinearLayoutManager(context)

        getMangerReviews()
        return view
    }

    private fun getMangerReviews() {
        val id = LoginActivity.user.value!!.ID
        val call = Rest.getService().getDetailManager(id)
        call.enqueue(object : Callback<ManagerDetail> {
            override fun onFailure(call: Call<ManagerDetail>, t: Throwable) {
                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ManagerDetail>, response: Response<ManagerDetail>) {
                if (response.isSuccessful) {
                    response.body()?.let { manager ->
                        ratingBar.rating = manager.Rate
                        ratingTv.text = "${manager.Rate} / 5"
                        scheduleTv.text = "${manager.ScheduleCnt} 건"
                        reviewTv.text = "${manager.ReviewCnt} 건"
                        rv.adapter = ReviewAdapter(manager.Reviews)
                    }
                }
            }
        })
    }
}