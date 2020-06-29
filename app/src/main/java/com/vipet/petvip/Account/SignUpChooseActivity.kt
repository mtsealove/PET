package com.vipet.petvip.Account

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vipet.petvip.R
import kotlinx.android.synthetic.main.activity_sign_up_choose.*

class SignUpChooseActivity : AppCompatActivity() {
    val SignUpCode = 100

    companion object {
        val Cat = "cat"
        val NORMAL: Int = 0
        val MANAGER: Int = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_choose)

        supportActionBar!!.hide()

        signup_choose_btn_normal.setOnClickListener {
            moveSignUp(NORMAL)
        }
        signup_choose_btn_manager.setOnClickListener {
            moveSignUp(MANAGER)
        }
    }


    // 어떠한 회원원으로 가입할 것인지 넘기고 액티비티 이동
    fun moveSignUp(cat: Int) {
        val intent = Intent(this, SignUpActivity::class.java)
        intent.putExtra(Cat, cat)
        startActivityForResult(intent, SignUpCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (resultCode) {
                SignUpCode ->
                    finish()
            }
        }
    }
}
