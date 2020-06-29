package com.vipet.petvip.Account

import android.annotation.TargetApi
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.vipet.petvip.R
import com.vipet.petvip.Restful.Rest
import com.vipet.petvip.Restful.Result
import kotlinx.android.synthetic.main.activity_register_pet.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.lang.Exception

class RegisterPetActivity : AppCompatActivity() {
    lateinit var birth: String
    lateinit var path: String
    val GALLERY = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_pet)

        val actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setDate()
        }

        setProfileIV()

        register_btn_confirm.setOnClickListener {
            if (checkInput()) {
                Register()
            }
        }
    }

    // 입력 값 확인
    private fun checkInput(): Boolean {
        when {
            register_et_name.text.isEmpty() -> {
                Toast.makeText(this, "펫 네임을 입력하세요.", Toast.LENGTH_SHORT).show()
                return false
            }
            birth.isEmpty() -> {
                Toast.makeText(this, "생일을 선택하게요.", Toast.LENGTH_SHORT).show()
                return false
            }
            register_et_weight.text.isEmpty() -> {
                Toast.makeText(this, "몸무게를 입력하세요.", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> {
                return true
            }
        }
    }

    // 서버에 반려견 정보와 이미지 파일 등록
    private fun Register() {
        val memberID = LoginActivity.user.value!!.ID
        val name = register_et_name.text.toString()
        val species: String = register_sp_species.selectedItem as String
        val garr = arrayOf('F', 'M')
        val Gender = garr[register_sp_gender.selectedItemPosition]
        val weight = Integer.parseInt(register_et_weight.text.toString())

        var call: Call<Result>? = null
        if (!path.isEmpty()) {
            var file = File(path)
            var requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            var body: MultipartBody.Part =
                MultipartBody.Part.createFormData("Profile", "og", requestBody)
            call = Rest.getService().postPet(memberID, name, birth, species, Gender, weight, body)
        } else {
            call = Rest.getService().postPet(memberID, name, birth, species, Gender, weight)
        }

        call.enqueue(object : Callback<Result> {
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(baseContext, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                // 이전 액티비티로 이동
                Toast.makeText(baseContext, "반려동물이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
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

    // 생일 선택
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setDate() {
        val pickerDialog = DatePickerDialog(this, R.style.DialogTheme)

        pickerDialog.setOnDateSetListener { datePicker: DatePicker, i: Int, i1: Int, i2: Int ->
            birth = "${i}-${i1 + 1}-${i2}"
            val birthStr = "${i}년 ${i1 + 1}월 ${i2}일"
            register_tv_birth.text = birthStr
        }
        register_tv_birth.setOnClickListener {
            pickerDialog.show()
        }
    }

    // 프로필 사진 클릭 이벤트
    private fun setProfileIV() {
        register_iv_profile.setOnClickListener {
            TedPermission.with(this)
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissionListener(permissionListener)
                .check()
        }
    }

    // 외장 메모리 권한 읽기
    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, GALLERY)
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(baseContext, "권한을 허용하시지 않으면 이미지를 업로드할 수 없습니다.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY -> {
                    var imgUri: Uri? = data?.data
                    try {
                        // 화면에 표시
                        register_iv_profile.setPadding(0, 0, 0, 0)
                        Glide.with(baseContext)
                            .asBitmap()
                            .load(imgUri)
                            .apply(RequestOptions().circleCrop())
                            .into(register_iv_profile)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    // 서버 업로드
                    val cursor = contentResolver.query(imgUri!!, null, null, null, null);

                    cursor!!.moveToNext()
                    path = cursor.getString(cursor.getColumnIndex("_data"))
                    cursor.close()
                }
            }
        }
    }
}
