package com.vipet.petvip.Account

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.vipet.petvip.MainActivity
import com.vipet.petvip.R
import com.vipet.petvip.Restful.Account
import com.vipet.petvip.Restful.LoginData
import com.vipet.petvip.Restful.Rest
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()

        setLogin()
        login_tv_signup.setOnClickListener {
            moveSignUp()
        }

        autoLogin()
    }

    // 다른 클래스에서도 사용하는 라이브 데이터
    companion object {
        var user: MutableLiveData<Account> = MutableLiveData()
    }

    private fun Login(account: Account) {
        user.value = account
        // 메인 액티비티로 이동
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // 스낵바 만들기
    private fun showSnackBar(msg: String) {
        val snackbar = Snackbar.make(loginLayout, "", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("확인") {
            snackbar.dismiss()
        }
        snackbar.setText(msg)
        snackbar.show()
    }

    private fun setLogin() {
        login_btn_login.setOnClickListener {
            if (checkInput()) {
                val id = login_et_id.text.toString()
                val pw = login_et_pw.text.toString()
                val call = Rest.getService().Login(LoginData(id, pw))
                call.enqueue(object : Callback<Account> {
                    override fun onFailure(call: Call<Account>, t: Throwable) {
                        Toast.makeText(applicationContext, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<Account>, response: Response<Account>) {
                        if (response.isSuccessful) {
                            response.body()?.let { account: Account ->
                                val save = findViewById<CheckBox>(R.id.login_cb_keep).isChecked
                                account.PW = pw
                                saveAccount(save, account)
                                Login(account)
                            } ?: {
                                showSnackBar("아이디와 비밀번호를 확인하세요")
                            }()
                        }
                    }

                })
            }
        }
    }

    // 자동 로그인을 위해 계정정보 저장
    private fun saveAccount(save: Boolean, account: Account) {
        Log.e("account", account.toString())
        val pref = getSharedPreferences("account", Context.MODE_PRIVATE)
        val edit = pref.edit()
        if (save) {
            edit.putString("id", account.ID)
            edit.putString("pw", account.PW!!)
        } else {
            edit.clear()
        }
        edit.apply()
        edit.commit()
    }


    // 입력 확인
    private fun checkInput(): Boolean {
        val id = login_et_id.text.toString()
        val pw = login_et_pw.text.toString()

        // 아이디 및 비밀번호 입력 시 true, 미 입력 시 false 반환
        if (id.isEmpty()) {
            showSnackBar("아이디를 입력하세요.")
            return false
        } else if (pw.isEmpty()) {
            showSnackBar("비밀번호를 입력하세요.")
            return false
        } else return true
    }

    // 회원가입 선택 페이지로 이동
    private fun moveSignUp() {
        val intent = Intent(this, SignUpChooseActivity::class.java)
        startActivity(intent)
    }

    // 자동 로그인
    private fun autoLogin() {
        val pref = getSharedPreferences("account", Context.MODE_PRIVATE)
        val id = pref.getString("id", null)
        val pw = pref.getString("pw", null)

        // 로그인 트리거
        if (id != null && pw != null) {
            login_et_id.setText(id)
            login_et_pw.setText(pw)
            login_btn_login.performClick()
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
