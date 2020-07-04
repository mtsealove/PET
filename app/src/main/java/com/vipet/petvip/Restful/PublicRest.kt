package com.vipet.petvip.Restful

import com.vipet.petvip.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

// 공공데이터 접속용
object PublicRest {
    private val retrofit =
        Retrofit.Builder()
            .baseUrl("http://api.nongsaro.go.kr") // 도메인 주소
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()

    fun getService(): PublicRetrofitService = retrofit.create(PublicRetrofitService::class.java)
}