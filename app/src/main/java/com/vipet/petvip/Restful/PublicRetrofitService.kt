package com.vipet.petvip.Restful

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import javax.security.auth.callback.Callback

interface PublicRetrofitService {
    @GET("/service/feedRawMaterial/feedRawMaterialList")
    fun getFoodList(
        @Query("apiKey") apiKey: String = "202007038ZEVDHB5V5HEXYTB0NXAFW",
        @Query("sFeedNm") fd: String
    ): Call<PublicData>

}