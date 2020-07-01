package com.vipet.petvip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.vipet.petvip.Account.LoginActivity
import com.vipet.petvip.Design.ReviewAdapter
import com.vipet.petvip.Restful.ManagerDetail
import com.vipet.petvip.Restful.PostReview
import com.vipet.petvip.Restful.Rest
import com.vipet.petvip.Restful.Result
import com.vipet.petvip.Restful.Review
import kotlinx.android.synthetic.main.activity_review.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewActivity : AppCompatActivity() {
    // 리뷰할 매니저
    val managerID by lazy {
        intent.getStringExtra("MANAGER")
    }

    val autoDialog by lazy {
        intent.getBooleanExtra("AUTO", false)
    }

    lateinit var actionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        review_rv.layoutManager = LinearLayoutManager(baseContext)
        review_btn_review.setTitle("후기 작성")
        review_btn_review.root.setOnClickListener {
            showDialog()
        }
        if (autoDialog) {
            showDialog()
        }
        getManager()
    }


    private fun getManager() {
        val call = Rest.getService().getDetailManager(managerID)
        call.enqueue(object : Callback<ManagerDetail> {
            override fun onFailure(call: Call<ManagerDetail>, t: Throwable) {
                Toast.makeText(baseContext, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ManagerDetail>, response: Response<ManagerDetail>) {
                if (response.isSuccessful) {
                    Log.e("manager", response.body().toString())
                    val manager = response.body()!!
                    actionBar.title = manager.Name + " 매니저"
                    review_rb_rating.rating = manager.Rate
                    review_tv_rating.text = "${manager.Rate} / 5"
                    review_tv_schedule.text = "${manager.ScheduleCnt} 건"
                    review_tv_review.text = "${manager.ReviewCnt} 건"
                    review_rv.adapter = ReviewAdapter(manager.Reviews)
                }
            }

        })
    }

    // 리뷰 작성 다이얼로그 출력
    private fun showDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_review, null, false)
        val contentEt = view.findViewById<EditText>(R.id.dialog_et_content)
        val completeBtn = view.findViewById<Button>(R.id.dialog_btn_complete)
        val rating = view.findViewById<RatingBar>(R.id.dialog_rating)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        completeBtn.setOnClickListener {
            if (contentEt.text.isEmpty()) {
                Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                createReview(contentEt.text.toString(), rating.rating)
                dialog.dismiss()
            }
        }
    }

    // 리뷰 작성
    private fun createReview(content: String, rating: Float) {
        val memberID = LoginActivity.user.value!!.ID
        val review = PostReview(memberID, managerID, rating, content)
        val call = Rest.getService().createReview(review)
        call.enqueue(object : Callback<Result> {
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(baseContext, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.isSuccessful && response.body()!!.OK) {
                    Toast.makeText(baseContext, "리뷰가 작성되었습니다.", Toast.LENGTH_SHORT).show()
                    // 정보 갱신
                    getManager()
                }
            }

        })
    }

    // 액션바 뒤로가기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
