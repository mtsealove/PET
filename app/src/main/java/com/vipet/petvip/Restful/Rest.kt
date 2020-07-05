package com.vipet.petvip.Restful

import com.vipet.petvip.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Rest {
    private val retrofit =
        Retrofit.Builder()
            .baseUrl("http://13.125.209.134:3000") // 도메인 주소
            .addConverterFactory(GsonConverterFactory.create()) // GSON을 사요아기 위해 ConverterFactory에 GSON 지정
            .build()

    fun getService(): RetrofitService = retrofit.create(RetrofitService::class.java)
}