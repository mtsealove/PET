package com.vipet.petvip.Account

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.vipet.petvip.R
import com.vipet.petvip.Restful.Rest
import com.vipet.petvip.Restful.Result
import com.vipet.petvip.Restful.Account
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    lateinit var lm: LocationManager
    val cat by lazy {
        intent.getIntExtra(SignUpChooseActivity.Cat, SignUpChooseActivity.NORMAL)
    }

    lateinit var waitDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar!!.hide()

        // 전화번호 입력 확인
        sign_et_phone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        setAddrTv()
        checkIdOverlap()
        setPwMatch()
        setConfirmBtn()


        waitDialog = ProgressDialog(this)
        waitDialog.setTitle("대기")
        waitDialog.setMessage("잠시만 기다려주세요.")
        waitDialog.setCancelable(false)
    }

    private fun setConfirmBtn() {
        sign_btn_confirm.setOnClickListener {
            if (checkInput()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("회원가입")
                    .setMessage("회원가입 하시겠습니까?")
                    .setNegativeButton("취소", null)
                    .setPositiveButton("확인") { dialog, which ->
                        val name = sign_et_name.text.toString()
                        val id = sign_et_id.text.toString()
                        val pw = sign_et_pw.text.toString()
                        val phone = sign_et_phone.text.toString()
                        val addr = sign_tv_addr.text.toString()
                        // 회원가입 객체를 만들어 서버에 전송
                        val sign = Account(name, id, pw, phone, addr, cat)
                        val call = Rest.getService().Join(sign)
                        call.enqueue(object : Callback<Result> {
                            override fun onFailure(call: Call<Result>, t: Throwable) {
                                Toast.makeText(applicationContext, "서버 연결 실패", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onResponse(
                                call: Call<Result>,
                                response: Response<Result>
                            ) {
                                // 회원가입 성공 시
                                if (response.isSuccessful && response.body()!!.OK) {
                                    // 회원가입 성공을 이전 액티비티로 전달
                                    setResult(Activity.RESULT_OK)
                                    Toast.makeText(
                                        applicationContext,
                                        "회원가입이 완료되었습니다.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    finish()
                                }
                            }

                        })
                    }
                builder.create().show()
            }
        }
    }

    // 아이디 입력 변경 시 서버 처리
    private fun checkIdOverlap() {
        val tl = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val id = s.toString()
                // 길이 측정
                if (id.length < 8) {
                    sign_tv_err.visibility = View.VISIBLE
                } else { // 중복 측정
                    Rest.getService().idOverlap(id).enqueue(object : Callback<Result> {
                        override fun onFailure(call: Call<Result>, t: Throwable) {
                            Toast.makeText(baseContext, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                        }

                        // 서버 연결 성공
                        override fun onResponse(call: Call<Result>, response: Response<Result>) {
                            if (response.isSuccessful) {
                                // 사용 가능 여부에 따라 아이디 오류 메세지 출력
                                val rs = response.body()!!.OK
                                if (rs) {
                                    sign_tv_err.visibility = View.GONE
                                } else {
                                    sign_tv_err.visibility = View.VISIBLE
                                }

                            }
                        }

                    })
                }

            }
        }
        sign_et_id.addTextChangedListener(tl)
    }

    // 주소를 선택하면 권한 체크 후 변환된 주소 표시
    private fun setAddrTv() {
        sign_tv_addr.setOnClickListener {
            TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .check()
        }
    }

    // 권한 체크 리스너
    private val permissionListener: PermissionListener = object : PermissionListener {
        @SuppressLint("MissingPermission")
        override fun onPermissionGranted() {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1f, locationListener)
            waitDialog.show()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(applicationContext, "권한을 허용하셔야 위치를 사용할 수 있습니다.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // 비밀번호 확인
    private fun setPwMatch() {
        val pwListener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (sign_et_pw.text.toString().equals(sign_et_pw_check.text.toString())) {
                    sign_tv_pw.visibility = View.GONE
                } else {
                    sign_tv_pw.visibility = View.VISIBLE
                }
            }

        }
        sign_et_pw_check.addTextChangedListener(pwListener)
    }


    // 위치정보를 얻어 화면에 표시
    val locationListener = object : LocationListener {
        // 위치정보 업데이트 완료
        override fun onLocationChanged(location: Location?) {
            // 위경도 한글주소 변환
            val geocoder = Geocoder(applicationContext)
            Log.e("addr", "lat: ${location!!.latitude}, lng: ${location!!.longitude}")
            try {
                val addrs = geocoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
                if (addrs != null && addrs.size != 0) {
                    var addr = addrs.get(0).getAddressLine(0)
                    addr = addr.replace("대한민국 ", "")
                    sign_tv_addr.setText(addr)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // 리스너 제거
            lm.removeUpdates(this)
            waitDialog.dismiss()
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

        }

        override fun onProviderEnabled(provider: String?) {

        }

        override fun onProviderDisabled(provider: String?) {

        }

    }

    // 입력 확인
    private fun checkInput(): Boolean {
        var snackbar = Snackbar.make(signup_layout, "", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("확인", {
            snackbar.dismiss()
        })

        if (sign_et_name.text.isEmpty()) {
            snackbar.setText("이름을 입력해주세요.")
            snackbar.show()
            return false
        } else if (sign_et_id.text.isEmpty()) {
            snackbar.setText("ID를 입력해주세요.")
            snackbar.show()
            return false
        } else if (sign_tv_err.visibility == View.VISIBLE) {
            snackbar.setText("올바른 ID를 입력해주세요.")
            snackbar.show()
            return false
        } else if (sign_et_pw.text.isEmpty()) {
            snackbar.setText("비밀번호를 입력해주세요.")
            snackbar.show()
            return false
        } else if (!sign_et_pw.text.toString().equals(sign_et_pw_check.text.toString())) {
            snackbar.setText("비밀번호가 일치하지 않습니다.")
            snackbar.show()
            return false
        } else if (sign_et_phone.text.isEmpty()) {
            snackbar.setText("전화번호를 입력해주세요.")
            snackbar.show()
            return false
        } else if (sign_tv_addr.text.toString().equals("주소")) {
            snackbar.setText("주소를 선택해주세요.")
            snackbar.show()
            return false
        } else {
            return true
        }
    }
}
