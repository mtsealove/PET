package com.vipet.petvip.Design

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vipet.petvip.R
import com.vipet.petvip.Restful.Review
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewAdapter(val reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val memberTv = v.findViewById<TextView>(R.id.item_review_tv_member)
        private val rating = v.findViewById<RatingBar>(R.id.item_review_rating)
        private val contentTv = v.findViewById<TextView>(R.id.item_review_tv_content)
        fun bind(review: Review) {
            memberTv.text = review.MemberName
            rating.rating = review.Rating
            contentTv.text = review.Content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(reviews[position])
    }
}