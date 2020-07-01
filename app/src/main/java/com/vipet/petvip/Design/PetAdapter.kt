package com.vipet.petvip.Design

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vipet.petvip.R
import com.vipet.petvip.ReserveActivity
import com.vipet.petvip.Restful.Pet
import kotlinx.android.synthetic.main.item_pet.view.*
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*

class PetAdapter(val pets: List<Pet>, val context: Context, val click: Boolean) :
    RecyclerView.Adapter<PetAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pets.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pets.get(position), context, click)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val profileIv = v.findViewById<ImageView>(R.id.item_pet_iv_profile)
        val nameTv = v.findViewById<TextView>(R.id.item_pet_tv_name)
        val intoTv = v.findViewById<TextView>(R.id.item_pet_tv_info)
        val root = v.findViewById<RelativeLayout>(R.id.item_pet_view)

        fun bind(pet: Pet, context: Context, click: Boolean) {
            nameTv.text = pet.Name
            val birthYear: Int = parseInt(pet.Birth.split("-")[0])
            val format = SimpleDateFormat("yyyy")
            val nowYear: Int = parseInt(format.format(Date()))
            val age = nowYear - birthYear + 1
            var gender = "여아"
            if (pet.Gender == 'M') {
                gender = "남아"
            }
            var info = "${pet.Species} / ${age}살 / $gender"
            intoTv.text = info
            try {
                Glide.with(context)
                    .load("http://172.19.68.108:3000/${pet.Img}")
                    .apply(RequestOptions().circleCrop())
                    .into(profileIv)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (click) {
                root.setOnClickListener {
                    (context as ReserveActivity).viewPager.currentItem = 1
                    context.pet.postValue(pet)
                }
            }
        }
    }
}
