package com.vipet.petvip.Design

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.support.v4.media.MediaBrowserCompat
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vipet.petvip.Account.LoginActivity
import com.vipet.petvip.R
import com.vipet.petvip.ReserveActivity
import com.vipet.petvip.Restful.Manager
import com.vipet.petvip.Restful.Pet
import com.vipet.petvip.Restful.PostSchedule
import com.vipet.petvip.Restful.Rest
import com.vipet.petvip.Restful.Result
import com.vipet.petvip.ReviewActivity
import kotlinx.android.synthetic.main.item_pet.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*

class ManagerAdapter(
    val managers: List<Manager>,
    val context: Context,
    val start: String?,
    val end: String?
) :
    RecyclerView.Adapter<ManagerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_manager, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return managers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(managers[position], context, start, end)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val nameTv = v.findViewById<TextView>(R.id.item_manager_tv_name)
        val rating = v.findViewById<RatingBar>(R.id.item_manager_rating)
        val root = v.findViewById<RelativeLayout>(R.id.item_manager_root)

        fun bind(manager: Manager, context: Context, start: String?, end: String?) {
            nameTv.text = manager.Name
            rating.max = 5
            rating.rating = manager.Rate
            // 예약용
            start?.let {
                end?.let {
                    root.setOnClickListener {
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("예약 확인")
                            .setMessage("예약을 진행하시겠습니까?")
                            .setNegativeButton("취소", null)
                            .setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                                next(context, start, end, manager.ID)
                            }
                        builder.create().show()
                    }
                }
            } ?: { // 소개용
                root.setOnClickListener {
                    val intent = Intent(context, ReviewActivity::class.java)
                    intent.putExtra("MANAGER", manager.ID)
                    context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
                }
            }()
        }

        fun next(context: Context, start: String, end: String, managerID: String) {
            val act = (context as ReserveActivity)
            val petid = act.pet.value!!.ID
            val serice = act.service
            val id = LoginActivity.user.value!!.ID
            val schedule = PostSchedule(id, managerID, petid!!, serice, start, end)
            Log.e("schedule", schedule.toString())

            val call = Rest.getService().createSchedule(schedule)
            call.enqueue(object : Callback<Result> {
                override fun onFailure(call: Call<Result>, t: Throwable) {
                    Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Result>, response: Response<Result>) {
                    if (response.isSuccessful && response.body()!!.OK) {
                        Toast.makeText(context, "정상적으로 예약되었습니다.", Toast.LENGTH_SHORT).show()
                        context.finish()
                    }
                }
            })
        }
    }
}
