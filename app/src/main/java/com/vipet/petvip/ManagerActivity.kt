package com.vipet.petvip

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vipet.petvip.Account.LoginActivity
import kotlinx.android.synthetic.main.activity_manager.*

class ManagerActivity : AppCompatActivity() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)
        val navView: BottomNavigationView = findViewById(R.id.manager_nav_view)
        navController = findNavController(R.id.manager_nav_host_fragment)

        NavigationUI.setupWithNavController(navView, navController)

        // 액션바 숨기
        supportActionBar?.let { bar ->
            bar.hide()
        }
        manger_tv_logout.setOnClickListener {
            showLogoutDialog()
        }
    }

    // 로그아웃 다이얼로그 출력
    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그아웃")
            .setMessage("로그아웃 하시겠습니까?")
            .setNegativeButton("취소", null)
            .setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                logOut()
            }
        builder.create().show()
    }

    // 로그아웃
    private fun logOut() {
        // 로그인 데이터 삭제
        LoginActivity.user.postValue(null)
        val pref = getSharedPreferences("account", Context.MODE_PRIVATE)
        val edit = pref?.edit()
        edit?.clear()
        edit?.apply()
        // 로그인 액티비티를 남기고 액티비티 종료
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    // 뒤로가기 2번 눌러 종료
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

    override fun onBackPressed() {
        var tempTime: Long = System.currentTimeMillis()
        var intervalTime: Long = tempTime - backPressedTime

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            Toast.makeText(this, "'뒤로가기'버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }
}