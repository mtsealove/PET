package com.vipet.petvip

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.google.android.material.tabs.TabLayout
import com.vipet.petvip.Fragments.SectionsPagerAdapter
import com.vipet.petvip.Restful.Pet

class ReserveActivity : AppCompatActivity() {
    companion object {
        var Start = ""
        var End = ""
    }

    lateinit var viewPager: ViewPager
    var pet = MutableLiveData<Pet>()
    lateinit var prevAdapter: SectionsPagerAdapter
    var service = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserve)
        service = intent.getIntExtra("service", 1)
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager,
                service
            )

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        prevAdapter = SectionsPagerAdapter(
            this,
            supportFragmentManager,
            service
        )
        // 액변바 보이ㅣㄱ
        supportActionBar!!.show()

        viewPager.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }
        })
    }

    // 뒤로가기를 프래그먼트 변경으로 사용
    override fun onBackPressed() {
        when (viewPager.currentItem) {
            0 -> super.onBackPressed()
            2 -> {
                viewPager.adapter = prevAdapter
                viewPager.currentItem = 1
            }
            else -> {
                viewPager.currentItem = --viewPager.currentItem
            }
        }
    }
}