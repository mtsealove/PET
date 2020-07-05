package com.vipet.petvip

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vipet.petvip.Account.LoginActivity
import kotlinx.android.synthetic.main.activity_manager.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


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

    // 위치 정보 확인 다이얼로그, 지도는 하나만 표시 가능하기 때문에 adapter에 못넣음
    fun showLocation(addr: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_location, null, false)
        val tv = view.findViewById<TextView>(R.id.dialog_location_tv)

        val container = view.findViewById<RelativeLayout>(R.id.dialog_location_container)
        // adb 불가
        val mapView = MapView(this)
        container.addView(mapView)

        tv.text = addr
        val geoCoder = Geocoder(this)
        val list = geoCoder.getFromLocationName(addr, 3)
        if (list.isNotEmpty()) {
            Log.e("latitude", list[0].latitude.toString())
            Log.e("logitude", list[0].longitude.toString())

            tv.setOnClickListener {
                try{
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("kakaomap://look?p=${list[0].latitude},${list[0].longitude}")))
                } catch (e:Exception) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=net.daum.android.map")))
                }

            }
            val point = MapPoint.mapPointWithGeoCoord(
                list[0].latitude,
                list[0].longitude
            )
            mapView.setMapCenterPoint(point, true)

            val marker = MapPOIItem()
            marker.itemName = "고객 위치"
            marker.tag = 0
            marker.mapPoint = point
            marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.
            marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mapView.addPOIItem(marker)


            // 카카오맵 바로가기
        } else {
            Log.e("location", "not found")
        }
        val builder = android.app.AlertDialog.Builder(this)
        builder.setView(view)
        builder.create().show()
    }


}