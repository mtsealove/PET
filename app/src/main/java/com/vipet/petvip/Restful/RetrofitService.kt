package com.vipet.petvip.Restful

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("/Overlap")
    fun idOverlap(@Query("ID") id: String): Call<Result>

    @POST("/SignUp")
    fun Join(@Body account: Account): Call<Result>

    @POST("/Login")
    fun Login(@Body loginData: LoginData): Call<Account>

    @GET("/Pets")
    fun getPets(@Query("ID") id: String): Call<List<Pet>>

    @Multipart
    @POST("/Register/Pet")
    fun postPet(
        @Part("MemberID") id: String,
        @Part("Name") name: String,
        @Part("Birth") birth: String,
        @Part("Species") species: String,
        @Part("Gender") gender: Char,
        @Part("Weight") weight: Int,
        @Part img: MultipartBody.Part
    ): Call<Result>

    @Multipart
    @POST("/Register/Pet")
    fun postPet(
        @Part("MemberID") id: String,
        @Part("Name") name: String,
        @Part("Birth") birth: String,
        @Part("Species") species: String,
        @Part("Gender") gender: Char,
        @Part("Weight") weight: Int
    ): Call<Result>

    @GET("/AbleManager")
    fun getAbleManagers(
        @Query("Start") start: String,
        @Query("End") end: String
    ): Call<List<Manager>>

    @POST("/Create/Schedule")
    fun createSchedule(@Body schedule: PostSchedule): Call<Result>

    @GET("/Schedule")
    fun getSchedule(@Query("ID") id: String, @Query("Limit") limit: Int): Call<List<Schedule>>

    @GET("/Reviews/All")
    fun getAllReviews(): Call<List<Review>>

    @GET("/Reviews")
    fun getDetailManager(@Query("ID") id: String): Call<ManagerDetail>

    @POST("/Create/Review")
    fun createReview(@Body review: PostReview): Call<Result>

    @GET("/Managers")
    fun getAllManagers(): Call<List<Manager>>
}