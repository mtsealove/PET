package com.vipet.petvip.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.vipet.petvip.Account.LoginActivity
import com.vipet.petvip.R
import com.vipet.petvip.Restful.Benefits
import com.vipet.petvip.Restful.Item
import com.vipet.petvip.Restful.Rest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BenefitFragment : Fragment() {
    private lateinit var monthChart: BarChart
    private lateinit var totalChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_benefit, container, false)
        monthChart = view.findViewById(R.id.benefit_chart_month)
        totalChart = view.findViewById(R.id.benefit_chart_total)
        getBenefits()
        return view
    }

    private fun getBenefits() {
        val id = LoginActivity.user.value!!.ID
        val call = Rest.getService().getBenefits(id)
        call.enqueue(object : Callback<Benefits> {
            override fun onFailure(call: Call<Benefits>, t: Throwable) {
                Toast.makeText(context, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Benefits>, response: Response<Benefits>) {
                if (response.isSuccessful) {
                    response.body()?.let { benefits ->
                        SetChart(benefits)
                    }
                }
            }

        })
    }

    //수익 차트 설정
    private fun SetChart(benefits: Benefits) {
        // 이번달 차트
        val monthData = ArrayList<BarEntry>()
        val monthLabel = ArrayList<String>()
        var idx = 0
        for (benefit in benefits.Month) {
            monthData.add(BarEntry(benefit.Price.toFloat(), idx++))
            monthData.add(BarEntry(benefit.Cnt.toFloat(), idx++))
            monthLabel.add("${benefit.Service} 주문 금액")
            monthLabel.add("${benefit.Service} 주문 회수")
        }

        val monthDataSet = BarDataSet(monthData, "")
        monthDataSet.colors = ColorTemplate.createColors(ColorTemplate.PASTEL_COLORS)
        monthChart.data = BarData(monthLabel, monthDataSet)
        monthChart.setDescription("")
        monthChart.animateY(1000)
        // 전체 차트
        val totalData = ArrayList<BarEntry>()
        val totalLabel = ArrayList<String>()
        idx = 0
        for (benefit in benefits.Total) {
            totalData.add(BarEntry(benefit.Price.toFloat(), idx++))
            totalData.add(BarEntry(benefit.Cnt.toFloat(), idx++))
            totalLabel.add("${benefit.Service} 주문 금액")
            totalLabel.add("${benefit.Service} 주문 회수")
        }
        val totalDataSet = BarDataSet(totalData, "")
        totalDataSet.colors = ColorTemplate.createColors(ColorTemplate.JOYFUL_COLORS)
        totalChart.data = BarData(totalLabel, totalDataSet)
        totalChart.setDescription("")
        totalChart.animateY(1000)
    }
}