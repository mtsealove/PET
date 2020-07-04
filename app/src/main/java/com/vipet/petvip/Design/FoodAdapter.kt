package com.vipet.petvip.Design

import android.content.Context
import android.service.autofill.Dataset
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.*
import com.vipet.petvip.R
import com.vipet.petvip.Restful.Item
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt
import java.lang.NumberFormatException


class FoodAdapter(val foodList: List<Item>, val context: Context) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(foodList[position], context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv = itemView.findViewById<TextView>(R.id.item_food_name)
        val chart = itemView.findViewById<BarChart>(R.id.item_food_chart)

        fun bind(item: Item, context: Context) {
            Log.e("item", item.crbQy)
            nameTv.text = item.feedNm
            SetChart(item, context)
        }

        //매출 차트 설정
        private fun SetChart(item: Item, context: Context) {
            val listData = ArrayList<BarEntry>()
            val data = ArrayList<Float>()
            listData.add(BarEntry(parseF(item.mitrQy), 0))
            listData.add(BarEntry(parseF(item.protQy), 1))
            listData.add(BarEntry(parseF(item.clciQy), 2))
            listData.add(BarEntry(parseF(item.phphQy), 3))
            listData.add(BarEntry(parseF(item.fatQy), 4))
            listData.add(BarEntry(parseF(item.crbQy), 5))
            listData.add(BarEntry(parseF(item.naQy), 6))
            listData.add(BarEntry(parseF(item.ptssQy), 7))
            listData.add(BarEntry(parseF(item.vtmaQy), 8))

            val dataset = BarDataSet(listData, "")
            dataset.colors = ColorTemplate.createColors(ColorTemplate.COLORFUL_COLORS)
            val bardata = BarData(getXAxisValues(), dataset)

            chart.data = bardata
            chart.setDescription("")
        }

        private fun getXAxisValues(): ArrayList<String> {
            val xAxis: ArrayList<String> = ArrayList()
            xAxis.add("수분")
            xAxis.add("단백질")
            xAxis.add("칼슘")
            xAxis.add("인")
            xAxis.add("지방")
            xAxis.add("탄수화물")
            xAxis.add("나트륨")
            xAxis.add("칼륨")
            xAxis.add("비타민 A")

            return xAxis
        }

        private fun parseF(og: String?): Float {
            og?.let { o ->
                var result = o.replace("<", "")
                result.replace(">", "")
                result.replace("!", "")
                result.trim()
                return try {
                    parseFloat(result)
                } catch (e: NumberFormatException) {
                    0f
                }
            }

            return 0f
        }
    }


}