package com.vipet.petvip.Design

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import com.vipet.petvip.R

open class NextBtn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    lateinit var titleTv: TextView
    lateinit var root: RelativeLayout

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_next_btn, null, false)
        titleTv = view.findViewById(R.id.view_btn_tv_title)
        root = view.findViewById(R.id.view_next_btn_root)
        addView(view)
    }

    fun setTitle(title: String) {
        titleTv.text = title
    }
}