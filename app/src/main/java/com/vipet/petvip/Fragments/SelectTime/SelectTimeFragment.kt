package com.vipet.petvip.Fragments.SelectTime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.vipet.petvip.Design.NextBtn
import com.vipet.petvip.Fragments.SectionsPagerAdapter
import com.vipet.petvip.Fragments.SelectManagerFragment

import com.vipet.petvip.R
import com.vipet.petvip.ReserveActivity
import java.lang.Integer.parseInt
import java.util.*


class SelectTimeFragment : Fragment() {
    lateinit var dateTv: TextView
    lateinit var timeTv: TextView
    lateinit var timeSp: Spinner
    lateinit var priceTv: TextView
    val price = arrayOf("18,000", "26,000", "30,000", "35,000")
    var priceInt = parseInt(price[0].replace(",", ""))
    val viewModel by lazy {
        TimeViewModel()
    }

    companion object {
        lateinit var date: String
        lateinit var nextBtn: NextBtn
    }

    var time = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_select_time, container, false)
        dateTv = root.findViewById(R.id.select_time_tv_date)
        timeTv = root.findViewById(R.id.select_time_tv_time)
        nextBtn = root.findViewById(R.id.select_time_btn_next)
        timeSp = root.findViewById(R.id.select_time_sp_time)
        nextBtn.setTitle("다음")
        priceTv = root.findViewById(R.id.select_time_price)
        priceTv.text = price[0] + "원"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setDate()
        }
        setTime()
        setNext()
        setPrice()
        return root
    }

    private fun setPrice() {
        timeSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val p = price[position]
                priceInt = parseInt(price[position].replace(",", ""))
                priceTv.text = "${p} 원"
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setDate() {
        dateTv.setOnClickListener {
            context?.let {
                val picker = DatePickerDialog(it)
                picker.datePicker.minDate = Calendar.getInstance().timeInMillis
                picker.setOnDateSetListener { view, year, month, dayOfMonth ->
                    date = "$year-"
                    if (month < 9) {
                        date += "0"
                    }
                    date += "${month + 1}-"
                    if (dayOfMonth < 10) {
                        date += "0"
                    }
                    date += dayOfMonth
                    dateTv.text =
                        date
                }
                picker.show()
            }
        }
    }

    private fun setTime() {
        timeTv.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (hourOfDay < 10) {
                    time += "0"
                }
                time += "${hourOfDay}:"
                if (minute < 10) {
                    time += "0"
                }
                time += minute
                timeTv.text = time
            }
            val cal = Calendar.getInstance()
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun setNext() {
        nextBtn.root.setOnClickListener {
            when {
                date.isEmpty() -> {
                    Toast.makeText(context, "날짜를 선택하세요.", Toast.LENGTH_SHORT).show()
                }
                time.isEmpty() -> {
                    Toast.makeText(context, "시간을 선택하세요.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 시작, 종료시간 설정
                    viewModel.setStart("$date $time")
                    viewModel.setEnd(getEndTime())
                    ReserveActivity.Start = "$date $time"
                    ReserveActivity.End = getEndTime()
                    val adapter: SectionsPagerAdapter =
                        (context as ReserveActivity).viewPager.adapter as SectionsPagerAdapter
                    adapter.fragments.add(
                        SelectManagerFragment(
                            "$date $time",
                            getEndTime(),
                            priceInt
                        )
                    )
                    (context as ReserveActivity).viewPager.adapter = adapter

                    (context as ReserveActivity).viewPager.currentItem = 2
                }
            }
        }
    }

    private fun getEndTime(): String {
        val timesplit = time.split(":")
        var hour = parseInt(timesplit[0])
        var min = parseInt(timesplit[1])
        when (timeSp.selectedItemPosition) {
            0 -> {
                hour += 1
            }
            1 -> {
                hour += 1
                min += 30
            }
            2 -> {
                hour += 2
            }
            3 -> {
                hour += 2
                min += 30
            }
        }

        if (min > 60) {
            min -= 60
            hour += 1
        }

        return "$date $hour:$hour"
    }
}

