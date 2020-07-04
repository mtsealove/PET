package com.vipet.petvip.Design

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vipet.petvip.R
import com.vipet.petvip.Restful.ManagerSchedule
import com.vipet.petvip.Restful.Pet
import com.vipet.petvip.Restful.Rest
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManagerScheduleAdapter(
    private val scheduleList: List<ManagerSchedule>,
    val context: Context
) :
    RecyclerView.Adapter<ManagerScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_manager_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(scheduleList[position], context)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val serviceTv = v.findViewById<TextView>(R.id.item_schedule_tv_service)
        private val petTv = v.findViewById<TextView>(R.id.item_schedule_tv_pet)
        private val monthTv = v.findViewById<TextView>(R.id.item_schedule_tv_month)
        private val dayTv = v.findViewById<TextView>(R.id.item_schedule_tv_day)
        private val dateTv = v.findViewById<TextView>(R.id.item_schedule_tv_date)
        private val memberTv = v.findViewById<TextView>(R.id.item_schedule_tv_member)
        private val startTv = v.findViewById<TextView>(R.id.item_schedule_tv_start)
        private val endTv = v.findViewById<TextView>(R.id.item_schedule_tv_end)
        private val petInfoTv = v.findViewById<TextView>(R.id.item_schedule_tv_pet_info)
        private val locationTv = v.findViewById<TextView>(R.id.item_schedule_tv_location)
        private val phoneTv = v.findViewById<TextView>(R.id.item_schedule_tv_phone)

        fun bind(schedule: ManagerSchedule, context: Context) {
            val date = schedule.Start.split(" ")[0]
            Log.e("date", date)
            val start = schedule.Start.split(" ")[1]
            val end = schedule.End.split(" ")[1]
            val month = date.split("-")[1]
            val day = date.split("-")[2]
            when (schedule.ServiceType) {
                0 -> serviceTv.text = "돌봄"
                1 -> serviceTv.text = "산책"
            }
            memberTv.text = schedule.MemberName
            dateTv.text = date
            startTv.text = start
            endTv.text = end
            petTv.text = schedule.PetName
            dayTv.text = day
            monthTv.text = month + "월"
            phoneTv.text = schedule.Phone
            phoneTv.setOnClickListener {
                callTo(schedule.Phone, context)
            }
            petInfoTv.setOnClickListener {
                getPet(schedule.PetID, context)
            }
            locationTv.setOnClickListener {
                showLocation(schedule.Addr, context)
            }
        }

        // 다이얼로 이동
        private fun callTo(phone: String, context: Context) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.replace("-", "")}"))
            context.startActivity(intent)
        }

        // 반려견정보 가져오기
        private fun getPet(id: Int, context: Context) {
            val call = Rest.getService().getPet(id)
            call.enqueue(object : Callback<Pet> {
                override fun onFailure(call: Call<Pet>, t: Throwable) {
                    Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                    Log.e("err", t.toString())
                }

                override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                    if (response.isSuccessful && response.body() != null) {
                        showPetDialog(response.body()!!, context)
                    }
                }
            })
        }

        // 반려견 정보 가져오기
        private fun showPetDialog(pet: Pet, context: Context) {
            val builder = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_pet, null, false)
            val profileIv = view.findViewById<ImageView>(R.id.dialog_pet_img)
            val nameTv = view.findViewById<TextView>(R.id.dialog_pet_name)
            val birthTv = view.findViewById<TextView>(R.id.dialog_pet_birth)
            val speciesTv = view.findViewById<TextView>(R.id.dialog_pet_species)
            val genderTv = view.findViewById<TextView>(R.id.dialog_pet_Gender)
            val weightTv = view.findViewById<TextView>(R.id.dialog_pet_weight)

            nameTv.text = pet.Name
            birthTv.text = pet.Birth
            speciesTv.text = pet.Species
            genderTv.text = when (pet.Gender) {
                'F' -> "여아"
                else -> "남아"
            }
            weightTv.text = "${pet.Weight} Kg"
            Glide.with(context)
                .load("http://172.30.101.1:3000/${pet.Img}").circleCrop()
                .into(profileIv)
            builder.setView(view)
            builder.create().show()
        }

        // 위치 정보 확인 다이얼로그
        private fun showLocation(addr: String, context: Context) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_location, null, false)
            val tv = view.findViewById<TextView>(R.id.dialog_location_tv)
            val container = view.findViewById<RelativeLayout>(R.id.dialog_location_container)
            // adb 불가
            val mapView = MapView(context)
            container.addView(mapView)
            tv.text = addr
            val geoCoder = Geocoder(context)
            val list = geoCoder.getFromLocationName(addr, 3)
            if (list.isNotEmpty()) {
                Log.e("latitude", list[0].latitude.toString())
                Log.e("logitude", list[0].longitude.toString())
            } else {
                Log.e("location", "not found")
            }
            val builder = AlertDialog.Builder(context)
            builder.setView(view)
            builder.create().show()
        }
    }
}