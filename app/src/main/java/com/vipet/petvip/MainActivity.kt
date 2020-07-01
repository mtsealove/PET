package com.vipet.petvip

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_manager.*

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(navView, navController)
        supportActionBar!!.hide()

        /*
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
         */

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        main_tv_manager.setOnClickListener {
            startActivity(Intent(this, ManagerListActivity::class.java))
        }
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
