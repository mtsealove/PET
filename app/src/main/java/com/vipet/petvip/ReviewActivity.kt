package com.vipet.petvip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ReviewActivity : AppCompatActivity() {
    // 리뷰할 매니저
    val managerID by lazy {
        intent.getStringExtra("MANAGER")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
    }
}
